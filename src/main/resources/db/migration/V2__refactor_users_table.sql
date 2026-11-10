ALTER TABLE users
DROP COLUMN IF EXISTS document_type,
    DROP COLUMN IF EXISTS document_number,
    DROP COLUMN IF EXISTS business_name,
    DROP COLUMN IF EXISTS billing_address;

ALTER TABLE users
    ADD COLUMN phone_number VARCHAR(20)