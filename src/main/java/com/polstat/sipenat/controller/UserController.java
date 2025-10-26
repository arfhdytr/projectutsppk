package com.polstat.sipenat.controller;

import com.polstat.sipenat.dto.ChangePasswordDto;
import com.polstat.sipenat.dto.UpdateUserDto;
import com.polstat.sipenat.dto.UserDto;
import com.polstat.sipenat.exception.ApiResponse;
import com.polstat.sipenat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getProfile() {
        String email = getCurrentUserEmail();
        UserDto userDto = userService.getUserByEmail(email);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .message("Profil berhasil diambil")
                .data(userDto)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@Valid @RequestBody UpdateUserDto updateDto) {
        String email = getCurrentUserEmail();

        // Convert UpdateUserDto to UserDto
        UserDto userDto = UserDto.builder()
                .fullName(updateDto.getFullName())
                .nimNip(updateDto.getNimNip())
                .phoneNumber(updateDto.getPhoneNumber())
                .address(updateDto.getAddress())
                .build();

        UserDto updatedUser = userService.updateProfile(email, userDto);

        ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                .success(true)
                .message("Profil berhasil diperbarui")
                .data(updatedUser)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordDto passwordDto) {
        String email = getCurrentUserEmail();
        userService.changePassword(email, passwordDto.getOldPassword(), passwordDto.getNewPassword());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Password berhasil diubah")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse<Void>> deleteAccount() {
        String email = getCurrentUserEmail();
        userService.deleteAccount(email);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Akun berhasil dihapus")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}