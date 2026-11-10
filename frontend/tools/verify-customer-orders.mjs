/**
 * Regresión del área de cliente contra Spring y PostgreSQL reales.
 * Solo para una BD desechable y el simulador del equipo, sin cobros reales.
 * QA_API_URL=http://localhost:8081/api node tools/verify-customer-orders.mjs --disposable
 * Crea cuentas, tiendas, productos y órdenes sintéticas. No elimina esos datos.
 */
import assert from 'node:assert/strict';

if (!process.argv.includes('--disposable'))
  throw new Error('Usa --disposable con una base de pruebas.');
const base = process.env.QA_API_URL || 'http://localhost:8081/api';
if (!['localhost', '127.0.0.1'].includes(new URL(base).hostname))
  throw new Error('Solo se permiten servidores locales.');
const suffix = Date.now().toString();
const password = 'BodekoOrders2026!';
let checks = 0;
async function request(path, { token, method = 'GET', body, status = 200 } = {}) {
  const response = await fetch(base + path, {
    method,
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(body ? { 'Content-Type': 'application/json' } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  assert.equal(response.status, status, `${method} ${path}`);
  checks++;
  return response.json();
}
async function register(email, owner = false) {
  return (
    await request(`/auth/register-${owner ? 'owner' : 'customer'}`, {
      method: 'POST',
      status: 201,
      body: {
        fullName: owner ? 'Propietario QA Ordenes' : 'Cliente QA Ordenes',
        email,
        password,
        phoneNumber: '+51999999888',
      },
    })
  ).token;
}
const admin = await register(`orders.owner.${suffix}@example.test`, true);
const customer = await register(`orders.customer.${suffix}@example.test`);
const other = await register(`orders.other.${suffix}@example.test`);
await request('/customer/orders', { status: 401 });
await request('/customer/orders', { token: admin, status: 403 });
assert.equal((await request('/customer/orders', { token: customer })).totalElements, 0);

async function fixtureStore(index) {
  const store = await request('/stores', {
    method: 'POST',
    token: admin,
    status: 201,
    body: {
      name: `Tienda QA Ordenes ${index}`,
      taxId: `29${suffix.slice(-8)}${index}`,
      legalName: 'Comercio de Prueba',
      subdomain: `orders-${suffix}-${index}`,
    },
  });
  const product = await request(`/stores/${store.id}/products`, {
    method: 'POST',
    token: admin,
    status: 201,
    body: {
      name: 'Producto de prueba de ordenes',
      basePrice: 10,
      taxExempt: false,
    },
  });
  await request(`/stores/${store.id}/products/${product.id}/inventory/quantity`, {
    method: 'PATCH',
    token: admin,
    body: { quantity: 40 },
  });
  await request(`/stores/${store.id}/products/${product.id}/status`, {
    method: 'PATCH',
    token: admin,
    body: { status: 'ACTIVE' },
  });
  return { store, product };
}
const first = await fixtureStore(1);
const second = await fixtureStore(2);
async function checkout(fixture, token = customer) {
  await request(`/customer/stores/${fixture.store.id}/cart/items`, {
    method: 'POST',
    token,
    body: { productId: fixture.product.id, quantity: 2 },
  });
  return request(`/customer/stores/${fixture.store.id}/orders`, {
    method: 'POST',
    token,
    status: 201,
    body: { invoiceType: 'RECEIPT', documentType: 'DNI', documentNumber: '12345678' },
  });
}
async function pay(fixture, order) {
  return request(`/customer/stores/${fixture.store.id}/orders/${order.id}/payments`, {
    method: 'POST',
    token: customer,
    status: 201,
    body: { sourceId: process.env.QA_APPROVED_TOKEN || 'tkn_test_ok' },
  });
}
async function stock(fixture) {
  return (
    await request(`/stores/${fixture.store.id}/products/${fixture.product.id}/inventory`, {
      token: admin,
    })
  ).quantity;
}
const pending = await checkout(first);
const paid = await checkout(first);
await pay(first, paid);
const shipped = await checkout(second);
await pay(second, shipped);
await request(`/stores/${second.store.id}/orders/${shipped.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'SHIPPED' },
});
const delivered = await checkout(second);
await pay(second, delivered);
await request(`/stores/${second.store.id}/orders/${delivered.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'SHIPPED' },
});
await request(`/stores/${second.store.id}/orders/${delivered.id}/status`, {
  method: 'PATCH',
  token: admin,
  body: { status: 'DELIVERED' },
});
const foreign = await checkout(first, other);
const list = await request('/customer/orders?size=2', { token: customer });
assert.equal(list.totalElements, 4);
assert.equal(list.content.length, 2);
assert.equal(list.totalPages, 2);
const all = await request('/customer/orders', { token: customer });
assert.equal(new Set(all.content.map((order) => order.storeId)).size, 2);
assert.ok(!all.content.some((order) => order.id === foreign.id));
assert.equal(
  (await request('/customer/orders?status=PAID', { token: customer })).content[0].id,
  paid.id,
);
assert.equal((await request('/customer/orders?page=-1&size=0', { token: customer })).size, 1);
await request('/customer/orders?status=INVALID', { token: customer, status: 400 });
await request(`/customer/orders/${foreign.id}`, { token: customer, status: 404 });
await request(`/customer/orders/${foreign.id}/cancel`, {
  method: 'POST',
  token: customer,
  status: 404,
});
await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: other,
  status: 404,
});
const before = await stock(first);
const cancellation = await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: customer,
});
assert.equal(cancellation.order.status, 'CANCELLED');
assert.equal(await stock(first), before + 2);
await request(`/customer/orders/${pending.id}/cancel`, {
  method: 'POST',
  token: customer,
  status: 409,
});
assert.equal(await stock(first), before + 2);
assert.equal(
  (await request(`/customer/orders/${paid.id}/cancel`, { method: 'POST', token: customer })).order
    .status,
  'CANCELLED',
);
assert.equal(await stock(first), before + 4);
for (const order of [shipped, delivered]) {
  await request(`/customer/orders/${order.id}/cancel`, {
    method: 'POST',
    token: customer,
    status: 409,
  });
}
// Dos cancelaciones simultáneas: una gana el bloqueo y la otra no repone stock otra vez.
const concurrent = await checkout(first);
const beforeConcurrent = await stock(first);
const attempts = await Promise.all(
  [1, 2].map(() =>
    fetch(`${base}/customer/orders/${concurrent.id}/cancel`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${customer}` },
    }),
  ),
);
assert.deepEqual(attempts.map((response) => response.status).sort(), [200, 409]);
checks += 2;
assert.equal(await stock(first), beforeConcurrent + 2);
// Se requiere el seed demo para este caso: el simulador no conoce su cargo histórico.
const login = await request('/auth/login', {
  method: 'POST',
  body: { email: 'cliente.demo@bodeko.test', password: 'BodekoDemo2026!' },
});
const seedPaid = 'b0de7000-0000-4000-8000-000000000002';
await request(`/customer/orders/${seedPaid}/cancel`, {
  method: 'POST',
  token: login.token,
  status: 422,
});
assert.equal(
  (await request(`/customer/orders/${seedPaid}`, { token: login.token })).order.status,
  'PAID',
);
console.log(
  JSON.stringify(
    {
      result: 'PASS',
      httpChecks: checks,
      scenarios: [
        'listado de varias tiendas',
        'paginación y filtro',
        'aislamiento por cliente y rol',
        'cancelación pendiente y pagada',
        'restricción enviada/entregada',
        'concurrencia sin doble stock',
        'rollback al rechazar reembolso',
      ],
      paidOrderId: paid.id,
      cancelledOrderId: pending.id,
    },
    null,
    2,
  ),
);
