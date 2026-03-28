package com.groupeisi.userservice.controller;

import com.groupeisi.userservice.config.JwtService;
import com.groupeisi.userservice.dto.UserAccountDto;
import com.groupeisi.userservice.entities.UserAccount;
import com.groupeisi.userservice.repository.UserAccountRepository;
import com.groupeisi.userservice.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final UserAccountService userAccountService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserAccountDto> register(@RequestBody UserAccountDto dto) {
        return ResponseEntity.ok(userAccountService.create(dto));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserAccountDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserAccount user = userAccountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
