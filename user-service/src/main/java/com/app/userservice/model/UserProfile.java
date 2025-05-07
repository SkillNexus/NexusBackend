package com.app.userservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "users")
public class UserProfile {

    @Id
    private String id;

    // Champ pour lier avec Keycloak
    private String keycloakId;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    private String profilePictureUrl;

    @Size(max = 150, message = "La bio ne peut pas dépasser 150 caractères")
    private String bio;

    @Email(message = "Format d'email invalide")
    private String email;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @DBRef
    private List<SkillTag> skills = new ArrayList<>();

    @DBRef
    private List<LearningObjective> learningObjectives = new ArrayList<>();

    @DBRef
    private List<InterestTag> interests = new ArrayList<>();

    // References aux IDs des partenariats (gérés par partner-service)
    private List<String> partnershipIds = new ArrayList<>();

    // Constructeurs
    public UserProfile() {
    }

    public UserProfile(String id, String keycloakId, String username, String profilePictureUrl, String bio,
                       String email, Date createdAt, Date updatedAt) {
        this.id = id;
        this.keycloakId = keycloakId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<SkillTag> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillTag> skills) {
        this.skills = skills;
    }

    public List<LearningObjective> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(List<LearningObjective> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public List<InterestTag> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestTag> interests) {
        this.interests = interests;
    }

    public List<String> getPartnershipIds() {
        return partnershipIds;
    }

    public void setPartnershipIds(List<String> partnershipIds) {
        this.partnershipIds = partnershipIds;
    }

    // Méthodes utilitaires
    public void addSkill(SkillTag skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public void addLearningObjective(LearningObjective objective) {
        if (this.learningObjectives == null) {
            this.learningObjectives = new ArrayList<>();
        }
        this.learningObjectives.add(objective);
    }

    public void addInterest(InterestTag interest) {
        if (this.interests == null) {
            this.interests = new ArrayList<>();
        }
        this.interests.add(interest);
    }

    public void addPartnershipId(String partnershipId) {
        if (this.partnershipIds == null) {
            this.partnershipIds = new ArrayList<>();
        }
        this.partnershipIds.add(partnershipId);
    }

    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(keycloakId, that.keycloakId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keycloakId, username, email);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + id + '\'' +
                ", keycloakId='" + keycloakId + '\'' +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}