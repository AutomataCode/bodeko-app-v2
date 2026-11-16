package com.reydiazz.bodeko.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * El sujeto del token es el id de la cuenta. El claim "kind" indica en qué tabla buscarla:
 * OWNER (users) o CUSTOMER (customers, con el claim "store" de su tienda).
 */
@Service
public class JwtService {

    public static final String KIND_CLAIM = "kind";
    public static final String STORE_CLAIM = "store";
    public static final String KIND_OWNER = "OWNER";
    public static final String KIND_CUSTOMER = "CUSTOMER";

    private final SecretKey key;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-ms}")
            long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expiration;
    }

    public String generateOwnerToken(UUID userId) {
        return baseToken(userId).claim(KIND_CLAIM, KIND_OWNER).compact();
    }

    public String generateCustomerToken(UUID customerId, UUID storeId) {
        return baseToken(customerId)
                .claim(KIND_CLAIM, KIND_CUSTOMER)
                .claim(STORE_CLAIM, storeId.toString())
                .compact();
    }

    /** Valida firma y vencimiento; lanza ExpiredJwtException o JwtException si no es válido. */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private JwtBuilder baseToken(UUID subject) {
        return Jwts.builder()
                .subject(subject.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key);
    }

}
