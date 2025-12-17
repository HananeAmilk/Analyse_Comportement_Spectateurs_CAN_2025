# Analyse du Comportement des Spectateurs - CAN 2025

## 📋 Description du Projet

Application Spring Batch développée pour analyser le comportement des spectateurs lors de la Coupe d'Afrique des Nations 2025 organisée au Maroc. Le système traite les données collectées par différents systèmes techniques (portiques électroniques et systèmes d'information) aux formats JSON et XML.

## 🎯 Objectifs

- Lire et valider les données multi-formats (JSON/XML)
- Normaliser les informations vers un modèle Java unifié
- Calculer des statistiques comportementales
- Classifier les spectateurs selon leur fréquence de participation
- Persister les données dans une base relationnelle

## 🏗️ Architecture

### Composants Spring Batch

#### 1. Readers
- **JSON Reader** : Lecture des fichiers générés par les portiques électroniques
- **XML Reader** : Lecture des fichiers produits par les systèmes d'information

#### 2. Processor
Effectue les traitements suivants :
- Validation des champs obligatoires et formats
- Décomposition de la localisation des sièges
- Calcul du nombre de matchs par spectateur
- Classification comportementale

#### 3. Writer
Enregistrement dans la base de données :
- Table `spectators` : informations des spectateurs
- Table `entries` : participations aux matchs
- Table `statistics` : statistiques dérivées

## 📊 Classification Comportementale

| Nombre de matchs | Catégorie |
|------------------|-----------|
| 1 | Première visite |
| 2-3 | Spectateur occasionnel |
| 4-6 | Spectateur régulier |
| > 6 | Super fan |

## 📁 Structure des Données

### Format JSON
```json
{
  "spectatorId": "SPX20245",
  "matchId": "MCH12",
  "entryTime": "2025-07-05T17:42:10",
  "gate": "Gate A3",
  "ticketNumber": "TK-55231-AGD",
  "age": 34,
  "nationality": "Maroc",
  "ticketType": "VIP",
  "seatLocation": {
    "tribune": "Est",
    "bloc": "C",
    "rang": 4,
    "siege": 12
  }
}
```

### Format XML
```xml
<spectatorEntry>
  <spectatorId>SPX20245</spectatorId>
  <matchId>MCH12</matchId>
  <entryTime>2025-07-05T17:42:10</entryTime>
  <gate>Gate A3</gate>
  <ticketNumber>TK-55231-AGD</ticketNumber>
  <age>34</age>
  <nationality>Maroc</nationality>
  <ticketType>VIP</ticketType>
  <seatLocation>
    <tribune>Est</tribune>
    <bloc>C</bloc>
    <rang>4</rang>
    <siege>12</siege>
  </seatLocation>
</spectatorEntry>
```

## 📈 Statistiques Dérivées

Le système génère automatiquement les statistiques suivantes :

- Nombre total de matchs par spectateur
- Répartition des spectateurs par nationalité
- Répartition par type de ticket (VIP, Standard, etc.)
- Taux d'occupation des portes d'accès
- Temps moyen d'arrivée avant le début du match
- Taux de fidélité (nombre de matchs / total de matchs)
- Top 10 des spectateurs les plus actifs
- Affluence par tribune / bloc / rang
- Évolution de la fréquentation match par match
- Temps minimum et maximum d'arrivée

## 🛠️ Technologies Utilisées

- **Java** 17+
- **Spring Boot** 3.x
- **Spring Batch** 5.x
- **Spring Data JPA**
- **Base de données** : MySQL/PostgreSQL
- **Maven** : Gestion des dépendances
- **Lombok** : Réduction du code boilerplate

## 📦 Prérequis

- JDK 17 ou supérieur
- Maven 3.8+
- MySQL 8.0+ ou PostgreSQL 13+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Installation

### 1. Cloner le projet
```bash
git clone https://github.com/votre-repo/can2025-spectator-analysis.git
cd can2025-spectator-analysis
```

### 2. Configuration de la base de données

Modifier le fichier `application.properties` :

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/can2025_db
spring.datasource.username=votre_username
spring.datasource.password=votre_password
spring.jpa.hibernate.ddl-auto=update

# Spring Batch Configuration
spring.batch.jdbc.initialize-schema=always
spring.batch.job.enabled=false
```

### 3. Créer la base de données
```sql
CREATE DATABASE can2025_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Compiler le projet
```bash
mvn clean install
```

## 🚀 Exécution

### Lancer le traitement batch
```bash
mvn spring-boot:run
```

### Ou avec un JAR
```bash
java -jar target/can2025-spectator-analysis-1.0.0.jar
```

### Paramètres d'exécution
```bash
java -jar target/can2025-spectator-analysis-1.0.0.jar \
  --input.json.path=/path/to/json/files \
  --input.xml.path=/path/to/xml/files
```

## 📂 Structure du Projet

```
src/main/java
├── com.ensa.can2025
│   ├── config
│   │   ├── BatchConfiguration.java
│   │   ├── DatabaseConfiguration.java
│   │   └── JsonXmlConfiguration.java
│   ├── model
│   │   ├── SpectatorEntry.java
│   │   ├── SeatLocation.java
│   │   └── SpectatorStatistics.java
│   ├── entity
│   │   ├── Spectator.java
│   │   ├── Entry.java
│   │   └── Statistics.java
│   ├── reader
│   │   ├── JsonSpectatorReader.java
│   │   └── XmlSpectatorReader.java
│   ├── processor
│   │   └── SpectatorDataProcessor.java
│   ├── writer
│   │   └── SpectatorDatabaseWriter.java
│   ├── validator
│   │   └── SpectatorDataValidator.java
│   └── repository
│       ├── SpectatorRepository.java
│       ├── EntryRepository.java
│       └── StatisticsRepository.java
```

## 🗄️ Schéma de Base de Données

### Table `spectators`
```sql
CREATE TABLE spectators (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spectator_id VARCHAR(50) UNIQUE NOT NULL,
    age INT,
    nationality VARCHAR(100),
    total_matches INT DEFAULT 0,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Table `entries`
```sql
CREATE TABLE entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spectator_id VARCHAR(50) NOT NULL,
    match_id VARCHAR(50) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    gate VARCHAR(50),
    ticket_number VARCHAR(100),
    ticket_type VARCHAR(50),
    tribune VARCHAR(50),
    bloc VARCHAR(10),
    rang INT,
    siege INT,
    FOREIGN KEY (spectator_id) REFERENCES spectators(spectator_id)
);
```

### Table `statistics`
```sql
CREATE TABLE statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_name VARCHAR(100) NOT NULL,
    metric_value VARCHAR(255),
    category VARCHAR(100),
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🧪 Tests

### Lancer les tests unitaires
```bash
mvn test
```

### Lancer les tests d'intégration
```bash
mvn verify
```

## 📝 Fichiers de Test

Le projet inclut des jeux de données de test dans le dossier `src/test/resources` :

- `test-data.json` : Exemples de données JSON
- `test-data.xml` : Exemples de données XML

## 📊 Monitoring

Le système génère des logs détaillés :

```properties
# Logging Configuration
logging.level.com.ensa.can2025=DEBUG
logging.level.org.springframework.batch=INFO
logging.file.name=logs/can2025-batch.log
```

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit les changements (`git commit -m 'Ajout de fonctionnalité'`)
4. Push vers la branche (`git push origin feature/amelioration`)
5. Ouvrir une Pull Request

## 👥 Auteurs

- **Équipe de développement** - Amilk Hanane - Oulaarif Nouhaila
- **Encadrant** : M. El yaakoubi


---

**ENSA-Agadir** - Architecture Logicielle 2025-2026
