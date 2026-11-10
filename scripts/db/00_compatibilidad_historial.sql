-- Compatibilidad de la estructura antigua de order_status_history con este backend.
-- No elimina filas ni modifica estados. No cambia el historial de Flyway.
-- En registros antiguos no se conoce el actor: changed_by permanece NULL.
BEGIN;

DO $$
DECLARE
    mapping record;
    has_expected boolean;
    has_legacy boolean;
BEGIN
    IF to_regclass('public.order_status_history') IS NULL THEN
        RAISE EXCEPTION 'No existe order_status_history. Aplica primero las migraciones del backend.';
    END IF;

    FOR mapping IN SELECT * FROM (VALUES
        ('previous_status', 'from_status'),
        ('new_status', 'to_status'),
        ('changed_at', 'created_at')
    ) AS names(legacy_name, expected_name)
    LOOP
        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_schema = 'public' AND table_name = 'order_status_history'
                       AND column_name = mapping.expected_name) INTO has_expected;
        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_schema = 'public' AND table_name = 'order_status_history'
                       AND column_name = mapping.legacy_name) INTO has_legacy;

        IF has_expected AND has_legacy THEN
            RAISE EXCEPTION 'Existen ambas columnas % y %. Revisa sus datos antes de normalizar el historial.',
                mapping.legacy_name, mapping.expected_name;
        ELSIF NOT has_expected AND has_legacy THEN
            EXECUTE format('ALTER TABLE public.order_status_history RENAME COLUMN %I TO %I',
                mapping.legacy_name, mapping.expected_name);
        ELSIF NOT has_expected THEN
            RAISE EXCEPTION 'Falta la columna % (o su equivalente antiguo %). No se modifico el historial.',
                mapping.expected_name, mapping.legacy_name;
        END IF;
    END LOOP;

    -- No se asignan administradores inventados a los cambios históricos.
    ALTER TABLE public.order_status_history ADD COLUMN IF NOT EXISTS changed_by UUID;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint c
        JOIN pg_attribute a ON a.attrelid = c.conrelid AND a.attnum = ANY(c.conkey)
        WHERE c.conrelid = 'public.order_status_history'::regclass
          AND c.contype = 'f' AND a.attname = 'changed_by'
    ) THEN
        ALTER TABLE public.order_status_history
            ADD CONSTRAINT fk_order_status_history_user FOREIGN KEY (changed_by)
            REFERENCES public.users(id) ON DELETE RESTRICT;
    END IF;

    CREATE INDEX IF NOT EXISTS idx_order_status_history_order_created
        ON public.order_status_history (order_id, created_at);
END $$;

COMMIT;
