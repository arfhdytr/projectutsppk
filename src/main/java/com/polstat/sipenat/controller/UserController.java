package com.polstat.sipenat.controller;

import com.polstat.sipenat.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// controller/UserController.java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        User u = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(u); // sebaiknya pakai DTO bukan entity
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Principal principal, @RequestBody UpdateUserDto dto) {
        User u = userRepository.findByUsername(principal.getName()).orElseThrow();
        u.setFullName(dto.getFullName());
        u.setPhoneNumber(dto.getPhoneNumber());
        u.setAddress(dto.getAddress());
        u.setNimNip(dto.getNimNip());
        userRepository.save(u);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePasswordDto dto) {
        User u = userRepository.findByUsername(principal.getName()).orElseThrow();
        if (!passwordEncoder.matches(dto.getOldPassword(), u.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password lama salah");
        }
        u.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message","Password diubah"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(Principal principal) {
        User u = userRepository.findByUsername(principal.getName()).orElseThrow();
        userRepository.delete(u); // atau set flag deleted=true
        return ResponseEntity.ok(Map.of("message","Akun dihapus"));
    }
}

