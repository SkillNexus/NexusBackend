package com.app.userservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.userservice.model.InterestTag;
import com.app.userservice.repository.InterestRepository;
import com.app.userservice.service.InterestService;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    @Autowired
    public InterestServiceImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public InterestTag createInterest(InterestTag interestTag) {
        // Vérifier si un intérêt avec ce nom existe déjà
        if (interestRepository.existsByName(interestTag.getName())) {
            throw new IllegalArgumentException("Un intérêt avec ce nom existe déjà");
        }
        return interestRepository.save(interestTag);
    }

    @Override
    public List<InterestTag> getAllInterests() {
        return interestRepository.findAll();
    }

    @Override
    public Optional<InterestTag> getInterestById(String id) {
        return interestRepository.findById(id);
    }

    @Override
    public Optional<InterestTag> getInterestByName(String name) {
        return interestRepository.findByName(name);
    }

    @Override
    public List<InterestTag> findInterestsByPartialName(String name) {
        return interestRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public InterestTag updateInterest(String id, InterestTag interestTag) {
        Optional<InterestTag> existingInterest = interestRepository.findById(id);
        if (existingInterest.isEmpty()) {
            throw new IllegalArgumentException("Intérêt non trouvé avec l'ID: " + id);
        }
        
        // Vérifier si le nom est modifié et s'il existe déjà pour un autre intérêt
        if (!existingInterest.get().getName().equals(interestTag.getName()) && 
            interestRepository.existsByName(interestTag.getName())) {
            throw new IllegalArgumentException("Un intérêt avec ce nom existe déjà");
        }
        
        // S'assurer que l'ID reste le même
        interestTag.setId(id);
        return interestRepository.save(interestTag);
    }

    @Override
    public void deleteInterest(String id) {
        if (!interestRepository.existsById(id)) {
            throw new IllegalArgumentException("Intérêt non trouvé avec l'ID: " + id);
        }
        interestRepository.deleteById(id);
    }
} 