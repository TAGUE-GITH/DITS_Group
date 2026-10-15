package com.dits.dits_group.config;

import com.dits.dits_group.role.entity.Role;
import com.dits.dits_group.role.repository.RoleRepository;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.entity.UserStatus;
import com.dits.dits_group.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Donnees indispensables au demarrage :
 * - les roles ROLE_CLIENT et ROLE_ADMIN ;
 * - le compte administrateur, cree une seule fois a partir des variables d'environnement ADMIN_EMAIL et
 *   ADMIN_PASSWORD (jamais de mot de passe dans le code). Un compte existant n'est jamais modifie ;
 * - par securite, les anciens comptes de test dont le mot de passe d'origine etait publie dans le depot
 *   sont desactives tant que ce mot de passe n'a pas ete change.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    /** Anciens comptes de test crees par les premieres versions, avec leur mot de passe d'origine (public). */
    static final Map<String, String> LEGACY_ACCOUNTS = Map.of(
            "admin@ditsgroup.com", "Admin123!",
            "client@ditsgroup.com", "Client123!"
    );

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String adminEmail,
            @Value("${app.admin.password:}") String adminPassword
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        role("ROLE_CLIENT");
        Role adminRole = role("ROLE_ADMIN");
        disableLegacyAccounts();
        createAdmin(adminRole);
    }

    private Role role(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            return roleRepository.save(role);
        });
    }

    private void disableLegacyAccounts() {
        LEGACY_ACCOUNTS.forEach((email, publicPassword) -> userRepository.findByEmail(email)
                .filter(User::isEnabled)
                .filter(user -> passwordEncoder.matches(publicPassword, user.getPassword()))
                .ifPresent(user -> {
                    user.setEnabled(false);
                    user.setStatus(UserStatus.DISABLED);
                    userRepository.save(user);
                    log.warn("Compte de test {} désactivé : son mot de passe d'origine était public", email);
                }));
    }

    private void createAdmin(Role adminRole) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) return;
        String email = adminEmail.trim().toLowerCase();
        if (userRepository.existsByEmail(email)) return;

        User admin = new User();
        admin.setFirstName("Administrateur");
        admin.setLastName("DITS Group");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRole(adminRole);
        userRepository.save(admin);
        log.info("Compte administrateur créé : {}", email);
    }
}
