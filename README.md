# DITS Group · Plateforme web

Plateforme web de **DITS Group**, société de conseil et d'expertise en systèmes d'information (conception et
administration de bases de données, cloud et on-premise), à Livry-sur-Seine.

Elle réunit un **site vitrine**, un **espace client** et un **tableau de bord d'administration**.

**Site en ligne : [dits-frontend.vercel.app](https://dits-frontend.vercel.app)**

![CI](https://github.com/TAGUE-GITH/DITS_Group/actions/workflows/ci.yml/badge.svg)

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

## Fonctionnalités

| Module | Contenu |
|---|---|
| Vitrine | Services, réalisations (filtre par technologie), articles avec recherche, partenaires, avis clients |
| Comptes | Inscription, connexion par jeton JWT, rôles client et administrateur, mot de passe oublié par e-mail |
| Espace client | Documents partagés (dépôt, téléchargement sécurisé), suivi des demandes |
| Recrutement | Offres d'emploi, candidatures avec CV (PDF, DOC, DOCX), suivi des candidatures |
| Contact | Demandes de devis et de contact, newsletter avec désinscription |
| Administration | Tableau de bord avec graphiques, validation des comptes, gestion de tous les contenus |

## Organisation du dépôt

```
backend/    API Spring Boot (Java 21) : sécurité, données, fichiers, e-mails
frontend/   Site React 19 (Vite) : vitrine, espace client, administration
Dockerfile  Image de l'API (utilisée par Render)
```

## Points techniques

- **Sécurité** : Spring Security avec jetons JWT signés ; aucun secret dans le code (clé de signature, base,
  messagerie et premier compte administrateur sont fournis par variables d'environnement) ; anciens comptes de test
  désactivés automatiquement ; chemins de fichiers contrôlés.
- **Fichiers** : images, documents et CV enregistrés en base de données. L'hébergeur gratuit efface le disque à chaque
  redéploiement : le disque ne sert que de cache et il est reconstitué au démarrage.
- **Contenu de départ** : chaque rubrique vide reçoit un contenu de présentation avec photos ; rien de ce que
  l'administrateur saisit n'est jamais modifié.
- **Qualité** : tests JUnit et MockMvc (base H2), tests Vitest et Testing Library, ESLint, intégration continue
  GitHub Actions (serveur, site et image Docker).

## Lancer en local

```bash
cp .env.example .env              # puis renseigner JWT_SECRET, ADMIN_EMAIL, ADMIN_PASSWORD
docker compose up --build         # API sur http://localhost:8080 + PostgreSQL

cd frontend && npm install && npm run dev   # site sur http://localhost:5173
```

## Tests

```bash
cd backend && ./mvnw test
cd frontend && npm run lint && npm test
```

## Mise en ligne

- **API** : Render (Docker, `Dockerfile` à la racine) et PostgreSQL. Variables : `DB_URL`, `DB_USERNAME`,
  `DB_PASSWORD`, `JWT_SECRET`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`, `CORS_ALLOWED_ORIGINS`, `FRONTEND_URL`, `MAIL_*`.
- **Site** : Vercel, dossier `frontend`, variable `VITE_API_URL` (adresse de l'API).

Détails : [backend/README.md](backend/README.md) et [frontend/README.md](frontend/README.md).
