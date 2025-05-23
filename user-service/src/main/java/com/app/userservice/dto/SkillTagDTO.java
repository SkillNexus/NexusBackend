package com.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les tags de compétences.
 * Permet de transférer les données de compétences entre l'API et le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillTagDTO {
    private String id;
    private String name;
    private String category;
    
    /**
     * Convertit un objet du modèle SkillTag en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static SkillTagDTO fromModel(com.app.userservice.model.SkillTag model) {
        SkillTagDTO dto = new SkillTagDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCategory(model.getCategory());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle SkillTag.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public com.app.userservice.model.SkillTag toModel() {
        com.app.userservice.model.SkillTag model = new com.app.userservice.model.SkillTag();
        model.setId(this.id);
        model.setName(this.name);
        model.setCategory(this.category);
        return model;
    }
} 