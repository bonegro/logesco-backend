# Getting Started

### 📌 1. Prérequis
Avant de lancer le projet, assure‑toi d’avoir installé :

* Docker

* Docker Compose

* Java 21 (si tu veux lancer l’app sans Docker)

* Maven (si tu veux builder le JAR manuellement)

### 🚀 2. Lancer l’application avec Docker
Le projet contient un fichier docker-compose.yml permettant de lancer :

* MySQL 8

* MailHog (serveur SMTP + interface web) pour recevoir les mails

* L’application Spring Boot

### 3. Commande de lancement
`docker-compose up --build`

Cette commande :

construit l’image Docker de l’application (logesco)

démarre MySQL

démarre MailHog

démarre l’application Spring Boot

### 🌐 4. Accès aux services
🔹 Application Spring Boot
`http://localhost:9091`

🔹 MailHog (interface web)
`http://localhost:8025`

🔹 MySQL
host: localhost

port: 3306

user: school_user

password: school_pass

database: school_db
