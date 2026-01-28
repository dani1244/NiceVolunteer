package pt.ua.nicevolunteers.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.nicevolunteers.auth.dto.LoginRequest;
import pt.ua.nicevolunteers.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        service.login(request.email(), request.password());
        return ResponseEntity.ok().build();
    }
}
