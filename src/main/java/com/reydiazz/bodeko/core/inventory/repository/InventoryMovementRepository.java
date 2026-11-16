package com.reydiazz.bodeko.core.inventory.repository;

import com.reydiazz.bodeko.core.inventory.model.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/** Consultas paginadas y filtrables para el historial de inventario. */
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, UUID>, JpaSpecificationExecutor<InventoryMovement> {
}
