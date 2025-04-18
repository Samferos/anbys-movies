# SAÉ 4 - R4.11 Développement Mobile
## Application Android native - Recherche de films (TMDb)

### Présentation

Ce projet a été réalisé dans le cadre de la SAÉ 4 du module R4.11 - Développement Mobile.  
L’objectif est de développer une application Android native permettant de rechercher, afficher et consulter les détails de films en utilisant l’API publique de The Movie Database (TMDb).

L’application est conçue pour être compatible avec les machines de l’IUT.

---

### Équipe projet

- Samuel Antunes 
- Théo Phan
- Lohan Boeglin

Dépôt Git du projet :  
[https://gitlab.univ-nantes.fr/school-stuff/semester-4/r4.11-d-veloppement-pour-applications-mobiles/dev.mobile.sae](https://gitlab.univ-nantes.fr/school-stuff/semester-4/r4.11-d-veloppement-pour-applications-mobiles/dev.mobile.sae)

### Fonctionnalités principales

- Recherche de films via l’API TMDb
- Affichage des résultats dans un `RecyclerView` personnalisée
- Vue détaillée d’un film (résumé, titre, date de sortie, note, etc.)
- Chargement dynamique des affiches de films
- Utilisation d’une architecture MVVM :
    - Modèle de données : classe `Movie`, `Movies`, `Genre`, `Genres`
    - DAO pour la gestion de la désérialisation JSON
- Trois activités principales :
    - Activité de recherche
    - Liste des résultats
    - Détail d’un film sélectionné

### Fonctionnalités avancées

Des fonctionnalités supplémentaires ont été ajoutées afin d’approfondir certains aspects du développement Android :

- Tri des résultats (popularité, note, date)
- Support multilingue (français / anglais)
- Utilisation de `Fragments`
- Persistance de données (film favoris)

### Prérequis techniques

- Android Studio
- SDK Android (niveau API 24 minimum)
- Connexion Internet active

### Installation et exécution

1. Cloner le dépôt :
```bash
git clone https://gitlab.univ-nantes.fr/school-stuff/semester-4/r4.11-d-veloppement-pour-applications-mobiles/dev.mobile.sae
```

2. Ouvrir le projet dans Android Studio
3. Synchroniser Gradle
4. Lancer l’application sur un émulateur ou un appareil Android

### Déploiement sur machine IUT

- Le projet a été conçu pour être compilé et exécuté sur les machines de l’IUT.
- **Attention** : en cas d’incompatibilité avec la version du plugin Android Gradle, il peut être nécessaire de modifier le fichier `libs.versions.toml` :

  Remplacer :
  ```
  agp = "8.9.1"
  ```
  par :
  ```
  agp = "8.5.2"
  ```

- Aucune autre configuration ou dépendance externe n’est requise.
- Le code est directement exécutable depuis Android Studio.

### Documentation et ressources

- Documentation TMDb : https://developer.themoviedb.org/docs/getting-started
- Documentation Android : https://developer.android.com/docs

