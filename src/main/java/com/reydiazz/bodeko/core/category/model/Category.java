package com.reydiazz.bodeko.core.category.model;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_categories_store_name",
                        columnNames = {"store_id", "name"}
                ),
                @UniqueConstraint(
                        name = "uq_categories_id_store",
                        columnNames = {"id", "store_id"}
                )
        }
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "name",nullable = false)
    private String name;

    public void update(String name){
        this.name = name;
    }

}
