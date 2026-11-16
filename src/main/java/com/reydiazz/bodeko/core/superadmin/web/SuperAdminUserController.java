package com.reydiazz.bodeko.core.superadmin.web;

import com.reydiazz.bodeko.core.superadmin.service.PlatformUserManagementService;
import com.reydiazz.bodeko.core.superadmin.web.request.ChangeAccountStatusRequest;
import com.reydiazz.bodeko.core.superadmin.web.response.*;
import com.reydiazz.bodeko.shared.response.PageResponse;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Rutas privadas para el panel de gestión de la plataforma. */
@RestController @RequestMapping("/api/super-admin") @RequiredArgsConstructor
public class SuperAdminUserController {
    private final PlatformUserManagementService service;

    @GetMapping("/summary")
    public PlatformUserSummaryResponse summary() { return service.summary(); }

    @GetMapping("/stores")
    public List<PlatformStoreOptionResponse> stores() { return service.stores(); }

    @GetMapping("/vendors")
    public PageResponse<ManagedAccountResponse> vendors(
            @RequestParam(required = false) String search, @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) UUID storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        validateDates(from, to);
        return service.vendors(search, active, storeId, from, to, page, size);
    }

    @GetMapping("/customers")
    public PageResponse<ManagedAccountResponse> customers(
            @RequestParam(required = false) String search, @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) UUID storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        validateDates(from, to);
        return service.customers(search, active, storeId, from, to, page, size);
    }

    @PatchMapping("/vendors/{id}/status")
    public ManagedAccountResponse changeVendorStatus(@PathVariable UUID id, @Valid @RequestBody ChangeAccountStatusRequest request,
                                                      @AuthenticationPrincipal UserPrincipal actor) {
        return service.changeVendorStatus(id, request.active(), request.reason(), actor.user().getId());
    }

    @PatchMapping("/customers/{id}/status")
    public ManagedAccountResponse changeCustomerStatus(@PathVariable UUID id, @Valid @RequestBody ChangeAccountStatusRequest request,
                                                        @AuthenticationPrincipal UserPrincipal actor) {
        return service.changeCustomerStatus(id, request.active(), request.reason(), actor.user().getId());
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to))
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "La fecha inicial no puede ser posterior a la fecha final.");
    }
}
