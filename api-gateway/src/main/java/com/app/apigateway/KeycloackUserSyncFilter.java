package com.app.apigateway;


import com.app.apigateway.service.RegisterRequest;
import com.app.apigateway.service.UserServiceClient;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;


@Component
@Slf4j
@AllArgsConstructor
public class KeycloackUserSyncFilter implements WebFilter  {
    //lis les infos JWT du token Keycloak et les synchronise avec le User-service et permet de les écrire son UserProfileDTO
       // Lis les infos du token et met dans UserProfileDTO

    private final UserServiceClient userServiceClient;



    public Mono<Void> filter (ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        return userServiceClient.createUser(getUserDetails(token)).then(Mono.defer(() -> {
            log.info("User details synchronized with User Service");
            return chain.filter(exchange);
        }).onErrorResume(e -> {
            log.error("Error synchronizing user details: {}", e.getMessage());
            return chain.filter(exchange);
        }));


    }

    private RegisterRequest getUserDetails(String token) {

        try {
            // Parse the JWT token
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeycloakId(claims.getStringClaim("sub"));
            registerRequest.setUsername(claims.getStringClaim("preferred_username"));

            return registerRequest;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }




}
