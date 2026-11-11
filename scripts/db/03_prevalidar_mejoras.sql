-- Solo lectura. Ejecutar antes de desplegar V6 y resolver resultados con el equipo.
SELECT lower(btrim(email)) AS correo, count(*) FROM users GROUP BY 1 HAVING count(*)>1;
SELECT lower(btrim(subdomain)) AS subdominio, count(*) FROM stores GROUP BY 1 HAVING count(*)>1;
SELECT owner_id,lower(regexp_replace(btrim(name), '\s+', ' ', 'g')) AS nombre,count(*) FROM stores GROUP BY 1,2 HAVING count(*)>1;
SELECT store_id,lower(regexp_replace(btrim(name), '\s+', ' ', 'g')) AS nombre,count(*) FROM products GROUP BY 1,2 HAVING count(*)>1;
SELECT store_id,lower(regexp_replace(btrim(name), '\s+', ' ', 'g')) AS nombre,count(*) FROM categories GROUP BY 1,2 HAVING count(*)>1;
SELECT order_id,count(*) FROM payments WHERE status='APPROVED' GROUP BY 1 HAVING count(*)>1;
