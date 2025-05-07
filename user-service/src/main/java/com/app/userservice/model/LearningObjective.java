package com.app.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Document(collection = "learning_objectives")
public class LearningObjective {

    @Id
    private String id;

    @NotBlank(message = "Le titre de l'objectif est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    private String title;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    // Pourcentage de progression
    @Min(0)
    @Max(100)
    private Integer progressPercentage = 0;

    // Date cible (optionnelle)
    private Date targetDate;

    // Référence à l'utilisateur propriétaire de cet objectif
    private String userId;

    private Date createdAt;

    private Date updatedAt;

    // Constructeurs
    public LearningObjective() {
    }

    public LearningObjective(String id, String title, String description, Integer progressPercentage,
                             Date targetDate, String userId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progressPercentage = progressPercentage;
        this.targetDate = targetDate;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructeur simplifié
    public LearningObjective(String title, String description, String userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.progressPercentage = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    // Méthodes utilitaires
    public void updateProgress(Integer newProgressPercentage) {
        this.progressPercentage = newProgressPercentage;
        this.updatedAt = new Date();
    }

    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningObjective that = (LearningObjective) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "LearningObjective{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressPercentage=" + progressPercentage +
                ", targetDate=" + targetDate +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}