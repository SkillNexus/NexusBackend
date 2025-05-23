package com.app.userservice.service;

import java.util.List;
import java.util.Optional;

import com.app.userservice.model.SkillTag;

/**
 * Service pour la gestion des compétences
 */
public interface SkillService {

    /**
     * Crée une nouvelle compétence
     * @param skillTag compétence à créer
     * @return la compétence créée
     */
    SkillTag createSkill(SkillTag skillTag);
    
    /**
     * Récupère toutes les compétences
     * @return liste des compétences
     */
    List<SkillTag> getAllSkills();
    
    /**
     * Recherche une compétence par son identifiant
     * @param id identifiant de la compétence
     * @return la compétence trouvée (optionnel)
     */
    Optional<SkillTag> getSkillById(String id);
    
    /**
     * Recherche une compétence par son nom
     * @param name nom de la compétence
     * @return la compétence trouvée (optionnel)
     */
    Optional<SkillTag> getSkillByName(String name);
    
    /**
     * Recherche des compétences par nom partiel
     * @param name nom partiel de la compétence
     * @return liste des compétences correspondantes
     */
    List<SkillTag> findSkillsByPartialName(String name);
    
    /**
     * Met à jour une compétence
     * @param id identifiant de la compétence
     * @param skillTag nouvelles informations
     * @return la compétence mise à jour
     */
    SkillTag updateSkill(String id, SkillTag skillTag);
    
    /**
     * Supprime une compétence
     * @param id identifiant de la compétence à supprimer
     */
    void deleteSkill(String id);
} 