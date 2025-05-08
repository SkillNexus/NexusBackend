package com.app.userservice.repository;

import com.app.userservice.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  UserRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findByEmail(String email);
    List<UserProfile> findBySkillTagsContaining(String skillTag);
    List<UserProfile> findByInterestTagsContaining(String interestTag);
    boolean existsByEmail(String email);
}
