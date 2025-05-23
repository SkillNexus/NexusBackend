package com.app.userservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import com.app.userservice.repository.InterestRepository;
import com.app.userservice.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    public void testInitDatabaseWhenEmpty() throws Exception {
        // Arrange
        when(skillRepository.count()).thenReturn(0L);
        when(interestRepository.count()).thenReturn(0L);

        // Act
        CommandLineRunner runner = dataInitializer.initDatabase(skillRepository, interestRepository);
        assertNotNull(runner);
        runner.run();

        // Assert
        // Vérifier que saveAll a été appelé plusieurs fois pour chaque repository
        verify(skillRepository, atLeast(4)).saveAll(anyList());
        verify(interestRepository, atLeast(4)).saveAll(anyList());
    }

    @Test
    public void testInitDatabaseWhenNotEmpty() throws Exception {
        // Arrange
        when(skillRepository.count()).thenReturn(10L);
        when(interestRepository.count()).thenReturn(10L);

        // Act
        CommandLineRunner runner = dataInitializer.initDatabase(skillRepository, interestRepository);
        assertNotNull(runner);
        runner.run();

        // Assert
        // Vérifier que saveAll n'a pas été appelé car les tables ne sont pas vides
        verify(skillRepository, never()).saveAll(anyList());
        verify(interestRepository, never()).saveAll(anyList());
    }
} 