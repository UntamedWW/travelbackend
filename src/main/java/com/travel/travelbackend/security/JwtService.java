package com.travel.travelbackend.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travelbackend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final TypeReference<Map<String, Object>> CLAIMS_TYPE = new TypeReference<>() {
    };

    private final String secret;
    private final long expirationSeconds;

    public JwtService(
            @Value("${app.jwt.secret:change-this-development-secret-change-before-prod}") String secret,
            @Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds
    ) {
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(User user) {
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", user.getEmail());
        payload.put("userId", user.getId());
        payload.put("exp", Instant.now().getEpochSecond() + expirationSeconds);

        String unsignedToken = encodeJson(header) + "." + encodeJson(payload);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public String extractEmail(String token) {
        return parseClaims(token).get("sub").toString();
    }

    public boolean isValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String unsignedToken = parts[0] + "." + parts[1];
            if (!MessageDigest.isEqual(sign(unsignedToken).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
                return false;
            }

            Object expiration = parseClaims(token).get("exp");
            long expiresAt = expiration instanceof Number number
                    ? number.longValue()
                    : Long.parseLong(expiration.toString());

            return expiresAt > Instant.now().getEpochSecond();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            byte[] json = Base64.getUrlDecoder().decode(parts[1]);
            return OBJECT_MAPPER.readValue(json, CLAIMS_TYPE);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(OBJECT_MAPPER.writeValueAsBytes(value));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not create token");
        }
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not sign token");
        }
    }
}
