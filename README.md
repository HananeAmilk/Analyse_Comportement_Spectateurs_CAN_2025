# Analyse du Comportement des Spectateurs - CAN 2025

## 👥 Auteurs

- **Équipe de développement** - Amilk Hanane - Oulaarif Nouhaila
- **Encadrant** : M. El yaakoubi


## 📋 Description du Projet

Application Spring Batch développée pour analyser le comportement des spectateurs lors de la Coupe d'Afrique des Nations 2025 organisée au Maroc. Le système traite les données collectées par différents systèmes techniques (portiques électroniques et systèmes d'information) aux formats JSON et XML.

## 🎯 Objectifs

- Lire et valider les données multi-formats (JSON/XML)
- Normaliser les informations vers un modèle Java unifié
- Calculer des statistiques comportementales
- Classifier les spectateurs selon leur fréquence de participation
- Persister les données dans une base relationnelle

## 🏗️ Architecture Générale

### Composants Spring Batch

#### 1. Readers
- **JSON Reader** : Lecture des fichiers générés par les portiques électroniques
- **XML Reader** : Lecture des fichiers produits par les systèmes d'information

#### 2. Processor
Effectue les traitements suivants :
- Validation des champs obligatoires et formats
- Creation des objets Entry et Spectator
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
- **Spring Boot** 3.2
- **Spring Data JPA**
- **Base de données** : MySQL
- **Maven** : Gestion des dépendances
- **Lombok** : Réduction du code boilerplate

## 📦 Prérequis

- JDK 17 ou supérieur
- Maven 3.8+
- MySQL 8.0+ 
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 📂 Structure du Projet

```
analyse_comportement_spectateurs/
│
├── src/
│   └── main/
│       ├── java/com/example/analyse_comportement_spectateurs/
│       │   ├── batch/
│       │   │   ├── adapter/
│       │   │   │   └── LocalDateTimeAdapter.java
│       │   │   ├── listener/
│       │   │   │   └── StatisticJobListener.java
│       │   │   ├── processor/
│       │   │   │   └── SpectatorProcessor.java
│       │   │   ├── reader/
│       │   │   │   ├── JsonSpectatorReader.java
│       │   │   │   └── XmlSpectatorReader.java
│       │   │   ├── validator/
│       │   │   │   └── SpectatorValidator.java
│       │   │   └── writer/
│       │   │       └── SpectatorWriter.java
│       │   │
│       │   ├── config/
│       │   │   └── BatchConfig.java
│       │   │
│       │   ├── model/
│       │   │   ├── Dtos/
│       │   │   │   ├── SeatLocationDto.java
│       │   │   │   └── SpectatorEntryDto.java
│       │   │   └── Entities/
│       │   │       ├── Entry.java
│       │   │       ├── Spectator.java
│       │   │       └── Statistic.java
│       │   │
│       │   ├── repositories/
│       │   │   ├── EntryRepository.java
│       │   │   ├── SpectatorRepository.java
│       │   │   └── StatisticRepository.java
│       │   │
│       │   └── service/
│       │       ├── StatisticService.java
│       │       └── AnalyseComportementSpectateurs2025Application.java
│       │
│       └── resources/
│           ├── Data/
│           │   ├── spectators.json
│           │   ├── spectators.xml
│           │   ├── application.properties
│           │   └── schema.sql
│           │
│           └── (fichiers de configuration)
│
└── test/
    ├── java/com/example/analyse_comportement_spectateurs/
    │   └── batch/
    │       ├── BatchIntegrationTest.java
    │       ├── SpectatorProcessorTest.java
    │       └── SpectatorValidatorTest.java
    │
    ├── resources/data/
    │   ├── spectators.json
    │   └── spectators.xml
    │
    └── AnalyseComportementSpectateurs2025ApplicationTests.java
│
├── .env
├── .gitattributes
├── .gitignore
├── LICENSE
├── README.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```


## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/amelioration`)
3. Commit les changements (`git commit -m 'Ajout de fonctionnalité'`)
4. Push vers la branche (`git push origin feature/amelioration`)
5. Ouvrir une Pull Request


---

**ENSA-Agadir** - Architecture Logicielle 2025-2026
