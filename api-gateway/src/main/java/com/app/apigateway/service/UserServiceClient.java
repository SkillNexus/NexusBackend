package com.app.apigateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;


@Service
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${user-service.base-url}") String userServiceBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(userServiceBaseUrl).build();
    }

    public Mono<String> createUser(RegisterRequest registerRequest) {
        return webClient.post()
                .uri("/api/users/create")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(String.class);


    }

    // Vérifier l'existence d'un utilisateur par ID
    public Mono<String> getUserById(String id) {
        return webClient.get()
                .uri("/api/users/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Vérifier l'existence d'un utilisateur par email
    public Mono<String> getUserByEmail(String email) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    // Vérifier l'existence d'un utilisateur par username
    public Mono<String> getUserByUsername(String username) {
        return webClient.get()
                .uri("/api/users/username/{username}", username)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Vérifier l'existence d'un utilisateur par keycloakId
    public Mono<String> getUserByKeycloakId(String keycloakId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/keycloak")
                        .queryParam("keycloakId", keycloakId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    // Récupérer le statut de complétion du profil
    public Mono<String> getProfileCompletionStatus(String userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/profile/completion")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    // Met à jour les informations personnelles d'un utilisateur
    public Mono<String> updatePersonalInfo(String userId, PersonalInfoDTO personalInfoDTO) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/profile/personal-info")
                        .queryParam("userId", userId)
                        .build())
                .bodyValue(personalInfoDTO)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Met à jour les centres d'intérêt d'un utilisateur
    public Mono<String> updateInterests(String userId, List<InterestTagDTO> interestDTOs) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/profile/interests")
                        .queryParam("userId", userId)
                        .build())
                .bodyValue(interestDTOs)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Met à jour les objectifs d'apprentissage d'un utilisateur
    public Mono<String> updateLearningObjectives(String userId, List<LearningObjectiveDTO> objectiveDTOs) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/profile/objectives")
                        .queryParam("userId", userId)
                        .build())
                .bodyValue(objectiveDTOs)
                .retrieve()
                .bodyToMono(String.class);
    }

}
