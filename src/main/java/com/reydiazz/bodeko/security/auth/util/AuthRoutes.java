package com.reydiazz.bodeko.security.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthRoutes {

    public final String API_BASE = "/api/auth";
    public final String REGISTER_OWNER = "/register-owner";
    public final String LOGIN = "/login";
    public final String ME = "/me";

    /** Acceso de clientes, separado por tienda. */
    public final String STORE_LOGIN = "/stores/{storeId}/login";
    public final String STORE_REGISTER = "/stores/{storeId}/register";
    public final String STORE_LOGIN_PATTERN = API_BASE + "/stores/*/login";
    public final String STORE_REGISTER_PATTERN = API_BASE + "/stores/*/register";

}
