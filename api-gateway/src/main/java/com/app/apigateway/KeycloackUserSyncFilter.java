package com.app.apigateway;


import java.text.ParseException;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.app.apigateway.service.RegisterRequest;
import com.app.apigateway.service.UserServiceClient;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@AllArgsConstructor
public class KeycloackUserSyncFilter implements WebFilter  {
    //lis les infos JWT du token Keycloak et les synchronise avec le User-service et permet de les écrire son UserProfileDTO
       // Lis les infos du token et met dans UserProfileDTO

    private final UserServiceClient userServiceClient;




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            // Pas de token, on continue la chaîne
            return chain.filter(exchange);
        }

        RegisterRequest userDetails = getUserDetails(token);

        return userServiceClient.getUserByEmail(userDetails.getEmail())
                .flatMap(response -> {
                    // L'utilisateur existe déjà, on continue la chaîne
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> {
                    if (isNotFoundException(e)) {
                        log.info("Utilisateur non trouvé, création en cours pour l'email: {}", userDetails.getEmail());
                        return userServiceClient.createUser(userDetails)
                                .then(chain.filter(exchange));
                    } else {
                        log.error("Erreur inattendue lors de la vérification de l'utilisateur: {}", e.getMessage());
                        return chain.filter(exchange);
                    }
                });
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

    private boolean isNotFoundException(Throwable e) {
        return e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
    }
}




