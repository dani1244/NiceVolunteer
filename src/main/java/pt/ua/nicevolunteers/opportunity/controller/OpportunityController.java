package pt.ua.nicevolunteers.opportunity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pt.ua.nicevolunteers.opportunity.dto.CreateOpportunityRequest;
import pt.ua.nicevolunteers.opportunity.dto.OpportunityResponse;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.Opportunity;
import pt.ua.nicevolunteers.volunteer.domain.opportunity.OpportunityStatus;
import pt.ua.nicevolunteers.volunteer.repository.OpportunityRepository;
import pt.ua.nicevolunteers.volunteer.service.OpportunityService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final OpportunityRepository opportunityRepository;

    public OpportunityController(OpportunityService opportunityService,
                                  OpportunityRepository opportunityRepository) {
        this.opportunityService = opportunityService;
        this.opportunityRepository = opportunityRepository;
    }

    @PostMapping
    public ResponseEntity<OpportunityResponse> createOpportunity(@RequestBody CreateOpportunityRequest request) {
        Opportunity opportunity = opportunityService.createOpportunity(
                request.title(),
                request.promoterEmail(),
                request.description(),
                request.points(),
                request.location()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OpportunityResponse.fromEntity(opportunity));
    }

    @GetMapping
    public ResponseEntity<List<OpportunityResponse>> getAllOpportunities() {
        List<OpportunityResponse> opportunities = opportunityRepository.findAll()
                .stream()
                .map(OpportunityResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityResponse> getOpportunityById(@PathVariable UUID id) {
        return opportunityRepository.findById(id)
                .map(OpportunityResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/open")
    public ResponseEntity<List<OpportunityResponse>> getOpenOpportunities() {
        List<OpportunityResponse> opportunities = opportunityService.getOpenOpportunities()
                .stream()
                .map(OpportunityResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(opportunities);
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<OpportunityResponse> closeOpportunity(@PathVariable UUID id) {
        return opportunityRepository.findById(id)
                .map(opportunity -> {
                    opportunityService.closeOpportunity(opportunity);
                    return ResponseEntity.ok(OpportunityResponse.fromEntity(opportunity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OpportunityResponse> completeOpportunity(@PathVariable UUID id) {
        return opportunityRepository.findById(id)
                .map(opportunity -> {
                    opportunityService.completeOpportunity(opportunity);
                    return ResponseEntity.ok(OpportunityResponse.fromEntity(opportunity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable UUID id) {
        if (!opportunityRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        opportunityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OpportunityResponse>> getOpportunitiesByStatus(@PathVariable OpportunityStatus status) {
        List<OpportunityResponse> opportunities = opportunityRepository.findByStatus(status)
                .stream()
                .map(OpportunityResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(opportunities);
    }
}
