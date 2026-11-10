/** Prueba el ranking con datos sintéticos. Usar solo con una base vacía y desechable. */
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';

if (!process.argv.includes('--disposable'))
  throw new Error('Añade --disposable solo para una base de pruebas vacía.');
const database = new URL(process.env.QA_DATABASE_URL || '');
const api = new URL(process.env.QA_API_URL || '');
if (
  ![database, api].every((url) => ['127.0.0.1', 'localhost'].includes(url.hostname)) ||
  !/(qa|test)/i.test(database.pathname) ||
  database.password
) {
  throw new Error(
    'Usa servidores locales y una base cuyo nombre contenga qa o test. La contraseña se configura con PGPASSWORD.',
  );
}
const psql = process.env.QA_PSQL || 'psql';
function sql(statement) {
  return execFileSync(psql, [database.href, '-X', '-v', 'ON_ERROR_STOP=1', '-At'], {
    input: statement,
    encoding: 'utf8',
  }).trim();
}
async function ranking() {
  const response = await fetch(`${api.href.replace(/\/$/, '')}/public/stores/top-selling`);
  assert.equal(response.status, 200, 'El ranking debe ser público.');
  return response.json();
}
assert.equal(
  sql('SELECT count(*) FROM stores;'),
  '0',
  'La base debe estar vacía antes de crear datos.',
);
assert.deepEqual(await ranking(), []);
sql(`
BEGIN;
INSERT INTO users(id,email,password_hash,full_name,role) VALUES
 ('10000000-0000-0000-0000-000000000001','ranking.owner@example.test','sin-acceso','Propietario QA','ADMIN'),
 ('10000000-0000-0000-0000-000000000002','ranking.customer@example.test','sin-acceso','Cliente QA','CUSTOMER');
INSERT INTO stores(id,owner_id,name,tax_id,legal_name,subdomain)
SELECT ('20000000-0000-0000-0000-' || lpad(n::text,12,'0'))::uuid,
 '10000000-0000-0000-0000-000000000001', name, '20' || lpad(n::text,9,'0'), name, slug
FROM (VALUES (1,'Bodeko Market','bodeko-market'),(2,'Casa y Diseño','casa-diseno'),(3,'Tecnología Bodeko','tecnologia-bodeko'),(4,'Taller Local','taller-local'),(5,'Sin ventas confirmadas','sin-ventas')) s(n,name,slug);
INSERT INTO orders(id,store_id,customer_id,order_number,customer_document_type,customer_document_number,subtotal,tax_amount,total,applied_tax_rate,status)
SELECT gen_random_uuid(), ('20000000-0000-0000-0000-' || lpad(store_number::text,12,'0'))::uuid,
 '10000000-0000-0000-0000-000000000002', 'RANK-' || store_number || '-' || n, 'DNI','12345678',
 CASE WHEN store_number=4 THEN 10000 ELSE 10 END, 0, CASE WHEN store_number=4 THEN 10000 ELSE 10 END, 0,
 CASE WHEN store_number=5 THEN CASE WHEN n%2=0 THEN 'PENDING' ELSE 'CANCELLED' END
      WHEN n%3=0 THEN 'DELIVERED' WHEN n%3=1 THEN 'PAID' ELSE 'SHIPPED' END
FROM (VALUES(1,8),(2,5),(3,5),(4,2),(5,100)) s(store_number, quantity)
CROSS JOIN LATERAL generate_series(1,quantity) n;
COMMIT;
`);
let stores = await ranking();
assert.deepEqual(
  stores.map((store) => [store.subdomain, store.salesCount]),
  [
    ['bodeko-market', 8],
    ['casa-diseno', 5],
    ['tecnologia-bodeko', 5],
  ],
);
assert.equal(stores.length, 3, 'El resultado debe estar limitado a tres tiendas.');
assert.ok(
  stores.every((store) => !('taxId' in store) && !('ownerId' in store) && !('legalName' in store)),
);
sql("UPDATE orders SET status='CANCELLED' WHERE store_id='20000000-0000-0000-0000-000000000001';");
stores = await ranking();
assert.deepEqual(
  stores.map((store) => store.subdomain),
  ['casa-diseno', 'tecnologia-bodeko', 'taller-local'],
  'Las cancelaciones deben actualizar el ranking.',
);
sql("UPDATE orders SET status='PAID' WHERE store_id='20000000-0000-0000-0000-000000000001';");
assert.equal((await ranking())[0].salesCount, 8);
console.log(
  'Ranking verificado: acceso público, vacío, estados válidos, límite, desempates, cantidad frente a monto, privacidad y cancelaciones.',
);
