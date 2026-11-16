CREATE TABLE billing_document_sequences (
    issuer_id UUID NOT NULL REFERENCES billing_issuers(id) ON DELETE RESTRICT,
    invoice_type VARCHAR(20) NOT NULL CHECK (invoice_type IN ('RECEIPT', 'INVOICE')),
    next_number BIGINT NOT NULL CHECK (next_number > 0),
    PRIMARY KEY (issuer_id, invoice_type)
);

CREATE TABLE sales_documents (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL UNIQUE REFERENCES orders(id) ON DELETE RESTRICT,
    store_id UUID NOT NULL REFERENCES stores(id) ON DELETE RESTRICT,
    billing_issuer_id UUID REFERENCES billing_issuers(id) ON DELETE RESTRICT,
    invoice_type VARCHAR(20) NOT NULL CHECK (invoice_type IN ('RECEIPT', 'INVOICE')),
    status VARCHAR(32) NOT NULL CHECK (status IN ('ISSUED', 'PENDING_CONFIGURATION', 'VOIDED')),
    series VARCHAR(4),
    document_number BIGINT,
    issuer_tax_id CHAR(11),
    issuer_legal_name VARCHAR(200),
    issuer_fiscal_address VARCHAR(300),
    issued_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_sales_documents_issued_fields CHECK (
        (status = 'PENDING_CONFIGURATION' AND series IS NULL AND document_number IS NULL)
        OR (status = 'ISSUED' AND series IS NOT NULL AND document_number > 0
            AND issuer_tax_id IS NOT NULL AND issuer_legal_name IS NOT NULL AND issuer_fiscal_address IS NOT NULL)
        OR status = 'VOIDED'
    )
);

CREATE UNIQUE INDEX uq_sales_documents_issuer_number
    ON sales_documents (billing_issuer_id, invoice_type, document_number)
    WHERE status = 'ISSUED';
CREATE INDEX idx_sales_documents_store_created
    ON sales_documents (store_id, created_at DESC);
