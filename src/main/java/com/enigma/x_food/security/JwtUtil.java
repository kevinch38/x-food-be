package com.enigma.x_food.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin.AdminService;
import com.enigma.x_food.feature.admin.dto.response.AdminResponse;
import com.enigma.x_food.feature.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${app.x-food.jwt-secret}")
    private String jwtSecret;
    @Value("${app.x-food.app-name}")
    private String appName;
    @Value("${app.x-food.jwtExpirationInSecond}")
    private long jwtExpirationInSecond;
    private final AdminService adminService;

    public String generateTokenUser(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(user.getAccountID())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", ERole.ROLE_USER.name())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("error while creating jwt token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String generateTokenAdmin(Admin admin) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(admin.getAdminID())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSecond))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", admin.getRole().getRole().name())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("error while creating jwt token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTVerificationException e) {
            log.error("invalid verification JWT: {}", e.getMessage());
            return false;
        }
    }

    public Boolean verifyJwtTokenAdmin(String token) {
        SecurityContext authentication = SecurityContextHolder.getContext();;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            AdminResponse admin = adminService.findById(decodedJWT.getSubject());
            Admin adminTemp;
            try{
                adminTemp = (Admin) authentication.getAuthentication().getPrincipal();
                log.info(String.valueOf(adminTemp));
            }
            catch (Exception e){
                authentication.setAuthentication(null);
                SecurityContextHolder.clearContext();
                return false;
            }
            for (ERole role : ERole.values()) {
                if (role.name().equals(admin.getRole())) {
                    return true;
                }
            }
            authentication.setAuthentication(null);
            SecurityContextHolder.clearContext();
            return false;
        } catch (JWTVerificationException e) {
            log.error("invalid verification JWT: {}", e.getMessage());
            authentication.setAuthentication(null);
            SecurityContextHolder.clearContext();
            return false;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Token");
        }
    }

    public JwtClaim getAdminInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return JwtClaim.builder()
                    .adminId(decodedJWT.getSubject())
                    .role(decodedJWT.getClaim("role").asString())
                    .build();
        } catch (JWTVerificationException e) {
            log.error("invalid verification JWT: {}", e.getMessage());
            return null;
        }
    }
}
