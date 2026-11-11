package com.reydiazz.bodeko.shared.validation;

import java.util.Locale;

/** Limpia espacios para comparar nombres de forma consistente con PostgreSQL. */
public final class NormalizedText {
    private NormalizedText() {}
    public static String clean(String value) { return value.trim().replaceAll("\\s+", " "); }
    public static String key(String value) { return clean(value).toLowerCase(Locale.ROOT); }
}
