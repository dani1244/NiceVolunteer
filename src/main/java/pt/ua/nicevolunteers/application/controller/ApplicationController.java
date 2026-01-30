package pt.ua.nicevolunteers.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pt.ua.nicevolunteers.application.domain.Application;
import pt.ua.nicevolunteers.application.dto.ApplicationResponse;
import pt.ua.nicevolunteers.application.dto.CreateApplicationRequest;
import pt.ua.nicevolunteers.volunteer.service.ApplicationService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@RequestBody CreateApplicationRequest request) {
        Application application = applicationService.apply(
                request.volunteerId(),
                request.opportunityId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApplicationResponse.fromEntity(application));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<ApplicationResponse>> getVolunteerApplications(@PathVariable UUID volunteerId) {
        List<ApplicationResponse> applications = applicationService.getVolunteerApplications(volunteerId)
                .stream()
                .map(ApplicationResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/opportunity/{opportunityId}")
    public ResponseEntity<List<ApplicationResponse>> getOpportunityApplications(@PathVariable UUID opportunityId) {
        List<ApplicationResponse> applications = applicationService.getOpportunityApplications(opportunityId)
                .stream()
                .map(ApplicationResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/opportunity/{opportunityId}/pending")
    public ResponseEntity<List<ApplicationResponse>> getPendingApplications(@PathVariable UUID opportunityId) {
        List<ApplicationResponse> applications = applicationService.getPendingApplicationsForOpportunity(opportunityId)
                .stream()
                .map(ApplicationResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/{applicationId}/accept")
    public ResponseEntity<ApplicationResponse> acceptApplication(@PathVariable UUID applicationId) {
        Application application = applicationService.acceptApplication(applicationId);
        return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
    }

    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(@PathVariable UUID applicationId) {
        Application application = applicationService.rejectApplication(applicationId);
        return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
    }

    @PostMapping("/{applicationId}/complete")
    public ResponseEntity<ApplicationResponse> completeApplication(@PathVariable UUID applicationId) {
        Application application = applicationService.completeApplication(applicationId);
        return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
    }
}
