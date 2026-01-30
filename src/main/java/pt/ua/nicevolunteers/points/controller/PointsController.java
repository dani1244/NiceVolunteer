package pt.ua.nicevolunteers.points.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ua.nicevolunteers.points.dto.CompleteActivityRequest;
import pt.ua.nicevolunteers.points.dto.PointsBalanceResponse;
import pt.ua.nicevolunteers.points.dto.PointsTransactionResponse;
import pt.ua.nicevolunteers.volunteer.service.ActivityCompletionService;
import pt.ua.nicevolunteers.volunteer.service.PointsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/points")
public class PointsController {

    private final PointsService pointsService;
    private final ActivityCompletionService activityCompletionService;

    public PointsController(PointsService pointsService,
                            ActivityCompletionService activityCompletionService) {
        this.pointsService = pointsService;
        this.activityCompletionService = activityCompletionService;
    }

    @GetMapping("/balance/{volunteerId}")
    public ResponseEntity<PointsBalanceResponse> getBalance(@PathVariable UUID volunteerId) {
        int balance = pointsService.getCurrentBalance(volunteerId);
        return ResponseEntity.ok(new PointsBalanceResponse(volunteerId, balance));
    }

    @GetMapping("/history/{volunteerId}")
    public ResponseEntity<List<PointsTransactionResponse>> getHistory(
            @PathVariable UUID volunteerId,
            @RequestParam(required = false) UUID requesterId) {

        List<PointsTransactionResponse> history;

        if (requesterId != null) {
            history = pointsService.getHistoryForVolunteer(requesterId, volunteerId)
                    .stream()
                    .map(PointsTransactionResponse::fromEntity)
                    .collect(Collectors.toList());
        } else {
            history = pointsService.getHistory(volunteerId)
                    .stream()
                    .map(PointsTransactionResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(history);
    }

    @PostMapping("/complete-activity")
    public ResponseEntity<PointsBalanceResponse> completeActivity(@RequestBody CompleteActivityRequest request) {
        activityCompletionService.completeActivity(
                request.opportunityId(),
                request.volunteerId(),
                request.promoterEmail()
        );

        int newBalance = pointsService.getCurrentBalance(request.volunteerId());
        return ResponseEntity.ok(new PointsBalanceResponse(request.volunteerId(), newBalance));
    }
}
