package com.app.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "skills")
public class SkillTag {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String category;

    // Optionnel : pour créer des tags prédéfinis vs personnalisés
    private boolean isPredefined = false;

    // Niveau de compétence
    private Integer level;

    // Constructeurs
    public SkillTag() {
    }

    public SkillTag(String id, String name, String category, boolean isPredefined, Integer level) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isPredefined = isPredefined;
        this.level = level;
    }

    // Constructeur simplifié
    public SkillTag(String name, String category) {
        this.name = name;
        this.category = category;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean predefined) {
        isPredefined = predefined;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillTag skillTag = (SkillTag) o;
        return Objects.equals(id, skillTag.id) &&
                Objects.equals(name, skillTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "SkillTag{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", isPredefined=" + isPredefined +
                ", level=" + level +
                '}';
    }
}