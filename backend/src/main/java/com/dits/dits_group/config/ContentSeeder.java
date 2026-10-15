package com.dits.dits_group.config;

import com.dits.dits_group.article.entity.Article;
import com.dits.dits_group.article.repository.ArticleRepository;
import com.dits.dits_group.common.storage.FileStorageService;
import com.dits.dits_group.partner.entity.Partner;
import com.dits.dits_group.partner.repository.PartnerRepository;
import com.dits.dits_group.project.entity.Project;
import com.dits.dits_group.project.repository.ProjectRepository;
import com.dits.dits_group.recruitment.entity.ContractType;
import com.dits.dits_group.recruitment.entity.JobOffer;
import com.dits.dits_group.recruitment.repository.JobOfferRepository;
import com.dits.dits_group.service.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contenu de depart du site (services, realisations, articles, partenaires technologiques, offres), avec des photos
 * libres de droits (Unsplash). Chaque rubrique n'est remplie que si elle est vide : ce que l'administrateur a saisi
 * n'est jamais modifie. Tout se modifie ou se supprime ensuite depuis l'administration.
 */
@Component
@Order(20)
@ConditionalOnProperty(name = "app.seed-content.enabled", havingValue = "true", matchIfMissing = true)
public class ContentSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ContentSeeder.class);
    private static final String TEAM = "L'équipe DITS Group";

    private record Photo(String folder, String unsplashId) {
    }

    private final ServiceRepository services;
    private final ProjectRepository projects;
    private final ArticleRepository articles;
    private final PartnerRepository partners;
    private final JobOfferRepository jobOffers;
    private final FileStorageService storage;
    private final boolean withPhotos;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL).build();

    public ContentSeeder(ServiceRepository services, ProjectRepository projects, ArticleRepository articles,
            PartnerRepository partners, JobOfferRepository jobOffers, FileStorageService storage,
            @Value("${app.seed-content.photos:true}") boolean withPhotos) {
        this.services = services;
        this.projects = projects;
        this.articles = articles;
        this.partners = partners;
        this.jobOffers = jobOffers;
        this.storage = storage;
        this.withPhotos = withPhotos;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedServices();
        seedProjects();
        seedArticles();
        seedPartners();
        seedJobOffers();
    }

    /** Photo telechargee et enregistree comme une image envoyee par l'administration ; sans connexion, pas de photo. */
    private String photo(Photo photo) {
        if (!withPhotos) return null;
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://images.unsplash.com/photo-" + photo.unsplashId()
                    + "?w=1200&h=800&fit=crop&q=75&fm=jpg")).timeout(Duration.ofSeconds(20)).build();
            HttpResponse<byte[]> response = http.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) return null;
            return storage.store(response.body(), "image/jpeg", "jpg", photo.folder());
        } catch (Exception exception) {
            log.warn("Photo {} indisponible : {}", photo.unsplashId(), exception.getMessage());
            return null;
        }
    }

    // ==========================================
    // SERVICES : les quatre expertises de DITS Group
    // ==========================================

    private void seedServices() {
        // Une premiere saisie d'essai (« Projet table ») est remplacee par les vrais services
        services.findAll().stream()
                .filter(s -> "Projet table".equals(s.getTitle()) && "Bon projet".equals(s.getDescription()))
                .forEach(services::delete);
        if (services.count() > 0) return;

        record Item(String title, String description, String photo) {
        }
        List<Item> items = List.of(
                new Item("Conseil et expertise",
                        "Audit de votre système d'information, analyse de vos besoins et recommandations d'architecture. Nous vous "
                                + "accompagnons dans vos choix technologiques, du cadrage du projet jusqu'à sa mise en production.",
                        "1542744173-8e7e53415bb0"),
                new Item("Conception de bases de données",
                        "Modélisation des données, schémas performants et évolutifs, adaptés à vos volumes et à vos usages métier. "
                                + "PostgreSQL, MySQL ou Couchbase : nous choisissons avec vous la technologie la plus adaptée.",
                        "1542903660-eedba2cda473"),
                new Item("Administration et exploitation",
                        "Supervision, sauvegardes, haute disponibilité, sécurité et optimisation des performances au quotidien. "
                                + "Vos bases restent disponibles, rapides et protégées.",
                        "1785682117028-6fcf2c0b515b"),
                new Item("Cloud et on-premise",
                        "Déploiement et gestion de vos environnements, dans le cloud comme dans vos propres infrastructures, "
                                + "et migrations de l'un à l'autre sans interruption de service.",
                        "1667984390538-3dea7a3fe33d"));
        for (Item item : items) {
            var service = new com.dits.dits_group.service.entity.Service();
            service.setTitle(item.title());
            service.setDescription(item.description());
            service.setImageUrl(photo(new Photo("services", item.photo())));
            service.setActive(true);
            services.save(service);
        }
        log.info("Contenu de départ : {} services", items.size());
    }

    // ==========================================
    // REALISATIONS
    // ==========================================

    private void seedProjects() {
        if (projects.count() > 0) return;
        record Item(String title, String client, String description, String technologies, int monthsAgo, String photo) {
        }
        List<Item> items = List.of(
                new Item("Migration d'une base Oracle vers PostgreSQL", "Enseigne de distribution",
                        "Migration complète d'une base de données commerciale vers PostgreSQL : analyse des dépendances, conversion "
                                + "des procédures stockées, reprise des données et bascule de nuit sans interruption pour les magasins. "
                                + "Résultat : des coûts de licence supprimés et des requêtes deux fois plus rapides.",
                        "PostgreSQL, Oracle, Migration, Bash", 3, "1784652852605-6945598f2af3"),
                new Item("Haute disponibilité et reprise après incident", "Groupe de restauration",
                        "Mise en place d'une réplication PostgreSQL avec bascule automatique, de sauvegardes chiffrées hors site et "
                                + "de procédures de reprise testées chaque trimestre. Disponibilité mesurée : 99,95 %.",
                        "PostgreSQL, Réplication, Sauvegardes, Linux", 6, "1782094673136-5198a372980c"),
                new Item("Supervision et tableau de bord des bases", "PME industrielle",
                        "Tableau de bord de supervision en temps réel : charge, requêtes lentes, espace disque et alertes par e-mail. "
                                + "L'équipe informatique anticipe désormais les incidents au lieu de les subir.",
                        "MySQL, Grafana, Prometheus, Docker", 9, "1551288049-bebda4e38f71"),
                new Item("Passage au cloud d'un système d'information", "Cabinet de services",
                        "Migration de serveurs on-premise vers un environnement cloud : architecture, sécurité des accès, "
                                + "automatisation des déploiements et accompagnement des équipes.",
                        "Cloud, Docker, Terraform, PostgreSQL", 12, "1690627931320-16ac56eb2588"));
        for (Item item : items) {
            Project project = new Project();
            project.setTitle(item.title());
            project.setClientName(item.client());
            project.setDescription(item.description());
            project.setTechnologies(item.technologies());
            project.setCompletionDate(LocalDate.now().minusMonths(item.monthsAgo()));
            project.setImageUrl(photo(new Photo("projects", item.photo())));
            project.setPublished(true);
            projects.save(project);
        }
        log.info("Contenu de départ : {} réalisations", items.size());
    }

    // ==========================================
    // ARTICLES
    // ==========================================

    private void seedArticles() {
        if (articles.count() > 0) return;
        record Item(String title, String summary, String content, int daysAgo, String photo) {
        }
        List<Item> items = List.of(
                new Item("PostgreSQL : 5 réglages pour accélérer vos requêtes",
                        "Index, statistiques, mémoire partagée : les réglages qui font gagner le plus de temps, sans changer une ligne de code.",
                        """
                                Une base lente n'est pas une fatalité. Voici les cinq réglages que nous vérifions en premier lors d'un audit.

                                1. Les index : un index manquant sur une colonne de jointure ou de filtre est la première cause de lenteur. EXPLAIN ANALYZE montre immédiatement les parcours séquentiels coûteux.

                                2. Les statistiques : un ANALYZE régulier permet au planificateur de choisir le bon plan d'exécution.

                                3. shared_buffers et work_mem : des valeurs par défaut trop prudentes obligent PostgreSQL à travailler sur disque.

                                4. Le VACUUM : sans lui, les tables gonflent et les requêtes ralentissent avec le temps.

                                5. Les requêtes elles-mêmes : pg_stat_statements identifie les quelques requêtes qui consomment l'essentiel des ressources.
                                """, 12, "1515879218367-8466d910aaa4"),
                new Item("Sauvegardes : la règle 3-2-1 appliquée à vos bases de données",
                        "Trois copies, deux supports, une copie hors site : une règle simple pour ne jamais perdre vos données.",
                        """
                                Une sauvegarde n'existe vraiment que si elle a été restaurée avec succès. La règle 3-2-1 reste la base d'une stratégie fiable.

                                Trois copies des données : la base en production et deux sauvegardes.

                                Deux supports différents : par exemple un stockage local rapide et un stockage objet.

                                Une copie hors site : en cas d'incendie, de vol ou de rançongiciel, elle permet de repartir.

                                Nous ajoutons toujours un quatrième point : un test de restauration régulier, chronométré, pour connaître le temps réel de reprise.
                                """, 30, "1744868562210-fffb7fa882d9"),
                new Item("Cloud ou on-premise : comment choisir pour vos données ?",
                        "Coûts, sécurité, performances, compétences : les critères qui guident le bon choix d'hébergement.",
                        """
                                Le cloud n'est pas toujours moins cher, et l'on-premise n'est pas toujours plus sûr. Le bon choix dépend de votre contexte.

                                Les coûts : le cloud transforme l'investissement en abonnement, mais une charge constante peut coûter plus cher sur plusieurs années.

                                La sécurité et la conformité : la localisation des données et les obligations réglementaires comptent autant que la technique.

                                Les performances : la latence entre vos applications et vos données doit être mesurée avant la migration.

                                Les compétences : exploiter une plateforme demande du temps ; un accompagnement évite les mauvaises surprises.

                                Dans bien des cas, une architecture hybride combine le meilleur des deux mondes.
                                """, 55, "1667984390535-6d03cff0b11a"));
        for (Item item : items) {
            Article article = new Article();
            article.setTitle(item.title());
            article.setSummary(item.summary());
            article.setContent(item.content().strip());
            article.setAuthor(TEAM);
            article.setPublicationDate(LocalDateTime.now().minusDays(item.daysAgo()));
            article.setImageUrl(photo(new Photo("articles", item.photo())));
            article.setPublished(true);
            articles.save(article);
        }
        log.info("Contenu de départ : {} articles", items.size());
    }

    // ==========================================
    // PARTENAIRES TECHNOLOGIQUES
    // ==========================================

    private void seedPartners() {
        if (partners.count() > 0) return;
        record Item(String name, String slug, String website, String description) {
        }
        List<Item> items = List.of(
                new Item("PostgreSQL", "postgresql", "https://www.postgresql.org",
                        "La base de données relationnelle open source de référence, au cœur de la plupart de nos missions."),
                new Item("MySQL", "mysql", "https://www.mysql.com", "Base relationnelle très répandue, que nous administrons et optimisons."),
                new Item("Couchbase", "couchbase", "https://www.couchbase.com",
                        "Base NoSQL distribuée pour les applications qui demandent des temps de réponse très courts."),
                new Item("Docker", "docker", "https://www.docker.com", "Conteneurs pour des environnements identiques du poste de travail à la production."),
                new Item("Linux", "linux", "https://www.kernel.org", "Le système qui fait tourner les serveurs de bases de données de nos clients."),
                new Item("Red Hat", "redhat", "https://www.redhat.com", "Distributions et outils d'entreprise pour des serveurs stables et supportés."));
        for (Item item : items) {
            Partner partner = new Partner();
            partner.setName(item.name());
            partner.setDescription(item.description());
            partner.setLogoUrl("https://cdn.simpleicons.org/" + item.slug());
            partner.setWebsiteUrl(item.website());
            partner.setPublished(true);
            partners.save(partner);
        }
        log.info("Contenu de départ : {} partenaires", items.size());
    }

    // ==========================================
    // OFFRES D'EMPLOI
    // ==========================================

    private void seedJobOffers() {
        if (jobOffers.count() > 0) return;
        record Item(String title, ContractType type, String description, String requirements) {
        }
        List<Item> items = List.of(
                new Item("Développeur full-stack Java / React (alternance)", ContractType.ALTERNANCE,
                        "Vous participez au développement de nos outils internes et des plateformes de nos clients : API Spring Boot, "
                                + "interfaces React, bases PostgreSQL, tests et déploiement continu.",
                        "Formation bac+3 à bac+5 en informatique ; Java, Spring Boot, React ; bases SQL ; goût du travail en équipe."),
                new Item("Administrateur de bases de données (stage de 6 mois)", ContractType.STAGE,
                        "Aux côtés de nos consultants, vous intervenez sur la supervision, les sauvegardes et l'optimisation des bases "
                                + "PostgreSQL et MySQL de nos clients.",
                        "Formation en informatique ; SQL et Linux ; curiosité et rigueur."),
                new Item("Consultant DBA PostgreSQL / MySQL", ContractType.CDI,
                        "Vous menez des missions d'audit, de migration et d'exploitation, en France et à l'international, et "
                                + "conseillez nos clients sur leurs architectures de données.",
                        "Expérience de 3 ans minimum en administration de bases de données ; haute disponibilité ; anglais professionnel."));
        for (Item item : items) {
            JobOffer offer = new JobOffer();
            offer.setTitle(item.title());
            offer.setContractType(item.type());
            offer.setLocation("Livry-sur-Seine (77)");
            offer.setDescription(item.description());
            offer.setRequirements(item.requirements());
            offer.setClosingDate(LocalDate.now().plusMonths(3));
            offer.setPublished(true);
            jobOffers.save(offer);
        }
        log.info("Contenu de départ : {} offres d'emploi", items.size());
    }
}
