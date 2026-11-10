-- HU-01: optional identity on registration, without changing existing user rows.
ALTER TABLE users ADD COLUMN document_type VARCHAR(20), ADD COLUMN document_number VARCHAR(20);
ALTER TABLE users ADD CONSTRAINT ck_users_document CHECK (
    (document_type IS NULL AND document_number IS NULL)
    OR (document_type IS NOT NULL AND document_number IS NOT NULL AND (
        (document_type = 'DNI' AND document_number ~ '^[0-9]{8}$')
        OR (document_type = 'RUC' AND document_number ~ '^[0-9]{11}$')
        OR (document_type IN ('CE', 'PASSPORT') AND document_number ~ '^[A-Za-z0-9]{6,20}$')
    ))
);
