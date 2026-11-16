package com.reydiazz.bodeko.core.superadmin.spec;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Filtros compartidos para buscar vendedores y clientes de la plataforma. */
public final class PlatformAccountSpecifications {
    private PlatformAccountSpecifications() {}

    public static Specification<User> vendors(String search, Boolean active, UUID storeId,
                                               OffsetDateTime from, OffsetDateTime until) {
        return (root, query, builder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(builder.equal(root.get("role"), UserRole.ADMIN));
            addCommon(predicates, root, builder, search, active, from, until);
            if (storeId != null) {
                Subquery<Integer> ownedStore = query.subquery(Integer.class);
                Root<Store> store = ownedStore.from(Store.class);
                ownedStore.select(builder.literal(1)).where(
                        builder.equal(store.get("owner").get("id"), root.get("id")),
                        builder.equal(store.get("id"), storeId));
                predicates.add(builder.exists(ownedStore));
            }
            return builder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    public static Specification<Customer> customers(String search, Boolean active, UUID storeId,
                                                     OffsetDateTime from, OffsetDateTime until) {
        return (root, query, builder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            addCommon(predicates, root, builder, search, active, from, until);
            if (storeId != null) predicates.add(builder.equal(root.get("store").get("id"), storeId));
            return builder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    private static <T> void addCommon(java.util.List<jakarta.persistence.criteria.Predicate> predicates,
                                      Root<T> root, jakarta.persistence.criteria.CriteriaBuilder builder,
                                      String search, Boolean active, OffsetDateTime from, OffsetDateTime until) {
        if (search != null && !search.isBlank()) {
            String value = "%" + search.trim().toLowerCase(java.util.Locale.ROOT)
                    .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get("fullName")), value, '\\'),
                    builder.like(builder.lower(root.get("email")), value, '\\')));
        }
        if (active != null) predicates.add(builder.equal(root.get("isActive"), active));
        if (from != null) predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), from));
        if (until != null) predicates.add(builder.lessThan(root.get("createdAt"), until));
    }
}
