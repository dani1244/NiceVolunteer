package pt.ua.nicevolunteers.recommendation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.nicevolunteers.opportunity.dto.OpportunityResponse;
import pt.ua.nicevolunteers.recommendation.dto.RecommendationResponse;
import pt.ua.nicevolunteers.volunteer.service.RecommendationService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID volunteerId) {
        List<OpportunityResponse> recommendations = recommendationService.recommendForVolunteer(volunteerId)
                .stream()
                .map(OpportunityResponse::fromEntity)
                .collect(Collectors.toList());

        RecommendationResponse response = new RecommendationResponse(volunteerId, recommendations);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendationsForCurrentUser(
            @RequestParam UUID volunteerId) {
        return getRecommendations(volunteerId);
    }
}
