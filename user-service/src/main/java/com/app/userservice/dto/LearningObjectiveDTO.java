package com.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les objectifs d'apprentissage.
 * Contient les méthodes de conversion depuis et vers le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningObjectiveDTO {
    private String id;
    private String title;
    private String description;
    private int progressPercentage;
    
    /**
     * Convertit un objet du modèle LearningObjective en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static LearningObjectiveDTO fromModel(com.app.userservice.model.LearningObjective model) {
        LearningObjectiveDTO dto = new LearningObjectiveDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setProgressPercentage(model.getProgressPercentage());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle LearningObjective.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public com.app.userservice.model.LearningObjective toModel() {
        com.app.userservice.model.LearningObjective model = new com.app.userservice.model.LearningObjective();
        model.setId(this.id);
        model.setTitle(this.title);
        model.setDescription(this.description);
        model.setProgressPercentage(this.progressPercentage);
        return model;
    }
} 