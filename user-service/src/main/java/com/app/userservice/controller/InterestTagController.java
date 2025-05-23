package com.app.userservice.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.dto.ApiResponse;
import com.app.userservice.dto.InterestTagDTO;
import com.app.userservice.model.InterestTag;
import com.app.userservice.service.InterestService;

/**
 * Contrôleur REST pour les opérations liées aux intérêts.
 * Utilise les DTOs pour la communication avec les clients.
 */
@RestController
@RequestMapping("/api/interests")
public class InterestTagController {

    private final InterestService interestService;

    @Autowired
    public InterestTagController(InterestService interestService) {
        this.interestService = interestService;
    }

    /**
     * Crée un nouvel intérêt.
     * 
     * @param interestTagDTO DTO contenant les informations de l'intérêt
     * @return Une réponse contenant le DTO de l'intérêt créé
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InterestTagDTO>> createInterest(@RequestBody InterestTagDTO interestTagDTO) {
        try {
            // Conversion du DTO vers le modèle
            InterestTag interestTag = interestTagDTO.toModel();
            
            // Appel du service métier
            InterestTag createdInterest = interestService.createInterest(interestTag);
            
            // Conversion du modèle vers le DTO pour la réponse
            InterestTagDTO createdInterestDTO = InterestTagDTO.fromModel(createdInterest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdInterestDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Récupère tous les intérêts.
     * 
     * @return Une liste de tous les intérêts convertis en DTOs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InterestTagDTO>>> getAllInterests() {
        // Récupération de tous les intérêts
        List<InterestTag> interests = interestService.getAllInterests();
        
        // Conversion de chaque intérêt en DTO
        List<InterestTagDTO> interestDTOs = interests.stream()
                .map(InterestTagDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(interestDTOs));
    }

    /**
     * Récupère un intérêt par son identifiant.
     * 
     * @param id Identifiant de l'intérêt
     * @return Le DTO de l'intérêt trouvé, ou une erreur s'il n'existe pas
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InterestTagDTO>> getInterestById(@PathVariable String id) {
        return interestService.getInterestById(id)
                .map(interest -> {
                    // Conversion de l'intérêt en DTO
                    InterestTagDTO interestDTO = InterestTagDTO.fromModel(interest);
                    return ResponseEntity.ok(ApiResponse.success(interestDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Intérêt non trouvé avec l'ID: " + id)));
    }

    /**
     * Recherche des intérêts par nom partiel.
     * 
     * @param name Nom partiel de l'intérêt
     * @return Une liste des intérêts correspondants convertis en DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<InterestTagDTO>>> searchInterests(@RequestParam String name) {
        List<InterestTag> interests = interestService.findInterestsByPartialName(name);
        
        List<InterestTagDTO> interestDTOs = interests.stream()
                .map(InterestTagDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(interestDTOs));
    }

    /**
     * Met à jour un intérêt existant.
     * 
     * @param id Identifiant de l'intérêt à mettre à jour
     * @param interestTagDTO DTO contenant les nouvelles informations
     * @return Le DTO de l'intérêt mis à jour, ou une erreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InterestTagDTO>> updateInterest(
            @PathVariable String id, 
            @RequestBody InterestTagDTO interestTagDTO) {
        try {
            // Conversion du DTO vers le modèle
            InterestTag interestTag = interestTagDTO.toModel();
            
            // Appel du service métier
            InterestTag updatedInterest = interestService.updateInterest(id, interestTag);
            
            // Conversion du modèle vers le DTO pour la réponse
            InterestTagDTO updatedInterestDTO = InterestTagDTO.fromModel(updatedInterest);
            
            return ResponseEntity.ok(ApiResponse.success(updatedInterestDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime un intérêt.
     * 
     * @param id Identifiant de l'intérêt à supprimer
     * @return Une réponse indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInterest(@PathVariable String id) {
        try {
            interestService.deleteInterest(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
} 