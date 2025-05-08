package com.app.userservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.userservice.model.UserProfile;
import com.app.userservice.repository.UserRepository;
import com.app.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfile registerUser(UserProfile userProfile) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userProfile.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        
        // Vérifier si le nom d'utilisateur existe déjà
        if (userRepository.existsByUsername(userProfile.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }
        
        return userRepository.save(userProfile);
    }

    @Override
    public List<UserProfile> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserProfile> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserProfile> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<UserProfile> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<UserProfile> getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public UserProfile updateUser(String id, UserProfile userProfile) {
        Optional<UserProfile> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        UserProfile currentUser = existingUser.get();
        
        // Vérifier si l'email est modifié et s'il existe déjà pour un autre utilisateur
        if (!currentUser.getEmail().equals(userProfile.getEmail()) && 
            userRepository.existsByEmail(userProfile.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        
        // Vérifier si le nom d'utilisateur est modifié et s'il existe déjà pour un autre utilisateur
        if (!currentUser.getUsername().equals(userProfile.getUsername()) && 
            userRepository.existsByUsername(userProfile.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }
        
        // S'assurer que l'ID reste le même
        userProfile.setId(id);
        
        // Conserver le keycloakId si non spécifié dans la mise à jour
        if (userProfile.getKeycloakId() == null) {
            userProfile.setKeycloakId(currentUser.getKeycloakId());
        }
        
        return userRepository.save(userProfile);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserProfile> findUsersBySkill(String skillName) {
        return userRepository.findBySkillsName(skillName);
    }

    @Override
    public List<UserProfile> findUsersByInterest(String interestName) {
        return userRepository.findByInterestsName(interestName);
    }
} 