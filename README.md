﻿# BilAbonnement

BilAbonnement er en webapplikation til håndtering af bilabonnementer, flådestyring og kundeadministration.

## Indholdsfortegnelse
- [Softwaremæssige Forudsætninger](#softwaremæssige-forudsætninger)
- [Database Setup](#database-setup)
- [Applikationsopsætning og Kørsel](#applikationsopsætning-og-kørsel)

## Softwaremæssige Forudsætninger

For at kunne bygge og afvikle denne applikation, skal følgende software være installeret på dit system:

*   **Java Development Kit (JDK)**
    *   Version: 21 eller nyere.
    *   Du kan downloade JDK'en fra [Oracle](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)

*   **Apache Maven**
    *   Version: 3.6.x eller nyere.
    *   Maven er et build automation tool, der bruges til at bygge projektet og håndtere dets afhængigheder.
    *   Du kan downloade Maven fra [Apache Maven Project](https://maven.apache.org/download.cgi).

*   **MySQL Database Server**
    *   Version: 8.0 eller nyere anbefales.
    *   Applikationen bruger en MySQL database til at gemme data. Du kan installere en lokal MySQL server eller bruge en cloud-baseret MySQL service.
*   **MySQL Client (Valgfrit)**
    *   Et værktøj som MySQL Workbench, DBeaver, eller `mysql` command-line client kan være nyttigt for at interagere med databasen direkte.

## Database Setup

Applikationen kræver en MySQL database ved navn `bilabonnement`.

1.  **Opret Databasen:**
    Opret databasen `bilabonnement` på din MySQL server, hvis den ikke allerede eksisterer.
    ```sql
    CREATE DATABASE IF NOT EXISTS bilabonnement;
    ```

2.  **Kør Skema Script:**
    Projektet indeholder et SQL script til at oprette den nødvendige tabelstruktur. Dette script findes i `src/main/resources/schema.sql`.

3.  **Konfigurer Databaseforbindelse (Environment Variabler):**
    Applikationen konfigurerer databaseforbindelsen via environment variabler. Du skal sætte følgende variabler i dit system eller i din IDE's run configuration:
    *   `DB_URL`: JDBC URL'en til din database.
        *   Eksempel (Azure MySQL): `jdbc:mysql://bilabonnement-gruppe4-database.mysql.database.azure.com:3306/bilabonnement`
        *   Eksempel (Lokal MySQL): `jdbc:mysql://localhost:3306/bilabonnement`
    *   `DB_USERNAME`: Brugernavnet til din database
    *   `DB_PASSWORD`: Adgangskoden til din database 
    

    For Azure databasen angivet er værdierne:
    *   `DB_URL`: `jdbc:mysql://bilabonnement-gruppe4-database.mysql.database.azure.com:3306/bilabonnement`
    *   `DB_USERNAME`: `*********`
    *   `DB_PASSWORD`: `*********`


## Applikationsopsætning og Kørsel

Følg disse trin for at installere afhængigheder, bygge og køre applikationen:

1.  **Klon Projektet (hvis ikke allerede gjort):**
    ```bash
    git clone <repository_url>
    cd BilAbonnement
    ```

2.  **Installer Afhængigheder og Byg Projektet:**
    Åbn en terminal eller kommandoprompt i projektets rodmappe (hvor `pom.xml` filen ligger) og kør følgende Maven kommando:
    ```bash
    mvn clean install
    ```
    Denne kommando vil downloade de nødvendige afhængigheder, kompilere kildekoden, køre tests og pakke applikationen i en `.jar` fil i `target` mappen.

3.  **Kør Applikationen:**
    Efter en succesfuld build, kan du starte applikationen med følgende kommando:
    ```bash
    mvn spring-boot:run
    ```
    Sørg for at dine database environment variabler (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) er sat korrekt i din IDE's run configuration.

4.  **Tilgå Applikationen:**
    Når applikationen er startet, kan du tilgå den i din webbrowser. Standardporten er `7777` (som specificeret i `application.properties`).
    URL: `http://localhost:7777`

### Udvikling med IDE (f.eks. IntelliJ IDEA)

*   Importer projektet som et Maven projekt i din IDE.
*   Sæt environment variablerne (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) i din IDE's run/debug configuration for `BilAbonnementApplication` main klassen.
*   Du kan derefter køre eller debugge applikationen direkte fra IDE'en.
