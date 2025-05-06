# NexusBackend
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
THIS IS THE FIRST STEP OF THE MVP APP
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
## MCD Table
<img width="631" alt="Capture d’écran 2025-05-06 à 09 28 27" src="https://github.com/user-attachments/assets/0699efe1-e83c-4199-be2c-c272d3dd167e" />

# Architecture Technique Backend - SkillNexus MVP

## 1. Vue d'ensemble de l'architecture

L'architecture backend de SkillNexus repose sur une approche microservices simplifiée, permettant un développement modulaire et une évolutivité progressive. Cette architecture est conçue pour supporter le développement rapide du MVP en 1 mois tout en posant les bases d'une plateforme robuste.

```
┌───────────────────┐       ┌─────────────────┐
│                   │       │                 │
│  Client Frontend  │◄─────►│   API Gateway   │
│    (React.js)     │       │                 │
│                   │       └────────┬────────┘
└───────────────────┘                │
                                     │
         ┌──────────────────────────┬──────────────────────────┐
         │                          │                          │
         ▼                          ▼                          ▼
┌─────────────────┐      ┌──────────────────┐      ┌────────────────────┐
│                 │      │                  │      │                    │
│ Authentification│      │ Service Profils  │      │ Service Partenariat │
│    (Keycloak)   │      │   Utilisateurs   │      │     & Matching     │
│                 │      │                  │      │                    │
└─────────────────┘      └──────────────────┘      └────────────────────┘
                                  │                           │
                                  │                           │
                         ┌────────┴──────────┐      ┌─────────┴─────────┐
                         ▼                   ▼      ▼                   ▼
                  ┌─────────────┐     ┌────────────┐     ┌─────────────────┐
                  │             │     │            │     │                 │
                  │   MongoDB   │     │   MySQL    │     │ Service Messagerie│
                  │             │     │            │     │                 │
                  └─────────────┘     └────────────┘     └─────────────────┘
```

## 2. Services microservices et responsabilités

### 2.1 API Gateway
- Point d'entrée unique pour toutes les requêtes client
- Routage des requêtes vers les services appropriés
- Gestion des en-têtes d'authentification
- Logging centralisé des requêtes

### 2.2 Service d'authentification (Keycloak)
- Gestion des identités et des accès
- Inscription et connexion des utilisateurs
- Gestion des sessions
- Sécurisation des APIs

### 2.3 Service de profils utilisateurs
- CRUD des profils utilisateurs
- Gestion des tags (compétences et intérêts)
- Stockage et récupération des photos de profil
- Gestion des objectifs d'apprentissage

### 2.4 Service de partenariat & matching
- Algorithme de matching entre utilisateurs
- Gestion des demandes de partenariat
- Suivi des partenariats actifs
- Gestion du feedback de fin de partenariat

### 2.5 Service de messagerie
- Échange de messages entre partenaires
- Stockage de l'historique des conversations
- Notifications de nouveaux messages

## 3. Modèles de données

### 3.1 MongoDB (NoSQL)
Utilisé pour les données non structurées ou évolutives:

#### Collection "users"
```json
{
  "_id": "ObjectId",
  "email": "string",
  "username": "string",
  "profilePicture": "string (URL)",
  "biography": "string (150 chars max)",
  "skills": ["string (tag IDs)"],
  "learningGoals": [
    {
      "title": "string",
      "description": "string",
      "targetDate": "Date"
    }
  ],
  "interests": ["string (tag IDs)"],
  "createdAt": "Date",
  "updatedAt": "Date"
}
```

#### Collection "messages"
```json
{
  "_id": "ObjectId",
  "senderId": "string (user ID)",
  "receiverId": "string (user ID)",
  "content": "string",
  "timestamp": "Date",
  "isRead": "boolean"
}
```

#### Collection "tags"
```json
{
  "_id": "ObjectId",
  "name": "string",
  "category": "string (skill/interest)",
  "usageCount": "number"
}
```

### 3.2 MySQL (Relationnel)
Utilisé pour les données relationnelles à structure fixe:

#### Table "partnerships"
```
- id (PK)
- user1_id (FK -> users)
- user2_id (FK -> users)
- status (enum: pending, active, ended)
- start_date (timestamp)
- end_date (timestamp, nullable)
- feedback_user1 (text, nullable)
- feedback_user2 (text, nullable)
- created_at (timestamp)
- updated_at (timestamp)
```

#### Table "partnership_goals"
```
- id (PK)
- partnership_id (FK -> partnerships)
- title (varchar)
- description (text)
- status (enum: not_started, in_progress, completed)
- created_at (timestamp)
- updated_at (timestamp)
```

## 4. Technologies et outils

### 4.1 Backend Framework
- **Spring Boot (Java)**: Framework robuste et mature pour le développement rapide d'APIs REST
- **Spring Security**: Intégration avec Keycloak pour la sécurité
- **Spring Data**: Pour l'interaction simplifiée avec MongoDB et MySQL

### 4.2 Bases de données
- **MongoDB**: Pour les données non structurées (profils, messages)
- **MySQL**: Pour les données relationnelles (partenariats)

### 4.3 Services d'authentification
- **Keycloak**: Solution complète de gestion des identités et des accès

### 4.4 Outils de développement
- **Maven/Gradle**: Gestion des dépendances et build
- **JUnit & Mockito**: Tests unitaires et d'intégration
- **Swagger/OpenAPI**: Documentation automatique des APIs

### 4.5 Conteneurisation
- **Docker**: Conteneurisation de tous les services
- **Docker Compose**: Orchestration des conteneurs pour le développement et le déploiement

## 5. Communication entre services

### 5.1 APIs REST
- Communication synchrone entre le frontend et le backend
- Format JSON pour les échanges de données
- Convention RESTful pour les endpoints

### 5.2 Communication inter-services
- Communication synchrone via Feign Client (Netflix/Spring Cloud)
- Interface déclarative pour les appels entre microservices
- Circuit breaker pattern avec Resilience4j pour la tolérance aux pannes
- Architecture événementielle légère pour les notifications entre services (peut être implémentée dans les versions ultérieures)

## 6. Environnement de déploiement

### 6.1 Configuration Docker
Chaque composant est conteneurisé séparément:
- Container Spring Boot pour chaque microservice
- Container Keycloak
- Container MongoDB
- Container MySQL

### 6.2 Docker Compose
- Définition de tous les services dans un fichier docker-compose.yml
- Configuration des variables d'environnement
- Gestion des dépendances entre services

### 6.3 Déploiement simplifié
- Scripts pour le déploiement en environnement de développement, test et production
- Configuration adaptée pour un déploiement rapide du MVP

## 7. Extensibilité pour l'après-MVP

L'architecture proposée permet d'ajouter facilement les fonctionnalités post-MVP:

### 7.1 Service de forums thématiques
- Nouveau microservice dédié aux forums
- Collections MongoDB pour les threads et réponses

### 7.2 Service d'événements communautaires
- Microservice pour la gestion des événements
- Base de données MySQL pour les inscriptions et planification

Cette architecture permet un développement modulaire où chaque équipe peut travailler indépendamment sur un service, tout en maintenant une cohérence globale du système.

