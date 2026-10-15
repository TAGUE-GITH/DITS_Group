package com.dits.dits_group.recruitment.controller;

import com.dits.dits_group.recruitment.dto.JobApplicationCreateRequest;
import com.dits.dits_group.recruitment.dto.JobApplicationResponse;
import com.dits.dits_group.recruitment.service.JobApplicationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-offers")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService
    ) {
        this.jobApplicationService =
                jobApplicationService;
    }

    // ==========================================
    // PUBLIC : POSTULER À UNE OFFRE
    // ==========================================

    @PostMapping(
            value = "/{jobOfferId}/applications",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<JobApplicationResponse>
    createApplication(
            @PathVariable Long jobOfferId,

            @Valid
            @ModelAttribute
            JobApplicationCreateRequest request
    ) {

        JobApplicationResponse createdApplication =
                jobApplicationService.create(
                        jobOfferId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdApplication);
    }
}