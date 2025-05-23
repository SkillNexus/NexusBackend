package com.app.userservice.repository;

import com.app.userservice.model.SkillTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends MongoRepository<SkillTag, String> {

    Optional<SkillTag> findByName(String name);
    List<SkillTag> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}
