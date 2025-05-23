package com.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les tags d'intérêts.
 * Permet de transférer les données d'intérêts entre l'API et le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestTagDTO {
    private String id;
    private String name;
    private String category;
    
    /**
     * Convertit un objet du modèle InterestTag en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static InterestTagDTO fromModel(com.app.userservice.model.InterestTag model) {
        InterestTagDTO dto = new InterestTagDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCategory(model.getCategory());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle InterestTag.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public com.app.userservice.model.InterestTag toModel() {
        com.app.userservice.model.InterestTag model = new com.app.userservice.model.InterestTag();
        model.setId(this.id);
        model.setName(this.name);
        model.setCategory(this.category);
        return model;
    }
} 