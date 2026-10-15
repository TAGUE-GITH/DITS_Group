package com.dits.dits_group.auth.service;

import com.dits.dits_group.auth.dto.LoginRequest;
import com.dits.dits_group.auth.dto.LoginResponse;
import com.dits.dits_group.security.jwt.JwtService;
import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.dits.dits_group.auth.dto.RegisterRequest;
import com.dits.dits_group.role.entity.Role;
import com.dits.dits_group.role.repository.RoleRepository;
import com.dits.dits_group.user.entity.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dits.dits_group.auth.dto.ForgotPasswordRequest;
import com.dits.dits_group.auth.password.PasswordResetToken;
import com.dits.dits_group.auth.password.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.dits.dits_group.auth.dto.ResetPasswordRequest;

import com.dits.dits_group.mail.service.EmailService;

import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().getName()
        );
    }
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte existe déjà avec cet email.");
        }

        Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() ->
                        new RuntimeException("Le rôle ROLE_CLIENT est introuvable.")
                );

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(clientRole);

        user.setStatus(UserStatus.PENDING);

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        Optional<User> optionalUser =
                userRepository.findByEmail(request.getEmail());

        // On ne révèle pas si l'email existe ou non
        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByUserId(user.getId())
                        .orElseGet(PasswordResetToken::new);

        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiresAt(
                LocalDateTime.now().plusMinutes(30)
        );

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                token
        );
    }
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(request.getToken())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Token de réinitialisation invalide."
                                )
                        );

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new RuntimeException(
                    "Le lien de réinitialisation a expiré."
            );
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}