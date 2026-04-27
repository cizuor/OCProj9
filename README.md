# Your Car Your Way - PoC : Tchat en Temps Réel

Bienvenue sur le dépôt du PoC "Tchat & Support Client" de Your Car Your Way. Ce projet a pour but de valider la faisabilité technique d'une messagerie en temps réel, sécurisée et multilingue, dans le cadre de la refonte globale de notre SI.
## Architecture Globale (N-Tiers)

**Le projet adopte une séparation stricte entre le client et le serveur :**

    Back-end (API & WebSockets) : Java 25, Spring Boot 3.5, Spring Security (JWT), Spring Data JPA.

    Front-end (SPA) : Angular 21+ (Standalone, Signals), Bootstrap 5, RxJS.

    Base de données : PostgreSQL 15.

    Infrastructure : Conteneurisation intégrale via Docker.

### 1. Démarrage Rapide (Installation)
Prérequis

**Assurez-vous d'avoir installé sur votre machine :**

* Docker Desktop (ou Docker Engine + Docker Compose)

* Node.js (v20 ou supérieur) et npm

* Angular CLI (npm install -g @angular/cli)

**Étape A : Lancer le Back-end (Docker)**

Le backend est entièrement "Dockerisé" via un build multi-stage (vous n'avez pas besoin d'installer Java ou Maven localement).

    Ouvrez un terminal à la racine du dossier backend (/chatpoc).

    Lancez la commande suivante :
    
```bash
    docker-compose up --build
```
* Que se passe-t-il ?

       * Docker télécharge l'environnement, compile le code Java, lance la base de données PostgreSQL et démarre l'API sur le port 8080.

       * Un script d'initialisation (DataInitializer) crée automatiquement des voitures dans le garage au premier démarrage.

**Étape B : Lancer le Front-end (Angular)**

    Ouvrez un terminal dans le dossier frontend (/chat-poc-front).

    Installez les dépendances :

```bash
    npm install
``` 

    Lancez le serveur de développement :

        Pour la version Française (Clients) : ng serve

        Pour la version Anglaise (Support) : ng serve --configuration=en

    Accédez au site sur : http://localhost:4200

### 2. Comment tester l'application ?

Pour valider le PoC, suivez ce scénario de démonstration :

    Côté Client : Allez sur http://localhost:4200/register et créez un compte (ex: client@mail.com).

        Magie du PoC : Le système vous connecte automatiquement et génère 2 réservations aléatoires pour vous permettre de tester le tchat tout de suite.

    Côté Agent de Support : Ouvrez une fenêtre de navigation privée. Allez sur http://localhost:4200/login. Créez un compte avec une adresse finissant par @chatpoc.com (ex: alice@chatpoc.com).

        Magie du PoC : L'application détecte votre domaine et vous donne accès à toutes les conversations de la base de données.

        Par défaut un utilisateur support (email: support@chatpoc.com    pseudo: support    mdp: Support123! ) est créé via le DataInitializer


    Le Temps Réel : Cliquez sur la même réservation sur les deux navigateurs. Écrivez un message. Il apparaîtra instantanément chez l'autre personne.

### 3. Guide d'Architecture pour les Développeurs (À lire absolument)

Pour les nouveaux arrivants (Juniors), voici les concepts clés implémentés dans ce projet :
#### Sécurité & Authentification (Zero Trust)

    JWT & Stateless : L'API ne stocke aucune session. À la connexion, un token JWT est généré. Le frontend Angular utilise un Interceptor (auth.interceptor.ts) pour attacher ce token à chaque requête sortante.

    Sécurité WebSocket : La connexion STOMP (WebSockets) est elle aussi sécurisée. Le AuthChannelInterceptor côté Java lit le JWT lors du "Handshake" et injecte l'identité (Principal) directement dans le canal de communication.

    Hachage : Les mots de passe sont hachés via BCrypt (force 12).

#### Temps Réel (WebSockets & STOMP)

    Nous n'utilisons pas d'appels REST classiques pour le tchat. Nous utilisons le protocole STOMP par-dessus des WebSockets natifs.

    Pub/Sub : Angular "s'abonne" à une adresse spécifique (ex: /topic/chat/42). Quand un message est envoyé au serveur, Spring Boot l'enregistre en base, puis le diffuse (Broadcast) uniquement aux utilisateurs abonnés à ce canal précis.

#### Angular Moderne (Signals & Control Flow)

    Zoneless & Signals : L'application privilégie l'utilisation des Signals (signal(), set(), update()) plutôt que les variables classiques. Cela permet de se passer de zone.js et de rafraîchir l'écran de manière ultra-optimisée sans recharger toute l'arborescence du DOM.

    Control Flow : Les templates HTML utilisent la nouvelle syntaxe native @if, @for et @empty (plus performante que les anciens *ngIf et *ngFor).

#### Internationalisation Native (i18n)

    Nous n'utilisons pas de librairies dynamiques. Le projet utilise @angular/localize.

    Les traductions sont compilées (AOT). Cela signifie que le site généré en anglais ne contient aucun texte français caché, garantissant un poids minimal (Eco-conception) et une Accessibilité (PSH) parfaite pour les lecteurs d'écran.

### 4. Documentation de l'API (Swagger)

Une documentation interactive de l'API est générée automatiquement. Une fois le conteneur Docker démarré, vous pouvez tester les routes backend (sans passer par Angular) en visitant :
👉 http://localhost:8080/swagger-ui/index.html