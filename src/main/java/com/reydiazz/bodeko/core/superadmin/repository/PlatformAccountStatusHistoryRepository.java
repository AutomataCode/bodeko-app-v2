package com.reydiazz.bodeko.core.superadmin.repository;

import com.reydiazz.bodeko.core.superadmin.model.PlatformAccountStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/** Guarda la auditoría de cambios de estado de cuentas. */
public interface PlatformAccountStatusHistoryRepository extends JpaRepository<PlatformAccountStatusHistory, UUID> {}
