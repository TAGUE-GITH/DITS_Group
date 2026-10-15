package com.dits.dits_group.user.repository;

import com.dits.dits_group.user.entity.User;
import com.dits.dits_group.user.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);

    long countByStatus(UserStatus status);
}