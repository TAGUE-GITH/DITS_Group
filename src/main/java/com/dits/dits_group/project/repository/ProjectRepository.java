package com.dits.dits_group.project.repository;

import com.dits.dits_group.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    List<Project> findByPublishedTrue();

    Optional<Project> findByIdAndPublishedTrue(Long id);
}