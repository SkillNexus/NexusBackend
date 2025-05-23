package com.app.userservice.model;

/**
 * Énumération représentant les différentes étapes de complétion du profil utilisateur.
 * Utilisée pour suivre la progression de l'utilisateur dans le processus d'inscription.
 */
public enum ProfileCompletionStatus {
    INITIAL,                 // Inscription initiale
    PERSONAL_INFO_COMPLETED, // Informations personnelles complétées
    INTERESTS_COMPLETED,     // Centres d'intérêt complétés
    COMPLETED                // Profil entièrement complété
} 