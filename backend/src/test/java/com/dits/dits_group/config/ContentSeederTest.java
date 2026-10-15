package com.dits.dits_group.config;

import com.dits.dits_group.article.repository.ArticleRepository;
import com.dits.dits_group.partner.repository.PartnerRepository;
import com.dits.dits_group.project.repository.ProjectRepository;
import com.dits.dits_group.recruitment.repository.JobOfferRepository;
import com.dits.dits_group.service.entity.Service;
import com.dits.dits_group.service.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/** Contenu de depart : chaque rubrique vide est remplie une fois, rien de ce qui existe n'est modifie. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContentSeederTest {

    @Autowired
    private ContentSeeder seeder;

    @Autowired
    private ServiceRepository services;

    @Autowired
    private ProjectRepository projects;

    @Autowired
    private ArticleRepository articles;

    @Autowired
    private PartnerRepository partners;

    @Autowired
    private JobOfferRepository jobOffers;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void theSiteStartsWithPresentableContentShownToVisitors() throws Exception {
        assertThat(services.count()).isEqualTo(4);
        assertThat(projects.count()).isEqualTo(4);
        assertThat(articles.count()).isEqualTo(3);
        assertThat(partners.count()).isEqualTo(6);
        assertThat(jobOffers.count()).isEqualTo(3);

        mockMvc.perform(get("/api/articles")).andExpect(jsonPath("$.length()").value(3));
        mockMvc.perform(get("/api/projects")).andExpect(jsonPath("$.length()").value(4));
        mockMvc.perform(get("/api/partners")).andExpect(jsonPath("$[0].logoUrl").exists());
        mockMvc.perform(get("/api/job-offers")).andExpect(jsonPath("$.length()").value(3));

        seeder.run(null);
        assertThat(articles.count()).isEqualTo(3);
        assertThat(services.count()).isEqualTo(4);
    }

    @Test
    void theFirstTestServiceIsReplacedButRealServicesAreKept() {
        services.deleteAll();
        Service test = new Service();
        test.setTitle("Projet table");
        test.setDescription("Bon projet");
        services.save(test);

        seeder.run(null);
        assertThat(services.findAll()).extracting(Service::getTitle).contains("Conseil et expertise").doesNotContain("Projet table");

        services.deleteAll();
        Service real = new Service();
        real.setTitle("Audit de sécurité");
        real.setDescription("Service saisi par l'administrateur");
        services.save(real);

        seeder.run(null);
        assertThat(services.findAll()).extracting(Service::getTitle).containsExactly("Audit de sécurité");
    }
}
