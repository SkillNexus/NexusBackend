package com.app.userservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.dto.ApiResponse;
import com.app.userservice.dto.LearningObjectiveDTO;
import com.app.userservice.model.LearningObjective;
import com.app.userservice.model.UserProfile;
import com.app.userservice.service.UserService;

/**
 * Contrôleur REST pour les opérations liées aux objectifs d'apprentissage.
 * Utilise les DTOs pour la communication avec les clients.
 */
@RestController
@RequestMapping("/api/objectives")
public class LearningObjectiveController {

    private final UserService userService;

    @Autowired
    public LearningObjectiveController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les objectifs d'apprentissage d'un utilisateur.
     * 
     * @param userId Identifiant de l'utilisateur
     * @return Une liste des objectifs d'apprentissage convertis en DTOs
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<LearningObjectiveDTO>>> getUserObjectives(@PathVariable String userId) {
        Optional<UserProfile> userOpt = userService.getUserById(userId);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + userId));
        }
        
        UserProfile user = userOpt.get();
        List<LearningObjective> objectives = user.getLearningObjectives();
        
        if (objectives == null || objectives.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(List.of()));
        }
        
        List<LearningObjectiveDTO> objectiveDTOs = objectives.stream()
                .map(LearningObjectiveDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(objectiveDTOs));
    }

    /**
     * Ajoute un objectif d'apprentissage à un utilisateur.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param objectiveDTO DTO contenant les informations de l'objectif
     * @return Le DTO de l'utilisateur mis à jour avec ses objectifs
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<LearningObjectiveDTO>>> addObjective(
            @PathVariable String userId,
            @RequestBody LearningObjectiveDTO objectiveDTO) {
        try {
            Optional<UserProfile> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + userId));
            }
            
            // Conversion du DTO vers le modèle
            LearningObjective objective = objectiveDTO.toModel();
            
            // Récupération et mise à jour de l'utilisateur
            UserProfile user = userOpt.get();
            List<LearningObjective> objectives = user.getLearningObjectives();
            
            if (objectives == null) {
                objectives = List.of(objective);
            } else {
                objectives.add(objective);
            }
            
            user.setLearningObjectives(objectives);
            
            // Sauvegarde de l'utilisateur mis à jour
            UserProfile updatedUser = userService.updateUser(userId, user);
            
            // Conversion des objectifs en DTOs pour la réponse
            List<LearningObjectiveDTO> objectiveDTOs = updatedUser.getLearningObjectives().stream()
                    .map(LearningObjectiveDTO::fromModel)
                    .collect(Collectors.toList());
                    
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(objectiveDTOs));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Met à jour un objectif d'apprentissage d'un utilisateur.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param objectiveId Identifiant de l'objectif
     * @param objectiveDTO DTO contenant les nouvelles informations
     * @return Le DTO de l'objectif mis à jour
     */
    @PutMapping("/user/{userId}/objective/{objectiveId}")
    public ResponseEntity<ApiResponse<LearningObjectiveDTO>> updateObjective(
            @PathVariable String userId,
            @PathVariable String objectiveId,
            @RequestBody LearningObjectiveDTO objectiveDTO) {
        try {
            Optional<UserProfile> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + userId));
            }
            
            UserProfile user = userOpt.get();
            List<LearningObjective> objectives = user.getLearningObjectives();
            
            if (objectives == null || objectives.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Aucun objectif trouvé pour cet utilisateur"));
            }
            
            // Recherche de l'objectif à mettre à jour
            Optional<LearningObjective> objectiveOpt = objectives.stream()
                    .filter(obj -> obj.getId().equals(objectiveId))
                    .findFirst();
                    
            if (objectiveOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Objectif non trouvé avec l'ID: " + objectiveId));
            }
            
            // Mise à jour de l'objectif
            LearningObjective objective = objectiveOpt.get();
            objective.setTitle(objectiveDTO.getTitle());
            objective.setDescription(objectiveDTO.getDescription());
            objective.setProgressPercentage(objectiveDTO.getProgressPercentage());
            
            // Sauvegarde de l'utilisateur mis à jour
            userService.updateUser(userId, user);
            
            // Conversion de l'objectif en DTO pour la réponse
            LearningObjectiveDTO updatedObjectiveDTO = LearningObjectiveDTO.fromModel(objective);
            
            return ResponseEntity.ok(ApiResponse.success(updatedObjectiveDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime un objectif d'apprentissage d'un utilisateur.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param objectiveId Identifiant de l'objectif
     * @return Une réponse indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/user/{userId}/objective/{objectiveId}")
    public ResponseEntity<ApiResponse<Void>> deleteObjective(
            @PathVariable String userId,
            @PathVariable String objectiveId) {
        try {
            Optional<UserProfile> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + userId));
            }
            
            UserProfile user = userOpt.get();
            List<LearningObjective> objectives = user.getLearningObjectives();
            
            if (objectives == null || objectives.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Aucun objectif trouvé pour cet utilisateur"));
            }
            
            // Filtrage des objectifs pour exclure celui à supprimer
            boolean objectiveExists = objectives.removeIf(obj -> obj.getId().equals(objectiveId));
            
            if (!objectiveExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Objectif non trouvé avec l'ID: " + objectiveId));
            }
            
            // Sauvegarde de l'utilisateur mis à jour
            userService.updateUser(userId, user);
            
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
} 