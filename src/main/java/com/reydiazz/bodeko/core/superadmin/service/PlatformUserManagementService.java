package com.reydiazz.bodeko.core.superadmin.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.customer.repository.CustomerRepository;
import com.reydiazz.bodeko.core.store.model.entity.Store;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import com.reydiazz.bodeko.core.user.core.repository.UserRepository;
import com.reydiazz.bodeko.core.superadmin.model.PlatformAccountStatusHistory;
import com.reydiazz.bodeko.core.superadmin.model.PlatformAccountType;
import com.reydiazz.bodeko.core.superadmin.repository.PlatformAccountStatusHistoryRepository;
import com.reydiazz.bodeko.core.superadmin.spec.PlatformAccountSpecifications;
import com.reydiazz.bodeko.core.superadmin.web.response.*;
import com.reydiazz.bodeko.shared.response.PageResponse;
import com.reydiazz.bodeko.core.superadmin.exception.PlatformAccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/** Consulta y administra las cuentas globales y las cuentas registradas en tiendas. */
@Service @RequiredArgsConstructor
public class PlatformUserManagementService {
    private static final int MAX_PAGE_SIZE = 100;
    private final UserRepository users;
    private final CustomerRepository customers;
    private final StoreRepository stores;
    private final PlatformAccountStatusHistoryRepository history;

    @Transactional(readOnly = true)
    public PlatformUserSummaryResponse summary() {
        return new PlatformUserSummaryResponse(users.countByRole(UserRole.ADMIN),
                users.countByRoleAndIsActive(UserRole.ADMIN, true), customers.count(), customers.countByIsActive(true));
    }

    @Transactional(readOnly = true)
    public PageResponse<ManagedAccountResponse> vendors(String search, Boolean active, UUID storeId,
                                                        LocalDate from, LocalDate to, int page, int size) {
        OffsetDateTime start = from == null ? null : from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = to == null ? null : to.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        Page<User> result = users.findAll(PlatformAccountSpecifications.vendors(search, active, storeId, start, end), pageable(page, size));
        Set<UUID> ids = result.getContent().stream().map(User::getId).collect(Collectors.toSet());
        Map<UUID, List<ManagedStoreResponse>> ownedStores = stores.findAllByOwnerIdIn(ids).stream()
                .collect(Collectors.groupingBy(store -> store.getOwner().getId(), Collectors.mapping(this::storeResponse, Collectors.toList())));
        return page(result, result.getContent().stream().map(user -> new ManagedAccountResponse(user.getId(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getRole().name(), user.isActive(), user.getCreatedAt(),
                ownedStores.getOrDefault(user.getId(), List.of()))).toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<ManagedAccountResponse> customers(String search, Boolean active, UUID storeId,
                                                          LocalDate from, LocalDate to, int page, int size) {
        OffsetDateTime start = from == null ? null : from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = to == null ? null : to.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        Page<Customer> result = customers.findAll(PlatformAccountSpecifications.customers(search, active, storeId, start, end), pageable(page, size));
        List<ManagedAccountResponse> content = result.getContent().stream().map(customer -> new ManagedAccountResponse(
                customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), "CUSTOMER",
                customer.isActive(), customer.getCreatedAt(), List.of(storeResponse(customer.getStore())))).toList();
        return page(result, content);
    }

    @Transactional(readOnly = true)
    public List<PlatformStoreOptionResponse> stores() {
        return stores.findAll(Sort.by(Sort.Order.asc("name"), Sort.Order.asc("id"))).stream()
                .map(store -> new PlatformStoreOptionResponse(store.getId(), store.getName(), store.getSubdomain())).toList();
    }

    @Transactional
    public ManagedAccountResponse changeVendorStatus(UUID id, boolean active, String reason, UUID actorId) {
        User vendor = users.findByIdAndRoleForUpdate(id, UserRole.ADMIN).orElseThrow(PlatformAccountNotFoundException::new);
        if (vendor.isActive() != active) {
            boolean previous = vendor.isActive();
            vendor.changeActive(active);
            users.flush();
            history.save(history(PlatformAccountType.VENDOR, id, previous, active, reason, actorId));
        }
        List<ManagedStoreResponse> vendorStores = stores.findAllByOwnerIdIn(List.of(id)).stream().map(this::storeResponse).toList();
        return new ManagedAccountResponse(vendor.getId(), vendor.getFullName(), vendor.getEmail(), vendor.getPhoneNumber(),
                vendor.getRole().name(), vendor.isActive(), vendor.getCreatedAt(), vendorStores);
    }

    @Transactional
    public ManagedAccountResponse changeCustomerStatus(UUID id, boolean active, String reason, UUID actorId) {
        Customer customer = customers.findByIdForUpdate(id).orElseThrow(PlatformAccountNotFoundException::new);
        if (customer.isActive() != active) {
            boolean previous = customer.isActive();
            customer.changeActive(active);
            customers.flush();
            history.save(history(PlatformAccountType.CUSTOMER, id, previous, active, reason, actorId));
        }
        return new ManagedAccountResponse(customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(),
                "CUSTOMER", customer.isActive(), customer.getCreatedAt(), List.of(storeResponse(customer.getStore())));
    }

    private PlatformAccountStatusHistory history(PlatformAccountType type, UUID accountId, boolean previous,
                                                  boolean active, String reason, UUID actorId) {
        return PlatformAccountStatusHistory.builder().id(UuidCreator.getTimeOrderedEpoch())
                .actor(users.getReferenceById(actorId)).accountType(type).accountId(accountId)
                .previousActive(previous).newActive(active).reason(reason.trim()).createdAt(OffsetDateTime.now()).build();
    }

    private ManagedStoreResponse storeResponse(Store store) {
        return new ManagedStoreResponse(store.getId(), store.getName(), store.getSubdomain());
    }

    private PageRequest pageable(int page, int size) {
        return PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), MAX_PAGE_SIZE),
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
    }

    private <T> PageResponse<ManagedAccountResponse> page(Page<T> source, List<ManagedAccountResponse> content) {
        return new PageResponse<>(content, source.getNumber(), source.getSize(), source.getTotalElements(), source.getTotalPages());
    }
}
