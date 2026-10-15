# DITS Group · Site web

Site de **DITS Group**, société de conseil et d'expertise en systèmes d'information (bases de données, cloud et
on-premise) : site vitrine, espace client et tableau de bord d'administration.

- En ligne : [dits-group.vercel.app](https://dits-group.vercel.app)
- API (Spring Boot) : dossier [backend](../backend)



## Pages

- **Vitrine** : accueil, services, réalisations (filtre par technologie), articles avec recherche, partenaires,
  carrières (offres et candidature avec CV), contact et demande de devis, newsletter
- **Espace client** : connexion, inscription, mot de passe oublié, documents, demandes
- **Administration** : tableau de bord avec graphiques, gestion des contenus, des comptes, des candidatures,
  des documents et de la newsletter

Les sections sans contenu ne s'affichent pas, et une image introuvable est remplacée par un visuel propre.

## Technique

React 19, React Router 7, Vite, CSS par composant, React Icons ; ESLint ; tests Vitest et Testing Library.

```bash
npm install
npm run dev     # http://localhost:5173 (API attendue sur http://localhost:8080)
npm run lint
npm test
npm run build
```

En production, l'adresse de l'API est donnée par la variable `VITE_API_URL` (Vercel).
