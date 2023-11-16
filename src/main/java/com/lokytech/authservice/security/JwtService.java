package com.lokytech.authservice.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final long VALIDITY = 60 * 60 * 1000L;

    public String generateToken(String username) {
        return generateToken(username, new ArrayList<>());
    }

    public String generateToken(String username, List<String> roles){
        try {
            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("roles", roles)
                    .expirationTime(new Date(new Date().getTime() + VALIDITY))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            logger.error("Error while generating token", e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY);

            if (signedJWT.verify(verifier) && !isTokenExpired(signedJWT)) {
                return true;
            }
        } catch (ParseException | JOSEException e) {
            logger.error("Error while validating token", e);
        }
        return false;
    }

    private boolean isTokenExpired(SignedJWT signedJWT) {
        try {
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return new Date().after(expiration);
        } catch (ParseException e) {
            logger.error("Error while checking token expiration", e);
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsSet.getSubject();
        } catch (ParseException e) {
            return null;
        }
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            List<String> roles = (List<String>) claimsSet.getClaim("roles");

            if (roles != null) {
                return roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (ParseException e) {
            return Collections.emptyList();
        }
    }
}
