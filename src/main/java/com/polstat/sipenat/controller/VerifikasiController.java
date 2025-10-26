package com.polstat.sipenat.controller;

import com.polstat.sipenat.dto.VerifikasiDto;
import com.polstat.sipenat.exception.ApiResponse;
import com.polstat.sipenat.service.VerifikasiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/verifikasi")
public class VerifikasiController {

    @Autowired
    private VerifikasiService verifikasiService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/pengajuan/{pengajuanId}")
    public ResponseEntity<ApiResponse<VerifikasiDto>> verifikasiPengajuan(
            @PathVariable Long pengajuanId,
            @Valid @RequestBody VerifikasiDto verifikasiDto) {

        String email = getCurrentUserEmail();
        VerifikasiDto verifikasi = verifikasiService.verifikasiPengajuan(pengajuanId, email, verifikasiDto);

        ApiResponse<VerifikasiDto> response = ApiResponse.<VerifikasiDto>builder()
                .success(true)
                .message("Verifikasi berhasil disimpan")
                .data(verifikasi)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pengajuan/{pengajuanId}")
    public ResponseEntity<ApiResponse<VerifikasiDto>> getVerifikasiByPengajuanId(@PathVariable Long pengajuanId) {
        VerifikasiDto verifikasi = verifikasiService.getVerifikasiByPengajuanId(pengajuanId);

        ApiResponse<VerifikasiDto> response = ApiResponse.<VerifikasiDto>builder()
                .success(true)
                .message("Data verifikasi berhasil diambil")
                .data(verifikasi)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}