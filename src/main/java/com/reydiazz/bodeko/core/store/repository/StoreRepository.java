package com.reydiazz.bodeko.core.store.repository;

import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.catalog.web.response.TopSellingStoreResponse;
import com.reydiazz.bodeko.core.order.core.model.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    /** Cuenta cada orden vendida una vez. Los empates se ordenan por nombre e identificador. */
    @Query("""
            select new com.reydiazz.bodeko.core.catalog.web.response.TopSellingStoreResponse(
                s.id, s.name, s.subdomain, s.currency, s.taxRate, count(o.id))
            from Store s join Order o on o.store = s
            where o.status in :statuses
            group by s.id, s.name, s.subdomain, s.currency, s.taxRate
            order by count(o.id) desc, lower(s.name) asc, s.id asc
            """)
    List<TopSellingStoreResponse> findTopSelling(@Param("statuses") List<OrderStatus> statuses, Pageable pageable);

    java.util.Optional<Store> findBySubdomainIgnoreCase(String subdomain);

    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);

    boolean existsByTaxId(String taxId);

    boolean existsBySubdomain(String subdomain);

    List<Store> findAllByOwnerId(UUID ownerId);

}
