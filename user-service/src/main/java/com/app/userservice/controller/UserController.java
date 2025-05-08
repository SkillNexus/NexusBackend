package com.app.userservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.dto.ApiResponse;
import com.app.userservice.dto.UserProfileDTO;
import com.app.userservice.model.UserProfile;
import com.app.userservice.service.UserService;

/**
 * Contrôleur REST pour les opérations liées aux utilisateurs.
 * Utilise les DTOs pour la communication avec les clients.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur.
     * 
     * @param userProfileDTO DTO contenant les informations de l'utilisateur
     * @return Une réponse contenant le DTO de l'utilisateur créé
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> createUser(@RequestBody UserProfileDTO userProfileDTO) {
        try {
            // Conversion du DTO vers le modèle
            UserProfile userProfile = userProfileDTO.toModel();
            
            // Appel du service métier
            UserProfile createdUser = userService.registerUser(userProfile);
            
            // Conversion du modèle vers le DTO pour la réponse
            UserProfileDTO createdUserDTO = UserProfileDTO.fromModel(createdUser);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createdUserDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Récupère tous les utilisateurs.
     * 
     * @return Une liste de tous les utilisateurs convertis en DTOs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getAllUsers() {
        // Récupération de tous les utilisateurs
        List<UserProfile> users = userService.getAllUsers();
        
        // Conversion de chaque utilisateur en DTO
        List<UserProfileDTO> userDTOs = users.stream()
                .map(UserProfileDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id Identifiant de l'utilisateur
     * @return Le DTO de l'utilisateur trouvé, ou une erreur s'il n'existe pas
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(user -> {
                    // Conversion de l'utilisateur en DTO
                    UserProfileDTO userDTO = UserProfileDTO.fromModel(user);
                    return ResponseEntity.ok(ApiResponse.success(userDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + id)));
    }
    
    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     * 
     * @param username Nom d'utilisateur
     * @return Le DTO de l'utilisateur trouvé, ou une erreur s'il n'existe pas
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> {
                    // Conversion de l'utilisateur en DTO
                    UserProfileDTO userDTO = UserProfileDTO.fromModel(user);
                    return ResponseEntity.ok(ApiResponse.success(userDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec le nom d'utilisateur: " + username)));
    }
    
    /**
     * Récupère un utilisateur par son email.
     * 
     * @param email Email de l'utilisateur
     * @return Le DTO de l'utilisateur trouvé, ou une erreur s'il n'existe pas
     */
    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(user -> {
                    // Conversion de l'utilisateur en DTO
                    UserProfileDTO userDTO = UserProfileDTO.fromModel(user);
                    return ResponseEntity.ok(ApiResponse.success(userDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'email: " + email)));
    }

    /**
     * Met à jour un utilisateur existant.
     * 
     * @param id Identifiant de l'utilisateur à mettre à jour
     * @param userProfileDTO DTO contenant les nouvelles informations
     * @return Le DTO de l'utilisateur mis à jour, ou une erreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateUser(
            @PathVariable String id, 
            @RequestBody UserProfileDTO userProfileDTO) {
        try {
            // Conversion du DTO vers le modèle
            UserProfile userProfile = userProfileDTO.toModel();
            
            // Appel du service métier
            UserProfile updatedUser = userService.updateUser(id, userProfile);
            
            // Conversion du modèle vers le DTO pour la réponse
            UserProfileDTO updatedUserDTO = UserProfileDTO.fromModel(updatedUser);
            
            return ResponseEntity.ok(ApiResponse.success(updatedUserDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime un utilisateur.
     * 
     * @param id Identifiant de l'utilisateur à supprimer
     * @return Une réponse indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Recherche des utilisateurs par compétence.
     * 
     * @param skillName Nom de la compétence
     * @return Une liste des utilisateurs correspondants convertis en DTOs
     */
    @GetMapping("/skills/{skillName}")
    public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getUsersBySkill(@PathVariable String skillName) {
        List<UserProfile> users = userService.findUsersBySkill(skillName);
        
        List<UserProfileDTO> userDTOs = users.stream()
                .map(UserProfileDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    /**
     * Recherche des utilisateurs par intérêt.
     * 
     * @param interestName Nom de l'intérêt
     * @return Une liste des utilisateurs correspondants convertis en DTOs
     */
    @GetMapping("/interests/{interestName}")
    public ResponseEntity<ApiResponse<List<UserProfileDTO>>> getUsersByInterest(@PathVariable String interestName) {
        List<UserProfile> users = userService.findUsersByInterest(interestName);
        
        List<UserProfileDTO> userDTOs = users.stream()
                .map(UserProfileDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }
} 