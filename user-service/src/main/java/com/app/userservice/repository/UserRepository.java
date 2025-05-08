package com.app.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.userservice.model.UserProfile;

@Repository
public interface UserRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByEmail(String email);
    Optional<UserProfile> findByUsername(String username);
    Optional<UserProfile> findByKeycloakId(String keycloakId);
    List<UserProfile> findBySkillsName(String skillName);
    List<UserProfile> findByInterestsName(String interestName);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
