package com.app.userservice.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.app.userservice.model.InterestTag;
import com.app.userservice.model.SkillTag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) pour l'entité UserProfile.
 * Cette classe sert d'interface entre l'API et le modèle interne.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String id;
    private String keycloakId;
    private String username;
    private String profilePictureUrl;
    private String email;
    private String bio;
    private List<SkillTagDTO> skills;
    private List<InterestTagDTO> interests;
    private List<LearningObjectiveDTO> learningObjectives;
    private List<String> partnershipIds;
    private Date createdAt;
    private Date updatedAt;
    
    /**
     * Convertit un objet du modèle UserProfile en UserProfileDTO.
     * Cette méthode statique facilite la conversion dans les contrôleurs.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static UserProfileDTO fromModel(com.app.userservice.model.UserProfile model) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(model.getId());
        dto.setKeycloakId(model.getKeycloakId());
        dto.setUsername(model.getUsername());
        dto.setProfilePictureUrl(model.getProfilePictureUrl());
        dto.setEmail(model.getEmail());
        dto.setBio(model.getBio());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setPartnershipIds(model.getPartnershipIds());
        
        // Conversion des compétences si présentes
        if (model.getSkills() != null) {
            dto.setSkills(model.getSkills().stream()
                .map(SkillTagDTO::fromModel)
                .collect(Collectors.toList()));
        }
        
        // Conversion des intérêts si présents
        if (model.getInterests() != null) {
            dto.setInterests(model.getInterests().stream()
                .map(InterestTagDTO::fromModel)
                .collect(Collectors.toList()));
        }
        
        // Conversion des objectifs d'apprentissage si présents
        if (model.getLearningObjectives() != null) {
            dto.setLearningObjectives(model.getLearningObjectives().stream()
                .map(LearningObjectiveDTO::fromModel)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle UserProfile.
     * Utile lors de la réception de données depuis l'API.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public com.app.userservice.model.UserProfile toModel() {
        com.app.userservice.model.UserProfile model = new com.app.userservice.model.UserProfile();
        model.setId(this.id);
        model.setKeycloakId(this.keycloakId);
        model.setUsername(this.username);
        model.setProfilePictureUrl(this.profilePictureUrl);
        model.setEmail(this.email);
        model.setBio(this.bio);
        model.setCreatedAt(this.createdAt);
        model.setUpdatedAt(this.updatedAt);
        model.setPartnershipIds(this.partnershipIds);
        
        // Conversion des compétences si présentes
        if (this.skills != null) {
            List<SkillTag> skillTagList = this.skills.stream()
                .map(SkillTagDTO::toModel)
                .collect(Collectors.toList());
            model.setSkills(skillTagList);
        }
        
        // Conversion des intérêts si présents
        if (this.interests != null) {
            List<InterestTag> interestTagList = this.interests.stream()
                .map(InterestTagDTO::toModel)
                .collect(Collectors.toList());
            model.setInterests(interestTagList);
        }
        
        // Conversion des objectifs d'apprentissage si présents
        if (this.learningObjectives != null) {
            model.setLearningObjectives(this.learningObjectives.stream()
                .map(LearningObjectiveDTO::toModel)
                .collect(Collectors.toList()));
        }
        
        return model;
    }
} 