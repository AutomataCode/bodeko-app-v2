package com.reydiazz.bodeko;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.sql.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/** Usa exclusivamente una base vacía de QA para comprobar la actualización con ofertas antiguas. */
@EnabledIfEnvironmentVariable(named="BODEKO_UPGRADE_TEST", matches="true")
class MigrationUpgradeTests {
 @Test void preservesLegacyOffersAndAddsUniqueIndexes() throws Exception {
  String url=System.getenv("BODEKO_UPGRADE_DB");
  assertEquals("jdbc:postgresql://127.0.0.1:55433/bodeko_upgrade_qa",url,"No usar una base de trabajo");
  try (Connection connection=DriverManager.getConnection(url,"postgres","solo-qa");Statement sql=connection.createStatement()) {
   try(ResultSet tables=connection.getMetaData().getTables(null,"public","users",null)) { assertFalse(tables.next(),"La base de actualización debe estar vacía"); }
   Flyway.configure().dataSource(url,"postgres","solo-qa").target("5").load().migrate();
   sql.execute("""
    INSERT INTO users(id,email,password_hash,full_name,role) VALUES
      ('10000000-0000-0000-0000-000000000001','upgrade@example.test','sin-acceso','QA','ADMIN');
    INSERT INTO stores(id,owner_id,name,tax_id,legal_name,subdomain) VALUES
      ('20000000-0000-0000-0000-000000000001','10000000-0000-0000-0000-000000000001','Tienda QA','20999999888','QA','upgrade-qa');
    INSERT INTO products(id,store_id,name,base_price,status) VALUES
      ('30000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000001','Café Demo',100,'ACTIVE'),
      ('30000000-0000-0000-0000-000000000002','20000000-0000-0000-0000-000000000001','Oferta histórica extrema',100000,'ACTIVE');
    INSERT INTO product_offers(store_id,product_id,offer_price,starts_at,ends_at) VALUES
      ('20000000-0000-0000-0000-000000000001','30000000-0000-0000-0000-000000000001',75,now(),now()+interval '1 day'),
      ('20000000-0000-0000-0000-000000000001','30000000-0000-0000-0000-000000000002',0.01,now(),now()+interval '1 day');
   """);
   Flyway.configure().dataSource(url,"postgres","solo-qa").load().migrate();
   try(ResultSet rows=sql.executeQuery("select offer_price,discount_percentage from product_offers order by product_id")) {
    assertTrue(rows.next());assertEquals(new BigDecimal("75.00"),rows.getBigDecimal(1));assertEquals(new BigDecimal("25.00"),rows.getBigDecimal(2));
    assertTrue(rows.next());assertEquals(new BigDecimal("0.01"),rows.getBigDecimal(1));assertNull(rows.getBigDecimal(2));
   }
   SQLException duplicate=assertThrows(SQLException.class,()->sql.execute("insert into products(id,store_id,name,base_price) values('30000000-0000-0000-0000-000000000003','20000000-0000-0000-0000-000000000001',' CAFÉ   DEMO ',100)"));
   assertEquals("23505",duplicate.getSQLState());
  }
 }
}
