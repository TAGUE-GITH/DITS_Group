package com.dits.dits_group.user.service;

import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.entity.UserStatus;
import com.dits.dits_group.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findByStatus(
            UserStatus status
    ) {
        return userRepository.findByStatus(status);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    // ACTIVATION
    public User activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Utilisateur introuvable"
                        )
                );

        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    // DÉSACTIVATION
    public User disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Utilisateur introuvable"
                        )
                );

        // Protection de l'administrateur
        if (
                user.getRole() != null &&
                        "ROLE_ADMIN".equals(
                                user.getRole().getName()
                        )
        ) {
            throw new RuntimeException(
                    "Le compte administrateur ne peut pas être désactivé."
            );
        }

        user.setStatus(UserStatus.DISABLED);
        user.setEnabled(false);

        return userRepository.save(user);
    }

    // SUPPRESSION
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Utilisateur introuvable"
                        )
                );

        // Protection du compte admin
        if (
                user.getRole() != null &&
                        "ROLE_ADMIN".equals(
                                user.getRole().getName()
                        )
        ) {
            throw new RuntimeException(
                    "Le compte administrateur ne peut pas être supprimé."
            );
        }

        userRepository.delete(user);
    }
}