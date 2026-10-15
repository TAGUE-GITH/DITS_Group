package com.dits.dits_group.project.controller;

import com.dits.dits_group.project.dto.ProjectRequest;
import com.dits.dits_group.project.dto.ProjectResponse;
import com.dits.dits_group.project.service.ProjectService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/projects")
public class AdminProjectController {

    private final ProjectService projectService;

    public AdminProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponse> createProject(@Valid @ModelAttribute ProjectRequest request) {
        ProjectResponse project = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @ModelAttribute ProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponse> togglePublished(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.togglePublished(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Réalisation supprimée avec succès."));
    }
}