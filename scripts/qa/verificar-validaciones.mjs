/** Prueba de API con datos sintéticos. Exige una base local vacía y un simulador QA dedicado. */

import assert from 'node:assert/strict';

import { randomUUID } from 'node:crypto';

if (!process.argv.includes('--disposable') || !process.env.QA_API_URL || !process.env.QA_MOCK_URL)
  throw new Error('Configura QA_API_URL y QA_MOCK_URL y usa --disposable.');

const base = process.env.QA_API_URL.replace(/\/$/, '');

const mock = process.env.QA_MOCK_URL.replace(/\/$/, '');

for (const address of [base, mock])
  if (!['127.0.0.1', 'localhost'].includes(new URL(address).hostname))
    throw new Error('Solo se admiten servidores locales de QA.');

let checks = 0;

async function raw(path, { method = 'GET', token, body, key, expected } = {}) {
  const headers = {
    ...(body ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };

  if (
    key !== null &&
    ['POST', 'PATCH'].includes(method) &&
    /\/orders(?:\/[^/]+\/(?:payments|cancel|status))?$/.test(path)
  )
    headers['Idempotency-Key'] = key || randomUUID();

  const response = await fetch(base + path, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  const data = response.status === 204 ? null : await response.json();

  if (expected !== undefined)
    assert.ok(
      (Array.isArray(expected) ? expected : [expected]).includes(response.status),
      `${method} ${path}: ${response.status} ${JSON.stringify(data)}`,
    );
  else assert.ok(response.status < 500, `${method} ${path}: error inesperado ${response.status}`);

  checks++;

  return { status: response.status, data };
}

async function api(path, options = {}) {
  return (await raw(path, options)).data;
}

const stats = async () => {
  const response = await fetch(mock + '/qa/stats');
  assert.equal(response.status, 200);
  return response.json();
};

assert.deepEqual(
  await api('/public/stores', { expected: 200 }),
  [],
  'La base de QA debe estar vacía; no continuar con una base de trabajo.',
);

assert.equal(
  (await stats()).charges,
  0,
  'Reinicia únicamente el simulador de QA antes de esta prueba.',
);

const password = 'BodekoPruebas2026!';

const signup = (email, name = 'Cliente QA') => ({
  email,
  password,
  fullName: name,
  phoneNumber: '+51999999111',
});

const owner = await api('/auth/register-owner', {
  method: 'POST',
  body: signup('owner.validation@example.test', 'Propietario QA'),
  expected: 201,
});

const a = await api('/auth/register-customer', {
  method: 'POST',
  body: signup('a.validation@example.test'),
  expected: 201,
});

const b = await api('/auth/register-customer', {
  method: 'POST',
  body: signup('b.validation@example.test'),
  expected: 201,
});

await api('/auth/register-customer', {
  method: 'POST',
  body: signup('A.Validation@EXAMPLE.test'),
  expected: 409,
});

const emailRace = await Promise.all(
  [1, 2].map(() =>
    raw('/auth/register-customer', {
      method: 'POST',
      body: signup('race.validation@example.test'),
    }),
  ),
);

assert.deepEqual(emailRace.map((x) => x.status).sort(), [201, 409]);

for (const invalid of ['x'.repeat(73), 'á'.repeat(37)])
  await api('/auth/register-customer', {
    method: 'POST',
    body: { ...signup('invalid@example.test'), password: invalid },
    expected: 400,
  });

await api('/auth/register-customer', {
  method: 'POST',
  body: signup('not-an-email'),
  expected: 400,
});

const storeBody = {
  name: 'Mercado Validaciones',
  taxId: '20987654001',
  legalName: 'Empresa QA Ficticia',
  subdomain: 'qa-validaciones',
};

const store = await api('/stores', {
  method: 'POST',
  token: owner.token,
  body: storeBody,
  expected: 201,
});

await api('/stores', {
  method: 'POST',
  token: owner.token,
  body: { ...storeBody, name: 'Otra tienda', subdomain: 'otro' },
  expected: 409,
});

await api('/stores', {
  method: 'POST',
  token: owner.token,
  body: { ...storeBody, name: 'Otra tienda', taxId: '20987654002', subdomain: 'QA-VALIDACIONES' },
  expected: 409,
});

await api('/stores', {
  method: 'POST',
  token: owner.token,
  body: {
    ...storeBody,
    name: '  MERCADO    VALIDACIONES  ',
    taxId: '20987654003',
    subdomain: 'qa-doble',
  },
  expected: 409,
});

const storeRace = await Promise.all(
  [4, 5].map((n) =>
    raw('/stores', {
      method: 'POST',
      token: owner.token,
      body: {
        ...storeBody,
        name: 'Tienda simultánea',
        taxId: `2098765400${n}`,
        subdomain: `qa-race-${n}`,
      },
    }),
  ),
);

assert.deepEqual(storeRace.map((x) => x.status).sort(), [201, 409]);

const store2 = await api('/stores', {
  method: 'POST',
  token: owner.token,
  body: { ...storeBody, name: 'Otra sucursal', taxId: '20987654006', subdomain: 'qa-sucursal' },
  expected: 201,
});

const adminPath = `/stores/${store.id}`;

const customerPath = `/customer/stores/${store.id}`;

const productBody = (name, price = 20) => ({
  name,
  basePrice: price,
  description: 'Ejemplo sintético',
  taxExempt: false,
});

async function product(name, stock = 10, price = 20) {
  const p = await api(adminPath + '/products', {
    method: 'POST',
    token: owner.token,
    body: productBody(name, price),
    expected: 201,
  });

  await api(`${adminPath}/products/${p.id}/inventory/quantity`, {
    method: 'PATCH',
    token: owner.token,
    body: { quantity: stock },
    expected: 200,
  });

  await api(`${adminPath}/products/${p.id}/status`, {
    method: 'PATCH',
    token: owner.token,
    body: { status: 'ACTIVE' },
    expected: 200,
  });

  return p;
}

const p = await product('Café para validación');
await api(`${adminPath}/products/${p.id}/status`, { method: 'PATCH', token: owner.token, body: { status: 0 }, expected: 400 });
await api(customerPath + '/cart/items', { method: 'POST', token: a.token, body: { productId: p.id, quantity: 1.5 }, expected: 400 });

await api(adminPath + '/products', {
  method: 'POST',
  token: owner.token,
  body: productBody(' CAFÉ   PARA VALIDACIÓN '),
  expected: 409,
});

const productRace = await Promise.all(
  [1, 2].map(() =>
    raw(adminPath + '/products', {
      method: 'POST',
      token: owner.token,
      body: productBody('Producto simultáneo'),
    }),
  ),
);

assert.deepEqual(productRace.map((x) => x.status).sort(), [201, 409]);

await api(`/stores/${store2.id}/products`, {
  method: 'POST',
  token: owner.token,
  body: productBody('Café para validación'),
  expected: 201,
});

for (const price of [-1, 0, 100000.01, 2.123])
  await api(adminPath + '/products', {
    method: 'POST',
    token: owner.token,
    body: productBody(`Precio ${price}`, price),
    expected: 400,
  });

await api(adminPath + '/products', {
  method: 'POST',
  token: owner.token,
  body: { basePrice: 10 },
  expected: 400,
});

await api(adminPath + '/products/no-es-uuid', { token: owner.token, expected: 400 });

await api(adminPath + '/products', { token: a.token, expected: 403 });

const q = await product('Otro producto para editar');

await api(`${adminPath}/products/${q.id}`, {
  method: 'PUT',
  token: owner.token,
  body: productBody('CAFÉ PARA VALIDACIÓN'),
  expected: 409,
});

await api(`${adminPath}/products/${p.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'INACTIVE' },
  expected: 200,
});

await api(`/public/stores/${store.id}/products/${p.id}`, { expected: 404 });

await api(customerPath + '/cart/items', {
  method: 'POST',
  token: a.token,
  body: { productId: p.id, quantity: 1 },
  expected: 409,
});

await api(`${adminPath}/products/${p.id}/status`, {
  method: 'PATCH',
  token: owner.token,
  body: { status: 'ACTIVE' },
  expected: 200,
});

await addEarly();

async function addEarly() {
  await api(customerPath + '/cart/items', {
    method: 'POST',
    token: a.token,
    body: { productId: p.id, quantity: 1 },
    expected: 200,
  });

  await api(`${adminPath}/products/${p.id}/status`, {
    method: 'PATCH',
    token: owner.token,
    body: { status: 'INACTIVE' },
    expected: 200,
  });

  await api(customerPath + '/orders', {
    method: 'POST',
    token: a.token,
    body: { invoiceType: 'RECEIPT', documentType: 'DNI', documentNumber: '12345678' },
    expected: 409,
  });

  await api(`${customerPath}/cart/items/${p.id}`, {
    method: 'DELETE',
    token: a.token,
    expected: 200,
  });

  await api(`${adminPath}/products/${p.id}/status`, {
    method: 'PATCH',
    token: owner.token,
    body: { status: 'ACTIVE' },
    expected: 200,
  });
}

await api(`${adminPath}/products/${p.id}/inventory/quantity`, {
  method: 'PATCH',
  token: owner.token,
  body: { quantity: 1000001 },
  expected: 400,
});

await api(adminPath + '/products', {
  method: 'POST',
  token: owner.token,
  body: { ...productBody('Imagen inválida'), imageUrl: 'javascript:alert(1)' },
  expected: 400,
});

await api(adminPath + '/products', {
  method: 'POST',
  token: owner.token,
  body: productBody('Nulo\u0000no permitido'),
  expected: 400,
});

const categoryBody = { name: 'Categoría QA' };

await api(adminPath + '/categories', {
  method: 'POST',
  token: owner.token,
  body: categoryBody,
  expected: 201,
});

await api(adminPath + '/categories', {
  method: 'POST',
  token: owner.token,
  body: { name: ' CATEGORÍA   QA ' },
  expected: 409,
});

const categoryRace = await Promise.all(
  [1, 2].map(() =>
    raw(adminPath + '/categories', {
      method: 'POST',
      token: owner.token,
      body: { name: 'Categoría simultánea' },
    }),
  ),
);

assert.deepEqual(categoryRace.map((x) => x.status).sort(), [201, 409]);

const offerPath = `${adminPath}/products/${p.id}/offers`;

const dates = {
  startsAt: new Date(Date.now() - 60000).toISOString(),
  endsAt: new Date(Date.now() + 86400000).toISOString(),
};

for (const discountPercentage of [-1, 0, 100, 12.123])
  await api(offerPath, {
    method: 'POST',
    token: owner.token,
    body: { ...dates, discountPercentage },
    expected: 400,
  });

await api(offerPath, {
  method: 'POST',
  token: owner.token,
  body: { ...dates, offerPrice: 10 },
  expected: 400,
});

await api(offerPath, {
  method: 'POST',
  token: owner.token,
  body: { startsAt: dates.endsAt, endsAt: dates.startsAt, discountPercentage: 25 },
  expected: 400,
});

const offer = await api(offerPath, {
  method: 'POST',
  token: owner.token,
  body: { ...dates, discountPercentage: 25 },
  expected: 201,
});

assert.equal(offer.offerPrice, 15);
assert.equal(offer.discountPercentage, 25);

await api(`${adminPath}/products/${p.id}`, {
  method: 'PUT',
  token: owner.token,
  body: productBody('Café para validación', 40),
  expected: 200,
});

assert.equal((await api(offerPath, { token: owner.token, expected: 200 }))[0].offerPrice, 30);

const offerRace = await Promise.all(
  [1, 2].map(() =>
    raw(`${adminPath}/products/${q.id}/offers`, {
      method: 'POST',
      token: owner.token,
      body: { ...dates, discountPercentage: 10 },
    }),
  ),
);

assert.deepEqual(offerRace.map((x) => x.status).sort(), [201, 409]);

const fiscal = { invoiceType: 'RECEIPT', documentType: 'DNI', documentNumber: '12345678' };

const add = (user, prod, quantity = 1) =>
  api(customerPath + '/cart/items', {
    method: 'POST',
    token: user.token,
    body: { productId: prod.id, quantity },
    expected: 200,
  });

const checkout = (user, key = randomUUID()) =>
  raw(customerPath + '/orders', { method: 'POST', token: user.token, body: fiscal, key });

const scarce = await product('Última unidad', 1, 10);

await add(a, scarce);
await add(b, scarce);

const raceKeys = [randomUUID(), randomUUID()];

const stockRace = await Promise.all([checkout(a, raceKeys[0]), checkout(b, raceKeys[1])]);

assert.deepEqual(stockRace.map((x) => x.status).sort(), [201, 409]);

assert.equal(
  (await api(`${adminPath}/products/${scarce.id}/inventory`, { token: owner.token })).quantity,
  0,
);

const winnerIndex = stockRace.findIndex((x) => x.status === 201);
const winner = [a, b][winnerIndex];
const loser = [a, b][1 - winnerIndex];
const order = stockRace[winnerIndex].data;

const replay = await checkout(winner, raceKeys[winnerIndex]);
assert.equal(replay.status, 201);
assert.equal(replay.data.id, order.id);

await api(customerPath + '/orders', {
  method: 'POST',
  token: winner.token,
  key: raceKeys[winnerIndex],
  body: { ...fiscal, documentNumber: '87654321' },
  expected: 409,
});

await api(customerPath + '/orders', {
  method: 'POST',
  token: loser.token,
  key: null,
  body: fiscal,
  expected: 400,
});

await api(`/customer/orders/${order.id}`, { token: loser.token, expected: 404 });

const paymentPath = `${customerPath}/orders/${order.id}/payments`;
const payKey = randomUUID();
const beforePay = await stats();

const payRace = await Promise.all(
  [1, 2].map(() =>
    raw(paymentPath, {
      method: 'POST',
      token: winner.token,
      key: payKey,
      body: { sourceId: 'tkn_test_ok' },
      expected: [201, 409],
    }),
  ),
);

assert.ok(payRace.some((x) => x.status === 201));

const paid = await api(paymentPath, {
  method: 'POST',
  token: winner.token,
  key: payKey,
  body: { sourceId: 'tkn_test_ok' },
  expected: 201,
});

assert.equal((await stats()).charges, beforePay.charges + 1, 'Debe existir un solo cobro externo.');

await api(paymentPath, {
  method: 'POST',
  token: winner.token,
  body: { sourceId: 'tkn_test_ok' },
  expected: 409,
});

const cancelKey = randomUUID();
const beforeCancel = await stats();

const cancelRace = await Promise.all(
  [1, 2].map(() =>
    raw(`/customer/orders/${order.id}/cancel`, {
      method: 'POST',
      token: winner.token,
      key: cancelKey,
      expected: [200, 409],
    }),
  ),
);

assert.ok(cancelRace.some((x) => x.status === 200));

const cancelled = await api(`/customer/orders/${order.id}/cancel`, {
  method: 'POST',
  token: winner.token,
  key: cancelKey,
  expected: 200,
});

assert.equal(cancelled.order.status, 'CANCELLED');

assert.equal(
  (await stats()).refunds,
  beforeCancel.refunds + 1,
  'Debe solicitarse una sola devolución.',
);

assert.equal(
  (await api(`${adminPath}/products/${scarce.id}/inventory`, { token: owner.token })).quantity,
  1,
);

await api(`/customer/orders/${order.id}/cancel`, {
  method: 'POST',
  token: winner.token,
  expected: 409,
});

// Vaciar el carrito del perdedor antes de la siguiente compra.

await api(`${customerPath}/cart/items/${scarce.id}`, {
  method: 'DELETE',
  token: loser.token,
  expected: 200,
});

const plentiful = await product('Carrito concurrente', 100, 10);

await Promise.all(Array.from({ length: 5 }, () => add(a, plentiful)));

let cart = await api(customerPath + '/cart', { token: a.token });
assert.equal(cart.items.find((x) => x.productId === plentiful.id).quantity, 5);

await api(customerPath + '/cart/items', {
  method: 'POST',
  token: a.token,
  body: { productId: plentiful.id, quantity: 10000 },
  expected: 400,
});

const sameKey = randomUUID();
const checkoutRace = await Promise.all(Array.from({ length: 4 }, () => checkout(a, sameKey)));

assert.ok(checkoutRace.every((x) => [201, 409].includes(x.status)));
assert.ok(checkoutRace.some((x) => x.status === 201));

const order2 = await api(customerPath + '/orders', {
  method: 'POST',
  token: a.token,
  key: sameKey,
  body: fiscal,
  expected: 201,
});

assert.equal(
  (await api(`${adminPath}/products/${plentiful.id}/inventory`, { token: owner.token })).quantity,
  95,
);

const declinePath = `${customerPath}/orders/${order2.id}/payments`;
const declineKey = randomUUID();
const beforeDecline = await stats();

for (let i = 0; i < 2; i++)
  await api(declinePath, {
    method: 'POST',
    token: a.token,
    key: declineKey,
    body: { sourceId: 'tkn_test_declined' },
    expected: 402,
  });

assert.equal((await stats()).charges, beforeDecline.charges + 1);

const distinctPayBefore = await stats();
const distinctPay = await Promise.all([1, 2].map(() => raw(declinePath, { method: 'POST', token: a.token, body: { sourceId: 'tkn_test_ok' }, expected: [201, 409] })));
assert.deepEqual(distinctPay.map((item) => item.status).sort(), [201, 409]);
assert.equal((await stats()).charges, distinctPayBefore.charges + 1);
const distinctCancelBefore = await stats();
const distinctCancel = await Promise.all([1, 2].map(() => raw(`${adminPath}/orders/${order2.id}/status`, { method: 'PATCH', token: owner.token, body: { status: 'CANCELLED' }, expected: [200, 409] })));
assert.deepEqual(distinctCancel.map((item) => item.status).sort(), [200, 409]);
assert.equal((await stats()).refunds, distinctCancelBefore.refunds + 1);
// La pasarela confirma internamente un cargo pero devuelve 503: no se puede cobrar otra vez.

await add(b, plentiful);

const uncertainOrder = (await checkout(b)).data;
const uncertainPath = `${customerPath}/orders/${uncertainOrder.id}/payments`;
const uncertainKey = randomUUID();
const beforeUncertain = await stats();

await api(uncertainPath, {
  method: 'POST',
  token: b.token,
  key: uncertainKey,
  body: { sourceId: 'tkn_qa_uncertain' },
  expected: 502,
});

for (const key of [uncertainKey, randomUUID()]) {
  const blocked = await api(uncertainPath, {
    method: 'POST',
    token: b.token,
    key,
    body: { sourceId: 'tkn_qa_uncertain' },
    expected: 409,
  });

  assert.equal(blocked.code, 'OPERATION_UNCERTAIN');
}

assert.equal((await stats()).charges, beforeUncertain.charges + 1);

await api(`/customer/orders/${uncertainOrder.id}/cancel`, {
  method: 'POST',
  token: b.token,
  expected: 409,
});

assert.equal(
  (await api(`/customer/orders/${uncertainOrder.id}`, { token: b.token })).order.status,
  'PENDING',
);

console.log(
  JSON.stringify(
    {
      resultado: 'correcto',
      peticionesVerificadas: checks,
      casos: [
        'unicidad concurrente',
        'porcentajes',
        'INACTIVE',
        'última unidad',
        'carrito concurrente',
        'checkout repetido',
        'cobro y devolución únicos',
        'pago rechazado repetido',
        'resultado externo incierto',
        'roles y privacidad',
      ],
    },
    null,
    2,
  ),
);
