package com.app.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "interests")
public class InterestTag {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String category;

    // Optionnel : pour créer des tags prédéfinis vs personnalisés
    private boolean isPredefined = false;

    // Constructeurs
    public InterestTag() {
    }

    public InterestTag(String id, String name, String category, boolean isPredefined) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isPredefined = isPredefined;
    }

    // Constructeur simplifié
    public InterestTag(String name, String category) {
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

    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterestTag that = (InterestTag) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "InterestTag{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", isPredefined=" + isPredefined +
                '}';
    }
}