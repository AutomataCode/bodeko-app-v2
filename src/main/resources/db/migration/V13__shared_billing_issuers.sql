CREATE TABLE billing_issuers (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    tax_id CHAR(11) NOT NULL,
    legal_name VARCHAR(200) NOT NULL,
    fiscal_address VARCHAR(300),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_billing_issuer_owner_tax_id UNIQUE (owner_id, tax_id)
);

ALTER TABLE stores DROP CONSTRAINT IF EXISTS stores_tax_id_key;
ALTER TABLE stores ADD COLUMN billing_issuer_id UUID;

INSERT INTO billing_issuers (id, owner_id, tax_id, legal_name, created_at, updated_at)
SELECT md5(owner_id::text || ':' || tax_id)::uuid, owner_id, tax_id, legal_name, now(), now()
FROM (
    SELECT DISTINCT ON (owner_id, tax_id) owner_id, tax_id, legal_name
    FROM stores ORDER BY owner_id, tax_id, id
) existing_issuers;

UPDATE stores s
SET billing_issuer_id = i.id
FROM billing_issuers i
WHERE i.owner_id = s.owner_id AND i.tax_id = s.tax_id;

ALTER TABLE stores ADD CONSTRAINT fk_stores_billing_issuer
    FOREIGN KEY (billing_issuer_id) REFERENCES billing_issuers(id) ON DELETE RESTRICT;
CREATE INDEX idx_stores_billing_issuer ON stores (billing_issuer_id);
