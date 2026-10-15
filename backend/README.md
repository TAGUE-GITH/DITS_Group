# DITS Group · API de la plateforme web

Serveur de la plateforme web de **DITS Group**, société de conseil et d'expertise en systèmes d'information
(conception et administration de bases de données, cloud et on-premise), à Livry-sur-Seine.

La plateforme réunit un **site vitrine**, un **espace client** et un **tableau de bord d'administration**.

- Site en ligne : [dits-frontend.vercel.app](https://dits-frontend.vercel.app)
- Code du site (React) : dossier [frontend](../frontend)



## Fonctionnalités

| Module | Contenu |
|---|---|
| Vitrine | Services, réalisations, articles, partenaires et avis clients, gérés depuis l'administration |
| Comptes | Inscription, connexion par jeton JWT, rôles client et administrateur, mot de passe oublié par e-mail |
| Espace client | Documents partagés avec le client (dépôt, téléchargement sécurisé), suivi des demandes |
| Recrutement | Offres d'emploi, candidatures avec CV (PDF, DOC, DOCX), suivi des candidatures |
| Contact | Demandes de devis et de contact, inscription à la newsletter et désinscription |
| Administration | Tableau de bord, validation des comptes, gestion de tous les contenus |

## Technique

- **Java 21**, **Spring Boot 4**, Spring Security (OAuth2 Resource Server, jetons JWT signés HS256), Spring Data JPA
- **PostgreSQL** ; tests sur **H2** en mode PostgreSQL
- Fichiers envoyés (images, documents, CV) **enregistrés en base de données** : l'hébergeur gratuit efface le disque
  à chaque redéploiement, le disque ne sert que de cache et il est reconstitué au démarrage
- E-mails par SMTP (Spring Mail), Docker, GitHub Actions

## Sécurité

- Aucun secret dans le code : la clé de signature (`JWT_SECRET`), la base et la messagerie sont configurées par
  variables d'environnement.
- Le **compte administrateur** est créé au premier démarrage à partir de `ADMIN_EMAIL` et `ADMIN_PASSWORD`
  (saisis chez l'hébergeur), puis jamais modifié. L'administrateur change ensuite son mot de passe et supprime
  `ADMIN_PASSWORD`.
- Les anciens comptes de test dont le mot de passe d'origine a été publié sont désactivés automatiquement.
- Les chemins des fichiers sont contrôlés (impossible de sortir du dossier des fichiers envoyés).

## Lancer en local

```bash
# Variables minimales (fichier .env lu par docker compose)
echo "JWT_SECRET=une-cle-longue-et-aleatoire-de-32-caracteres-minimum" > .env
echo "ADMIN_EMAIL=admin@exemple.fr" >> .env
echo "ADMIN_PASSWORD=un-mot-de-passe-provisoire" >> .env

docker compose up --build   # depuis la racine du depot : API sur http://localhost:8080 + PostgreSQL
```

## Tests

```bash
./mvnw test
```

Comptes et droits (administrateur, client, visiteur), neutralisation des anciens comptes de test, conservation des
images, documents et CV après l'effacement du disque.

## Variables d'environnement (production)

| Variable | Rôle |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Base PostgreSQL |
| `JWT_SECRET` | Clé de signature des jetons (32 caractères minimum, obligatoire) |
| `ADMIN_EMAIL`, `ADMIN_PASSWORD` | Premier compte administrateur |
| `CORS_ALLOWED_ORIGINS`, `FRONTEND_URL` | Adresse du site |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` | Envoi des e-mails |
