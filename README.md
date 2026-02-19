# 🏥 HealthTech — Système de Gestion Médicale

Application web complète de gestion médicale développée avec **Java Spring Boot** et **Angular**.

![HealthTech Dashboard](./docs/dashboard-preview.png)

## 🚀 Stack Technique

| Couche | Technologie |
|---|---|
| Backend | Java 17, Spring Boot 3, Spring Security |
| Frontend | Angular 17, Angular Material |
| Base de données | PostgreSQL |
| Authentification | JWT (JSON Web Tokens) |
| Build | Maven, npm |

## 📦 Fonctionnalités

- ✅ **Authentification JWT** — Login/Register avec rôles (Admin, Médecin, Patient)
- ✅ **Gestion des patients** — CRUD complet avec dossiers médicaux
- ✅ **Gestion des médecins** — Profils, spécialités, disponibilités
- ✅ **Rendez-vous** — Planification, confirmation, annulation
- ✅ **Tableau de bord** — KPIs, graphiques, activité en temps réel
- ✅ **Dossiers médicaux** — Historique, prescriptions, analyses

## 🏗️ Architecture

```
healthtech-project/
├── backend/               → Spring Boot API REST
│   ├── src/main/java/com/healthtech/
│   │   ├── controller/    → REST Controllers
│   │   ├── model/         → Entités JPA
│   │   ├── service/       → Logique métier
│   │   ├── repository/    → Spring Data JPA
│   │   └── config/        → Security, JWT, CORS
│   └── src/main/resources/
│       └── application.yml
├── frontend/              → Angular App
│   └── src/app/
│       ├── core/          → Guards, Interceptors, Services
│       ├── features/      → Modules fonctionnels
│       └── shared/        → Composants partagés
└── docs/                  → Documentation
```

## ⚙️ Installation

### Prérequis
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Maven 3.8+

### Backend

```bash
cd backend

# Configurer la base de données dans application.yml
# spring.datasource.url=jdbc:postgresql://localhost:5432/healthtech_db

# Lancer le serveur
./mvnw spring-boot:run
```

> API disponible sur `http://localhost:8080`

### Frontend

```bash
cd frontend

# Installer les dépendances
npm install

# Lancer l'application
ng serve --open
```

> App disponible sur `http://localhost:4200`

### Base de données

```bash
# Créer la base de données PostgreSQL
psql -U postgres -c "CREATE DATABASE healthtech_db;"

# Le schéma est généré automatiquement par Hibernate (ddl-auto: update)
```

## 🔐 Endpoints API

```
POST   /api/auth/login           → Authentification
POST   /api/auth/register        → Inscription

GET    /api/patients             → Liste des patients
POST   /api/patients             → Créer un patient
GET    /api/patients/{id}        → Détail patient
PUT    /api/patients/{id}        → Modifier patient
DELETE /api/patients/{id}        → Supprimer patient

GET    /api/doctors              → Liste des médecins
GET    /api/appointments         → Liste des rendez-vous
POST   /api/appointments         → Créer un RDV
PUT    /api/appointments/{id}/status → Changer statut RDV

GET    /api/medical-records      → Dossiers médicaux
POST   /api/medical-records      → Créer un dossier
```

## 🐳 Docker (optionnel)

```bash
# Lancer tout l'environnement avec Docker Compose
docker-compose up -d
```

## 👥 Rôles utilisateurs

| Rôle | Accès |
|---|---|
| `ADMIN` | Accès complet à toutes les fonctionnalités |
| `DOCTOR` | Patients, RDV, Dossiers médicaux |
| `PATIENT` | Ses propres RDV et dossiers |

## 📄 Licence

MIT License — Libre d'utilisation et de modification.

---

> Développé avec ❤️ — HealthTech © 2026
