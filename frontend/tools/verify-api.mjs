/**
 * Prueba de integración con datos sintéticos en una base desechable.
 * Requiere Spring conectado al simulador del equipo en scripts/culqi-mock/server.py.
 * QA_APPROVED_TOKEN permite utilizar otro simulador compatible.
 * No ejecutar contra producción: crea cuentas, tiendas, productos y órdenes.
 * Ejecutar: node tools/verify-api.mjs --disposable
 */
import assert from 'node:assert/strict';
import { randomUUID } from 'node:crypto';

if (!process.argv.includes('--disposable')) {
  throw new Error('Esta prueba crea datos. Usa --disposable únicamente con una base desechable.');
}
const base = process.env.QA_API_URL || 'http://localhost:8080/api';
if (!['localhost', '127.0.0.1'].includes(new URL(base).hostname)) {
  throw new Error('La prueba solo admite servidores locales de desarrollo.');
}
const suffix = Date.now().toString();
const password = 'BodekoTest2026!';
const approvedToken = process.env.QA_APPROVED_TOKEN || 'tkn_test_ok';
let assertions = 0;
async function request(path, { method = 'GET', body, token, expected = 200 } = {}) {
  const response = await fetch(`${base}${path}`, {
    method,
    headers: {
      ...(body ? { 'Content-Type': 'application/json' } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(['POST', 'PATCH'].includes(method) &&
      /\/orders(?:\/[^/]+\/(?:payments|cancel|status))?$/.test(path)
        ? { 'Idempotency-Key': randomUUID() }
        : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  assert.equal(
    response.status,
    expected,
    `${method} ${path}: esperaba ${expected}, recibió ${response.status}`,
  );
  assertions++;
  return response.status === 204 ? null : response.json();
}
const ownerEmail = `qa.owner.${suffix}@example.test`;
const customerEmail = `qa.customer.${suffix}@example.test`;
const owner = await request('/auth/register-owner', {
  method: 'POST',
  expected: 201,
  body: {
    email: ownerEmail,
    password,
    fullName: 'Propietario de Prueba',
    phoneNumber: '+51999999999',
  },
});
const customer = await request('/auth/register-customer', {
  method: 'POST',
  expected: 201,
  body: {
    email: customerEmail,
    password,
    fullName: 'Cliente de Prueba',
    documentType: 'DNI',
    documentNumber: '12345678',
  },
});
await request('/auth/register-customer', {
  method: 'POST',
  expected: 409,
  body: { email: customerEmail, password, fullName: 'Duplicado de Prueba' },
});
await request('/auth/register-customer', {
  method: 'POST',
  expected: 400,
  body: {
    email: `qa.invalid.${suffix}@example.test`,
    password,
    fullName: 'Documento Invalido',
    documentType: 'DNI',
    documentNumber: '123',
  },
});
await request('/auth/me', { token: owner.token });
const store = await request('/stores', {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    name: 'Tienda QA Bodeko',
    taxId: `20${suffix.slice(-9)}`,
    legalName: 'Comercio de Prueba S.A.C.',
    subdomain: `qa-${suffix}`,
  },
});
assert.equal(store.taxRate, 0.18);
await request(`/public/stores/by-subdomain/${store.subdomain}`);
const publicStores = await request('/public/stores');
assert.ok(publicStores.some((item) => item.id === store.id));
assert.equal(publicStores.find((item) => item.id === store.id).taxId, undefined);
await request(`/stores/${store.id}/products`, { token: customer.token, expected: 403 });
const category = await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: { name: 'Abarrotes QA' },
});
await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 409,
  body: { name: 'Abarrotes QA' },
});
await request(`/public/stores/${store.id}/categories`);
const product = await request(`/stores/${store.id}/products`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    name: 'Café peruano de prueba',
    description: 'Producto sintético para validar la integración.',
    basePrice: 25.9,
    imageUrl: null,
    categoryId: category.id,
    taxExempt: false,
  },
});
assert.equal(product.status, 'DRAFT');
await request(`/public/stores/${store.id}/products/${product.id}`, { expected: 404 });
const stock = await request(`/stores/${store.id}/products/${product.id}/inventory`, {
  token: owner.token,
});
assert.equal(stock.quantity, 0);
assert.equal(stock.lowStockThreshold, 5);
await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
  method: 'PATCH',
  token: owner.token,
  body: { quantity: 20 },
});
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ACTIVE' },
});
await request(`/stores/${store.id}/categories/${category.id}`, {
  method: 'DELETE',
  token: owner.token,
  expected: 409,
});
await request(`/stores/${store.id}/products/${product.id}/offers`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: {
    discountPercentage: 22.78,
    startsAt: new Date(Date.now() - 60000).toISOString(),
    endsAt: new Date(Date.now() + 86400000).toISOString(),
  },
});
const catalog = await request(`/public/stores/${store.id}/products?search=caf&inStock=true`);
assert.equal(catalog.totalElements, 1);
assert.equal(catalog.content[0].offerPrice, 20);
await request(`/public/stores/${store.id}/products/${product.id}`);
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
const cart = await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
assert.equal(cart.totalQuantity, 2);
assert.equal(cart.items.length, 1);
assert.equal(cart.subtotal, 40);
const login = await request('/auth/login', {
  method: 'POST',
  body: { email: customerEmail, password },
});
assert.equal(
  (await request(`/customer/stores/${store.id}/cart`, { token: login.token })).totalQuantity,
  2,
);
const order = await request(`/customer/stores/${store.id}/orders`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: {
    invoiceType: 'RECEIPT',
    documentType: 'DNI',
    documentNumber: '12345678',
    businessName: null,
    billingAddress: 'Dirección sintética de prueba',
  },
});
assert.equal(order.subtotal, 40);
assert.equal(order.taxAmount, 7.2);
assert.equal(order.total, 47.2);
const other = await request('/auth/register-customer', {
  method: 'POST',
  expected: 201,
  body: { email: `qa.other.${suffix}@example.test`, password, fullName: 'Otro Cliente' },
});
await request(`/customer/stores/${store.id}/orders/${order.id}`, {
  token: other.token,
  expected: 404,
});
await request(`/customer/stores/${store.id}/orders/${order.id}`, { token: customer.token });
assert.equal(
  (await request(`/stores/${store.id}/products/${product.id}/inventory`, { token: owner.token }))
    .quantity,
  18,
);
await request(`/customer/stores/${store.id}/orders/${order.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 402,
  body: { sourceId: 'tkn_test_declined' },
});
assert.equal(
  (await request(`/customer/stores/${store.id}/orders/${order.id}`, { token: customer.token }))
    .status,
  'PENDING',
);
const payment = await request(`/customer/stores/${store.id}/orders/${order.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: { sourceId: approvedToken },
});
assert.equal(payment.status, 'APPROVED');
assert.equal(
  (await request(`/stores/${store.id}/orders/${order.id}`, { token: owner.token })).status,
  'PAID',
);
await request(`/stores/${store.id}/orders/${order.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'CANCELLED' },
});
assert.equal(
  (await request(`/stores/${store.id}/products/${product.id}/inventory`, { token: owner.token }))
    .quantity,
  20,
);
await request(`/stores/${store.id}/tax-rate`, {
  method: 'PATCH',
  token: owner.token,
  body: { taxRate: 0.18 },
});
await request(`/stores/${store.id}/products/${product.id}/inventory/low-stock-threshold`, {
  method: 'PATCH',
  token: owner.token,
  body: { lowStockThreshold: 4 },
});
await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
  method: 'PATCH',
  token: owner.token,
  expected: 400,
  body: { quantity: -1 },
});
await request(`/stores/${store.id}/products/${product.id}/offers`, {
  method: 'POST',
  token: owner.token,
  expected: 400,
  body: {
    discountPercentage: 10,
    startsAt: '2026-11-20T10:00:00Z',
    endsAt: '2026-11-19T10:00:00Z',
  },
});
const emptyCategory = await request(`/stores/${store.id}/categories`, {
  method: 'POST',
  token: owner.token,
  expected: 201,
  body: { name: 'Temporal QA' },
});
await request(`/stores/${store.id}/categories/${emptyCategory.id}`, {
  method: 'PUT',
  token: owner.token,
  body: { name: 'Temporal editada QA' },
});
await request(`/stores/${store.id}/categories/${emptyCategory.id}`, {
  method: 'DELETE',
  token: owner.token,
  expected: 204,
});
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
await request(`/customer/stores/${store.id}/cart/items/${product.id}`, {
  method: 'PATCH',
  token: customer.token,
  body: { quantity: 3 },
});
const emptyCart = await request(`/customer/stores/${store.id}/cart/items/${product.id}`, {
  method: 'DELETE',
  token: customer.token,
});
assert.equal(emptyCart.totalQuantity, 0);
await request(`/customer/stores/${store.id}/cart/items`, {
  method: 'POST',
  token: customer.token,
  body: { productId: product.id, quantity: 1 },
});
const invoice = await request(`/customer/stores/${store.id}/orders`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: {
    invoiceType: 'INVOICE',
    documentType: 'RUC',
    documentNumber: '20123456789',
    businessName: 'Empresa de Prueba S.A.C.',
  },
});
await request(`/customer/stores/${store.id}/orders/${invoice.id}/payments`, {
  method: 'POST',
  token: customer.token,
  expected: 201,
  body: { sourceId: approvedToken },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'SHIPPED' },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'DELIVERED' },
});
await request(`/stores/${store.id}/orders/${invoice.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  expected: 409,
  body: { status: 'CANCELLED' },
});
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ARCHIVED' },
});
await request(`/public/stores/${store.id}/products/${product.id}`, { expected: 404 });
await request(`/stores/${store.id}/products/${product.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ACTIVE' },
});
console.log(
  JSON.stringify(
    {
      status: 'PASS',
      httpChecks: assertions,
      subdomain: store.subdomain,
      storeId: store.id,
      productId: product.id,
      orderId: order.id,
      syntheticAccounts: { ownerEmail, customerEmail },
      note: 'Solo datos sintéticos en base desechable. Pago simulado; API y PostgreSQL reales.',
    },
    null,
    2,
  ),
);
