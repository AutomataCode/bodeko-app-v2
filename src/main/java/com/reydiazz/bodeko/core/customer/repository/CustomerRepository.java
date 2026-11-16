package com.reydiazz.bodeko.core.customer.repository;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {

    @Query("select c from Customer c where c.store.id = :storeId and lower(trim(c.email)) = :email")
    Optional<Customer> findByStoreAndEmail(@Param("storeId") UUID storeId, @Param("email") String email);

    @Query("select count(c) > 0 from Customer c where c.store.id = :storeId and lower(trim(c.email)) = :email")
    boolean existsByStoreAndEmail(@Param("storeId") UUID storeId, @Param("email") String email);

    long count();
    long countByIsActive(boolean active);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Customer c where c.id = :id")
    Optional<Customer> findByIdForUpdate(@Param("id") UUID id);

}
