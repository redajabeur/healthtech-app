# 🏛️ Smart Earthen Buildings — Système de Monitoring IoT/IA

> **Thèse de Doctorat — Génie Civil · Construction Durable et Intelligente**  
> *"Smart Earthen Buildings: IoT- and AI-Based Monitoring to Improve Energy Efficiency and Reduce Operational Costs"*

---

## 🎯 Objectif Scientifique

Développer un système intelligent de **monitoring hygrothermique et structurel** des constructions en terre (pisé, adobe, BTC, cob), combinant **capteurs IoT** et **intelligence artificielle**, afin de :

- 📊 Collecter des données environnementales en temps réel (température, humidité, CO₂, pression)
- 🤖 Prédire les anomalies et défaillances via des modèles IA (LSTM, Random Forest, CNN)
- ⚡ Optimiser la performance énergétique et réduire les coûts opérationnels
- 🔬 Caractériser le comportement hygroscopique des matériaux en terre

---

## 🏗️ Architecture Technique

```
smart-earthen/
├── backend/                    → Spring Boot 3 — API REST + MQTT + IA
│   └── src/main/java/com/smartearthen/
│       ├── controller/         → REST Controllers
│       ├── model/              → Entités JPA (Sensor, Reading, Building, Alert)
│       ├── service/            → Logique métier + IA
│       ├── repository/         → Spring Data JPA
│       ├── dto/                → Data Transfer Objects
│       ├── config/             → Security, CORS, MQTT, WebSocket
│       └── mqtt/               → Broker MQTT (Mosquitto integration)
├── frontend/                   → Angular 17 — Dashboard IoT
│   └── src/app/
│       ├── core/               → Services, Guards, Interceptors, Models
│       ├── features/
│       │   ├── dashboard/      → Vue d'ensemble temps réel
│       │   ├── sensors/        → Gestion capteurs IoT
│       │   ├── buildings/      → Plan thermique 2D des bâtiments
│       │   ├── alerts/         → Système d'alertes
│       │   ├── analytics/      → Graphiques et analyses
│       │   └── ai-predictions/ → Prédictions IA
│       └── shared/             → Composants réutilisables
├── ml/                         → Scripts Python IA (LSTM, RF, CNN)
│   ├── models/                 → Modèles entraînés
│   ├── data/                   → Jeux de données
│   └── notebooks/              → Jupyter Notebooks
├── iot/                        → Firmware capteurs (ESP32/Arduino)
│   ├── esp32-sensor/           → Code firmware
│   └── config/                 → Configuration MQTT
├── docs/                       → Documentation + Dashboard preview
└── docker-compose.yml
```

---

## 🔧 Stack Technique

| Couche | Technologie | Usage |
|---|---|---|
| **Backend** | Java 17 + Spring Boot 3 | API REST, WebSocket, MQTT |
| **Frontend** | Angular 17 + Angular Material | Dashboard temps réel |
| **Base de données** | PostgreSQL + TimescaleDB | Séries temporelles IoT |
| **Broker MQTT** | Eclipse Mosquitto | Communication capteurs |
| **IA/ML** | Python + TensorFlow + Scikit-learn | LSTM, Random Forest |
| **IoT Hardware** | ESP32 + DHT22/BME280/SHT31 | Capteurs hygrothermiques |
| **Cache** | Redis | Sessions + données temps réel |
| **Temps réel** | WebSocket + SockJS | Push notifications |

---

## 📡 Capteurs IoT Instrumentés

| Capteur | Mesure | Protocole | Bâtiment |
|---|---|---|---|
| DHT22 | Temp + Humidité | MQTT/WiFi | Pisé, Adobe |
| BME280 | Temp + Hum + Pression | I²C → MQTT | BTC, Cob |
| SHT31 | Temp + Humidité haute précision | MQTT | Tous |
| BME680 | Temp + Hum + COV + CO₂ | MQTT | Zones occupées |
| DS18B20 | Température mur (profil) | 1-Wire → MQTT | Murs pisé |
| Capteur résistif | Teneur en eau matériau | Analogique → ESP32 | Tous |

---

## 🤖 Modèles IA Implémentés

| Modèle | Objectif | Entrées | Sortie |
|---|---|---|---|
| **LSTM** | Prédiction T° et HR à 48h | Séries temporelles | Courbes prédites |
| **Isolation Forest** | Détection anomalies | Données capteurs | Score anomalie |
| **Random Forest** | Classification état matériau | Multi-capteurs | État : OK/DÉGRADÉ/CRITIQUE |
| **CNN 1D** | Pattern recognition confort thermique | Séquences 24h | Score confort 0-100 |
| **Régression linéaire** | Estimation coûts énergétiques | Données météo + capteurs | kWh + MAD/mois |

---

## ⚙️ Installation

### Prérequis
```bash
Java 17+, Node.js 18+, Python 3.10+, PostgreSQL 15+, Maven 3.9+, Mosquitto MQTT
```

### Démarrage rapide (Docker)
```bash
git clone https://github.com/redajabeur/smart-earthen-buildings.git
cd smart-earthen-buildings
docker-compose up -d
```

### Backend
```bash
cd backend
./mvnw spring-boot:run
# API → http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
ng serve --open
# App → http://localhost:4200
```

### Broker MQTT
```bash
mosquitto -c iot/config/mosquitto.conf
# Broker → mqtt://localhost:1883
```

---

## 🌐 API REST — Endpoints

```
# Bâtiments
GET    /api/buildings                  → Liste des bâtiments
POST   /api/buildings                  → Créer un bâtiment
GET    /api/buildings/{id}/sensors     → Capteurs d'un bâtiment

# Capteurs
GET    /api/sensors                    → Tous les capteurs
POST   /api/sensors                    → Enregistrer un capteur
GET    /api/sensors/{id}/readings      → Relevés d'un capteur
GET    /api/sensors/{id}/status        → État temps réel

# Données environnementales
GET    /api/readings?from=&to=&sensor= → Série temporelle filtrée
GET    /api/readings/latest            → Dernières mesures
POST   /api/readings/bulk              → Import bulk

# Intelligence Artificielle
GET    /api/ai/predictions/{buildingId}  → Prédictions 48h
GET    /api/ai/anomalies                 → Anomalies détectées
GET    /api/ai/comfort-score/{buildingId}→ Score de confort
GET    /api/ai/energy-forecast           → Prévision énergétique

# Alertes
GET    /api/alerts                     → Alertes actives
PUT    /api/alerts/{id}/acknowledge    → Acquitter une alerte
GET    /api/alerts/history             → Historique alertes

# Rapports
GET    /api/reports/energy?period=     → Rapport énergétique
GET    /api/reports/hygrothermal       → Rapport hygrothermique
POST   /api/reports/export             → Export PDF/Excel
```

---

## 📊 Matériaux en Terre Étudiés

- **Pisé** — Terre compactée entre banches, murs porteurs 50cm
- **Adobe** — Briques crues séchées au soleil, argile + paille
- **BTC** — Briques de Terre Comprimée stabilisées 5% chaux
- **Cob** — Mélange terre, sable et fibres végétales
- **Torchis** — Structure bois + remplissage terre-paille
- **Bauge** — Terre crue façonnée à la main

---

## 🔬 Thèse — Publications Cibles

- Journal of Building Engineering
- Energy and Buildings
- Construction and Building Materials
- Building and Environment

---

## 👨‍🔬 Informations Thèse

**Titre** : Smart Earthen Buildings: IoT- and AI-Based Monitoring to Improve Energy Efficiency and Reduce Operational Costs  
**Discipline** : Génie Civil — Construction Durable et Intelligente  
**Financement** : Programme Doctoral Ingénierie

---

## 📄 Licence

MIT License — Libre pour usage académique et de recherche.

> *"La construction en terre, conjuguée à l'intelligence artificielle, ouvre la voie à des bâtiments résilients, économes et respectueux de l'environnement."*
