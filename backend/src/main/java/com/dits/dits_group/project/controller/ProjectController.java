package com.dits.dits_group.project.controller;

import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.service.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(
            ProjectService projectService
    ) {
        this.projectService =
                projectService;
    }

    // ==========================================
    // LISTE PUBLIQUE
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ProjectResponse>>
    getPublishedProjects() {

        return ResponseEntity.ok(
                projectService.findAllPublished()
        );
    }

    // ==========================================
    // DÉTAIL PUBLIC
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse>
    getPublishedProjectById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projectService.findPublishedById(
                        id
                )
        );
    }
}