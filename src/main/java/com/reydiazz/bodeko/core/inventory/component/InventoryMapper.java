package com.reydiazz.bodeko.core.inventory.component;

import com.reydiazz.bodeko.core.inventory.model.entity.Inventory;
import com.reydiazz.bodeko.core.inventory.web.response.InventoryResponse;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getProduct().getId(),
                inventory.getQuantity(),
                inventory.getLowStockThreshold(),
                inventory.isLowStock(),
                inventory.getUpdatedAt()
        );
    }

}
