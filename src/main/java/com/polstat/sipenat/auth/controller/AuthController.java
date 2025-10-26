package com.polstat.sipenat.auth.controller;

import com.polstat.sipenat.auth.dto.AuthRequest;
import com.polstat.sipenat.auth.dto.AuthResponse;
import com.polstat.sipenat.auth.dto.RegisterRequest;
import com.polstat.sipenat.dto.UserDto;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.exception.ApiResponse;
import com.polstat.sipenat.exception.BadRequestException;
import com.polstat.sipenat.repository.UserRepository;
import com.polstat.sipenat.service.UserService;
import com.polstat.sipenat.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API untuk registrasi dan login user")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Registrasi user baru",
            description = "Endpoint untuk mendaftarkan user baru (Mahasiswa/Admin/Petugas Keuangan)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Registrasi berhasil",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Email/username sudah terdaftar atau validasi gagal"
            )
    })

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto = userService.register(request);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .message("Registrasi berhasil")
                .data(userDto)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Login user",
            description = "Endpoint untuk login dan mendapatkan JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login berhasil, token JWT dikembalikan",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Email atau password salah"
            )
    })

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Email atau password salah");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User tidak ditemukan"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        String token = jwtUtil.generateToken(userDetails, claims);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login berhasil")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}