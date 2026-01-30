package pt.ua.nicevolunteers.promoter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.nicevolunteers.promoter.domain.Promoter;
import pt.ua.nicevolunteers.promoter.dto.CreatePromoterRequest;
import pt.ua.nicevolunteers.promoter.dto.PromoterResponse;
import pt.ua.nicevolunteers.promoter.service.PromoterService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/promoters")
public class PromoterController {

    private final PromoterService promoterService;

    public PromoterController(PromoterService promoterService) {
        this.promoterService = promoterService;
    }

    @PostMapping
    public ResponseEntity<PromoterResponse> registerPromoter(@RequestBody CreatePromoterRequest request) {
        Promoter promoter = promoterService.register(
                request.name(),
                request.email(),
                request.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PromoterResponse.fromEntity(promoter));
    }

    @GetMapping
    public ResponseEntity<List<PromoterResponse>> getAllPromoters() {
        List<PromoterResponse> promoters = promoterService.getAllPromoters()
                .stream()
                .map(PromoterResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(promoters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoterResponse> getPromoterById(@PathVariable UUID id) {
        Promoter promoter = promoterService.getById(id);
        return ResponseEntity.ok(PromoterResponse.fromEntity(promoter));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PromoterResponse> getPromoterByEmail(@PathVariable String email) {
        Promoter promoter = promoterService.getByEmail(email);
        return ResponseEntity.ok(PromoterResponse.fromEntity(promoter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoterResponse> updatePromoter(
            @PathVariable UUID id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String contactPhone) {

        Promoter promoter = promoterService.updateProfile(id, description, website, contactPhone);
        return ResponseEntity.ok(PromoterResponse.fromEntity(promoter));
    }
}
