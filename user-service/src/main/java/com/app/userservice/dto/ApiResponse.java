package com.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe générique pour standardiser les réponses API.
 * 
 * @param <T> Type générique qui représente le type de données retourné.
 *           L'utilisation de <T> (type paramétré) permet à cette classe d'encapsuler 
 *           n'importe quel type de données (UserProfileDTO, List<SkillTagDTO>, etc.)
 *           sans avoir à créer des classes spécifiques pour chaque type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Indique si l'opération a réussi ou échoué
     */
    private boolean success;
    
    /**
     * Message détaillant le résultat de l'opération
     */
    private String message;
    
    /**
     * Données de la réponse, de type T (générique)
     * Exemple: Un objet UserProfileDTO, une liste List<SkillTagDTO>, etc.
     */
    private T data;
    
    /**
     * Méthode utilitaire pour créer une réponse réussie avec des données
     * 
     * @param <T> Type des données à encapsuler dans la réponse
     * @param data Données à inclure dans la réponse
     * @return Un objet ApiResponse indiquant un succès avec les données fournies
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Opération réussie", data);
    }
    
    /**
     * Méthode utilitaire pour créer une réponse d'erreur
     * 
     * @param <T> Type des données (non utilisé car null)
     * @param message Message d'erreur à inclure
     * @return Un objet ApiResponse indiquant une erreur avec le message fourni
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
} 