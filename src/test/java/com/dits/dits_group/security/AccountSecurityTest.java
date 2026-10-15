package com.dits.dits_group.security;

import com.dits.dits_group.config.DataInitializer;
import com.dits.dits_group.role.repository.RoleRepository;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.entity.UserStatus;
import com.dits.dits_group.user.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Comptes et droits : administrateur cree depuis l'hebergeur, anciens comptes de test neutralises. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository users;

    @Autowired
    private RoleRepository roles;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataInitializer initializer;

    private String login(String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"%s\", \"password\": \"%s\"}".formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    private User user(String email, String password, String role) {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Compte");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(roles.findByName(role).orElseThrow());
        return users.save(user);
    }

    @Test
    void theAdministratorIsCreatedFromTheHostSettingsAndManagesTheSite() throws Exception {
        assertThat(users.findByEmail("patron@ditsgroup.test")).isPresent();
        String admin = login("patron@ditsgroup.test", "mot-de-passe-du-patron-2026");

        mockMvc.perform(get("/api/admin/dashboard/stats").header("Authorization", "Bearer " + admin)).andExpect(status().isOk());
        mockMvc.perform(get("/api/admin/dashboard/stats")).andExpect(status().isUnauthorized());

        user("lea@client.test", "mot-de-passe-client", "ROLE_CLIENT");
        String client = login("lea@client.test", "mot-de-passe-client");
        mockMvc.perform(get("/api/admin/dashboard/stats").header("Authorization", "Bearer " + client)).andExpect(status().isForbidden());
    }

    @Test
    void oldTestAccountsWithTheirPublicPasswordAreDisabled() throws Exception {
        user("admin@ditsgroup.com", "Admin123!", "ROLE_ADMIN");
        user("client@ditsgroup.com", "un-nouveau-mot-de-passe", "ROLE_CLIENT");

        initializer.run(null);

        assertThat(users.findByEmail("admin@ditsgroup.com").orElseThrow().isEnabled()).isFalse();
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"admin@ditsgroup.com\", \"password\": \"Admin123!\"}"))
                .andExpect(status().is4xxClientError());
        // Un compte dont le mot de passe a ete change reste actif
        assertThat(users.findByEmail("client@ditsgroup.com").orElseThrow().isEnabled()).isTrue();
    }

    @Test
    void visitorsReadThePublicContentWithoutAccount() throws Exception {
        for (String url : new String[] { "/api/services", "/api/projects", "/api/articles", "/api/partners", "/api/job-offers" })
            mockMvc.perform(get(url)).andExpect(status().isOk());
        mockMvc.perform(get("/api/admin/services")).andExpect(status().isUnauthorized());
    }
}
