package com.reydiazz.bodeko.core.store.component;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.web.response.StoreResponse;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreResponse toResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getTaxId(),
                store.getLegalName(),
                store.getSubdomain(),
                store.getCurrency(),
                store.getTaxRate(),
                store.getCreatedAt()
        );
    }

}
