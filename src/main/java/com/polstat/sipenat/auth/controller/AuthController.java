package com.polstat.sipenat.auth.controller;

import com.polstat.sipenat.auth.dto.AuthRequest;
import com.polstat.sipenat.auth.dto.RegisterRequest;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// auth/controller/AuthController.java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // optional; we use UserDetailsService approach

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) return ResponseEntity.badRequest().body("Username sudah ada");
        if (userRepository.existsByEmail(req.email())) return ResponseEntity.badRequest().body("Email sudah ada");

        User u = User.builder()
                .username(req.username())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .fullName(req.fullName())
                .nimNip(req.nimNip())
                .phoneNumber(req.phoneNumber())
                .role(Role.valueOf(req.role().toUpperCase()))
                .build();
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message","Registrasi berhasil"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        var userOpt = userRepository.findByUsername(req.username());
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        User u = userOpt.get();
        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getUsername(), u.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
