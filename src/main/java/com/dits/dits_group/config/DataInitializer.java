package com.dits.dits_group.config;

import com.dits.dits_group.role.entity.Role;
import com.dits.dits_group.role.repository.RoleRepository;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_CLIENT");
                        return roleRepository.save(role);
                    });

            if (!userRepository.existsByEmail("client@ditsgroup.com")) {

                User client = new User();

                client.setFirstName("Client");
                client.setLastName("Test");
                client.setEmail("client@ditsgroup.com");

                client.setPassword(
                        passwordEncoder.encode("Client123!")
                );

                client.setEnabled(true);
                client.setRole(clientRole);

                userRepository.save(client);

                System.out.println("Utilisateur client créé.");
            }

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });



            if (!userRepository.existsByEmail("admin@ditsgroup.com")) {

                User admin = new User();

                admin.setFirstName("Admin");
                admin.setLastName("DITS Group");
                admin.setEmail("admin@ditsgroup.com");

                admin.setPassword(
                        passwordEncoder.encode("Admin123!")
                );

                admin.setEnabled(true);
                admin.setRole(adminRole);

                userRepository.save(admin);

                System.out.println("Utilisateur administrateur créé.");

            }
        };

    }
}