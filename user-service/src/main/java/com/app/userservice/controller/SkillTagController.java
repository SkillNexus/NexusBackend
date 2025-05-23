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
import com.app.userservice.dto.SkillTagDTO;
import com.app.userservice.model.SkillTag;
import com.app.userservice.service.SkillService;

/**
 * Contrôleur REST pour les opérations liées aux compétences.
 * Utilise les DTOs pour la communication avec les clients.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillTagController {

    private final SkillService skillService;

    @Autowired
    public SkillTagController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Crée une nouvelle compétence.
     * 
     * @param skillTagDTO DTO contenant les informations de la compétence
     * @return Une réponse contenant le DTO de la compétence créée
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SkillTagDTO>> createSkill(@RequestBody SkillTagDTO skillTagDTO) {
        try {
            // Conversion du DTO vers le modèle
            SkillTag skillTag = skillTagDTO.toModel();
            
            // Appel du service métier
            SkillTag createdSkill = skillService.createSkill(skillTag);
            
            // Conversion du modèle vers le DTO pour la réponse
            SkillTagDTO createdSkillDTO = SkillTagDTO.fromModel(createdSkill);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdSkillDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Récupère toutes les compétences.
     * 
     * @return Une liste de toutes les compétences converties en DTOs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillTagDTO>>> getAllSkills() {
        // Récupération de toutes les compétences
        List<SkillTag> skills = skillService.getAllSkills();
        
        // Conversion de chaque compétence en DTO
        List<SkillTagDTO> skillDTOs = skills.stream()
                .map(SkillTagDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(skillDTOs));
    }

    /**
     * Récupère une compétence par son identifiant.
     * 
     * @param id Identifiant de la compétence
     * @return Le DTO de la compétence trouvée, ou une erreur si elle n'existe pas
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillTagDTO>> getSkillById(@PathVariable String id) {
        return skillService.getSkillById(id)
                .map(skill -> {
                    // Conversion de la compétence en DTO
                    SkillTagDTO skillDTO = SkillTagDTO.fromModel(skill);
                    return ResponseEntity.ok(ApiResponse.success(skillDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Compétence non trouvée avec l'ID: " + id)));
    }

    /**
     * Recherche des compétences par nom partiel.
     * 
     * @param name Nom partiel de la compétence
     * @return Une liste des compétences correspondantes converties en DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SkillTagDTO>>> searchSkills(@RequestParam String name) {
        List<SkillTag> skills = skillService.findSkillsByPartialName(name);
        
        List<SkillTagDTO> skillDTOs = skills.stream()
                .map(SkillTagDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(skillDTOs));
    }

    /**
     * Met à jour une compétence existante.
     * 
     * @param id Identifiant de la compétence à mettre à jour
     * @param skillTagDTO DTO contenant les nouvelles informations
     * @return Le DTO de la compétence mise à jour, ou une erreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillTagDTO>> updateSkill(
            @PathVariable String id, 
            @RequestBody SkillTagDTO skillTagDTO) {
        try {
            // Conversion du DTO vers le modèle
            SkillTag skillTag = skillTagDTO.toModel();
            
            // Appel du service métier
            SkillTag updatedSkill = skillService.updateSkill(id, skillTag);
            
            // Conversion du modèle vers le DTO pour la réponse
            SkillTagDTO updatedSkillDTO = SkillTagDTO.fromModel(updatedSkill);
            
            return ResponseEntity.ok(ApiResponse.success(updatedSkillDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime une compétence.
     * 
     * @param id Identifiant de la compétence à supprimer
     * @return Une réponse indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(@PathVariable String id) {
        try {
            skillService.deleteSkill(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
} 