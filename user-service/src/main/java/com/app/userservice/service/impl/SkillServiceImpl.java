package com.app.userservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.userservice.model.SkillTag;
import com.app.userservice.repository.SkillRepository;
import com.app.userservice.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public SkillTag createSkill(SkillTag skillTag) {
        // Vérifier si une compétence avec ce nom existe déjà
        if (skillRepository.existsByName(skillTag.getName())) {
            throw new IllegalArgumentException("Une compétence avec ce nom existe déjà");
        }
        return skillRepository.save(skillTag);
    }

    @Override
    public List<SkillTag> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<SkillTag> getSkillById(String id) {
        return skillRepository.findById(id);
    }

    @Override
    public Optional<SkillTag> getSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    @Override
    public List<SkillTag> findSkillsByPartialName(String name) {
        return skillRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public SkillTag updateSkill(String id, SkillTag skillTag) {
        Optional<SkillTag> existingSkill = skillRepository.findById(id);
        if (existingSkill.isEmpty()) {
            throw new IllegalArgumentException("Compétence non trouvée avec l'ID: " + id);
        }
        
        // Vérifier si le nom est modifié et s'il existe déjà pour une autre compétence
        if (!existingSkill.get().getName().equals(skillTag.getName()) && 
            skillRepository.existsByName(skillTag.getName())) {
            throw new IllegalArgumentException("Une compétence avec ce nom existe déjà");
        }
        
        // S'assurer que l'ID reste le même
        skillTag.setId(id);
        return skillRepository.save(skillTag);
    }

    @Override
    public void deleteSkill(String id) {
        if (!skillRepository.existsById(id)) {
            throw new IllegalArgumentException("Compétence non trouvée avec l'ID: " + id);
        }
        skillRepository.deleteById(id);
    }
} 