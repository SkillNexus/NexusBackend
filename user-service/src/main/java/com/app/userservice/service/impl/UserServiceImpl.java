package com.app.userservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.userservice.model.InterestTag;
import com.app.userservice.model.LearningObjective;
import com.app.userservice.model.ProfileCompletionStatus;
import com.app.userservice.model.SkillTag;
import com.app.userservice.model.UserProfile;
import com.app.userservice.repository.UserRepository;
import com.app.userservice.service.InterestService;
import com.app.userservice.service.LearningObjectiveService;
import com.app.userservice.service.SkillService;
import com.app.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SkillService skillService;
    private final InterestService interestService;
    private final LearningObjectiveService learningObjectiveService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SkillService skillService, InterestService interestService, LearningObjectiveService learningObjectiveService) {
        this.userRepository = userRepository;
        this.skillService = skillService;
        this.interestService = interestService;
        this.learningObjectiveService = learningObjectiveService;
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
        
        // Initialiser les dates
        Date now = new Date();
        userProfile.setCreatedAt(now);
        userProfile.setUpdatedAt(now);
        
        // Initialiser le statut de complétion
        userProfile.setProfileCompletionStatus(ProfileCompletionStatus.INITIAL);
        
        return userRepository.save(userProfile);
    }
    
    @Override
    public UserProfile updatePersonalInfo(String userId, String username, String bio, String profilePictureUrl) {
        UserProfile user = getUserById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));
        
        if (username != null && !username.isEmpty()) {
            // Vérifier si le nom d'utilisateur est déjà pris
            if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
            }
            user.setUsername(username);
        }
        
        if (bio != null) {
            // Limiter la bio à 150 caractères
            if (bio.length() > 150) {
                bio = bio.substring(0, 150);
            }
            user.setBio(bio);
        }
        
        if (profilePictureUrl != null) {
            user.setProfilePictureUrl(profilePictureUrl);
        }
        
        // Mettre à jour le statut de complétion
        if (user.getProfileCompletionStatus() == ProfileCompletionStatus.INITIAL) {
            user.setProfileCompletionStatus(ProfileCompletionStatus.PERSONAL_INFO_COMPLETED);
        }
        
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }
    
    @Override
    public UserProfile updateInterests(String userId, List<InterestTag> interests) {
        UserProfile user = getUserById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));
        
        // Traiter les intérêts
        if (interests != null && !interests.isEmpty()) {
            List<InterestTag> processedInterests = new ArrayList<>();
            
            for (InterestTag interest : interests) {
                if (interest.getId() == null) {
                    // Vérifier si un intérêt avec ce nom existe déjà
                    Optional<InterestTag> existingInterest = interestService.getInterestByName(interest.getName());
                    
                    if (existingInterest.isPresent()) {
                        // Utiliser l'intérêt existant
                        processedInterests.add(existingInterest.get());
                    } else {
                        try {
                            // Créer un nouvel intérêt
                            InterestTag newInterest = new InterestTag();
                            newInterest.setName(interest.getName());
                            newInterest.setCategory(interest.getCategory());
                            newInterest.setPredefined(false);
                            
                            InterestTag savedInterest = interestService.createInterest(newInterest);
                            processedInterests.add(savedInterest);
                        } catch (Exception e) {
                            // En cas d'erreur, essayer de récupérer l'intérêt qui vient d'être créé
                            Optional<InterestTag> justCreatedInterest = interestService.getInterestByName(interest.getName());
                            if (justCreatedInterest.isPresent()) {
                                processedInterests.add(justCreatedInterest.get());
                            }
                        }
                    }
                } else {
                    // Vérifier que l'intérêt existe
                    Optional<InterestTag> existingInterest = interestService.getInterestById(interest.getId());
                    if (existingInterest.isPresent()) {
                        processedInterests.add(existingInterest.get());
                    }
                }
            }
            
            user.setInterests(processedInterests);
        }
        
        // Mettre à jour le statut de complétion
        if (user.getProfileCompletionStatus() == ProfileCompletionStatus.PERSONAL_INFO_COMPLETED) {
            user.setProfileCompletionStatus(ProfileCompletionStatus.INTERESTS_COMPLETED);
        }
        
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }
    
    @Override
    public UserProfile updateLearningObjectives(String userId, List<LearningObjective> objectives) {
        UserProfile user = getUserById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));
        
        // Vérifier le nombre d'objectifs (maximum 3)
        if (objectives != null && objectives.size() > 3) {
            throw new IllegalArgumentException("Vous ne pouvez pas avoir plus de 3 objectifs d'apprentissage");
        }
        
        if (objectives != null) {
            // Attacher l'utilisateur aux objectifs
            for (LearningObjective objective : objectives) {
                objective.setUserId(userId);
            }
            
            user.setLearningObjectives(objectives);
            
            // Traiter les objectifs d'apprentissage
            processLearningObjectives(user);
        }
        
        // Mettre à jour le statut de complétion
        if (user.getProfileCompletionStatus() == ProfileCompletionStatus.INTERESTS_COMPLETED) {
            user.setProfileCompletionStatus(ProfileCompletionStatus.COMPLETED);
        }
        
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }
    
    @Override
    public ProfileCompletionStatus getProfileCompletionStatus(String userId) {
        UserProfile user = getUserById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));
            
        return user.getProfileCompletionStatus();
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
        
        // Conserver certaines valeurs importantes si non spécifiées
        if (userProfile.getCreatedAt() == null) {
            userProfile.setCreatedAt(currentUser.getCreatedAt());
        }
        
        // Mettre à jour la date de dernière modification
        userProfile.setUpdatedAt(new Date());
        
        // Traiter les compétences, intérêts et objectifs
        processSkills(userProfile);
        processInterests(userProfile);
        processLearningObjectives(userProfile);
        
        return userRepository.save(userProfile);
    }
    
    // Méthode d'aide pour traiter les compétences
    private void processSkills(UserProfile userProfile) {
        if (userProfile.getSkills() != null && !userProfile.getSkills().isEmpty()) {
            List<SkillTag> processedSkills = new ArrayList<>();
            
            for (SkillTag skill : userProfile.getSkills()) {
                if (skill.getId() == null) {
                    // Vérifier si une compétence avec ce nom existe déjà
                    Optional<SkillTag> existingSkill = skillService.getSkillByName(skill.getName());
                    
                    if (existingSkill.isPresent()) {
                        // Utiliser la compétence existante
                        processedSkills.add(existingSkill.get());
                    } else {
                        try {
                            // Créer une nouvelle compétence
                            SkillTag newSkill = new SkillTag();
                            newSkill.setName(skill.getName());
                            newSkill.setCategory(skill.getCategory());
                            newSkill.setLevel(skill.getLevel());
                            newSkill.setPredefined(false);
                            
                            SkillTag savedSkill = skillService.createSkill(newSkill);
                            processedSkills.add(savedSkill);
                        } catch (Exception e) {
                            // En cas d'erreur, essayer de récupérer la compétence qui vient d'être créée
                            Optional<SkillTag> justCreatedSkill = skillService.getSkillByName(skill.getName());
                            if (justCreatedSkill.isPresent()) {
                                processedSkills.add(justCreatedSkill.get());
                            }
                        }
                    }
                } else {
                    // Vérifier que la compétence existe
                    Optional<SkillTag> existingSkill = skillService.getSkillById(skill.getId());
                    if (existingSkill.isPresent()) {
                        processedSkills.add(existingSkill.get());
                    }
                }
            }
            
            userProfile.setSkills(processedSkills);
        }
    }
    
    // Méthode d'aide pour traiter les intérêts
    private void processInterests(UserProfile userProfile) {
        if (userProfile.getInterests() != null && !userProfile.getInterests().isEmpty()) {
            List<InterestTag> processedInterests = new ArrayList<>();
            
            for (InterestTag interest : userProfile.getInterests()) {
                if (interest.getId() == null) {
                    // Vérifier si un intérêt avec ce nom existe déjà
                    Optional<InterestTag> existingInterest = interestService.getInterestByName(interest.getName());
                    
                    if (existingInterest.isPresent()) {
                        // Utiliser l'intérêt existant
                        processedInterests.add(existingInterest.get());
                    } else {
                        try {
                            // Créer un nouvel intérêt
                            InterestTag newInterest = new InterestTag();
                            newInterest.setName(interest.getName());
                            newInterest.setCategory(interest.getCategory());
                            newInterest.setPredefined(false);
                            
                            InterestTag savedInterest = interestService.createInterest(newInterest);
                            processedInterests.add(savedInterest);
                        } catch (Exception e) {
                            // En cas d'erreur, essayer de récupérer l'intérêt qui vient d'être créé
                            Optional<InterestTag> justCreatedInterest = interestService.getInterestByName(interest.getName());
                            if (justCreatedInterest.isPresent()) {
                                processedInterests.add(justCreatedInterest.get());
                            }
                        }
                    }
                } else {
                    // Vérifier que l'intérêt existe
                    Optional<InterestTag> existingInterest = interestService.getInterestById(interest.getId());
                    if (existingInterest.isPresent()) {
                        processedInterests.add(existingInterest.get());
                    }
                }
            }
            
            userProfile.setInterests(processedInterests);
        }
    }

    // Méthode d'aide pour traiter les objectifs d'apprentissage
    private void processLearningObjectives(UserProfile userProfile) {
        if (userProfile.getLearningObjectives() != null && !userProfile.getLearningObjectives().isEmpty()) {
            List<LearningObjective> processedObjectives = new ArrayList<>();
            
            for (LearningObjective objective : userProfile.getLearningObjectives()) {
                if (objective.getId() == null) {
                    // Vérifier si un objectif avec ce titre existe déjà pour cet utilisateur
                    Optional<LearningObjective> existingObjective = 
                        learningObjectiveService.getObjectiveByTitleAndUserId(objective.getTitle(), userProfile.getId());
                    
                    if (existingObjective.isPresent()) {
                        // Utiliser l'objectif existant
                        processedObjectives.add(existingObjective.get());
                    } else {
                        try {
                            // S'assurer que l'utilisateur est correctement assigné
                            objective.setUserId(userProfile.getId());
                            
                            // Créer un nouvel objectif
                            LearningObjective savedObjective = learningObjectiveService.createObjective(objective);
                            processedObjectives.add(savedObjective);
                        } catch (Exception e) {
                            // En cas d'erreur, essayer de récupérer l'objectif qui vient d'être créé
                            Optional<LearningObjective> justCreatedObjective = 
                                learningObjectiveService.getObjectiveByTitleAndUserId(objective.getTitle(), userProfile.getId());
                            if (justCreatedObjective.isPresent()) {
                                processedObjectives.add(justCreatedObjective.get());
                            }
                        }
                    }
                } else {
                    // Vérifier que l'objectif existe
                    Optional<LearningObjective> existingObjective = learningObjectiveService.getObjectiveById(objective.getId());
                    if (existingObjective.isPresent()) {
                        processedObjectives.add(existingObjective.get());
                    }
                }
            }
            
            userProfile.setLearningObjectives(processedObjectives);
        }
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