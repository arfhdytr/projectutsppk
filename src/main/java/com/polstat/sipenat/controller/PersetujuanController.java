package com.polstat.sipenat.controller;

import com.polstat.sipenat.dto.PersetujuanDto;
import com.polstat.sipenat.exception.ApiResponse;
import com.polstat.sipenat.service.PersetujuanService;
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
@RequestMapping("/api/persetujuan")
public class PersetujuanController {

    @Autowired
    private PersetujuanService persetujuanService;

    @PreAuthorize("hasRole('PETUGAS_KEUANGAN')")
    @PostMapping("/pengajuan/{pengajuanId}")
    public ResponseEntity<ApiResponse<PersetujuanDto>> prosesPersetujuan(
            @PathVariable Long pengajuanId,
            @Valid @RequestBody PersetujuanDto persetujuanDto) {

        String email = getCurrentUserEmail();
        PersetujuanDto persetujuan = persetujuanService.prosesPengajuan(pengajuanId, email, persetujuanDto);

        ApiResponse<PersetujuanDto> response = ApiResponse.<PersetujuanDto>builder()
                .success(true)
                .message("Persetujuan berhasil disimpan")
                .data(persetujuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pengajuan/{pengajuanId}")
    public ResponseEntity<ApiResponse<PersetujuanDto>> getPersetujuanByPengajuanId(@PathVariable Long pengajuanId) {
        PersetujuanDto persetujuan = persetujuanService.getPersetujuanByPengajuanId(pengajuanId);

        ApiResponse<PersetujuanDto> response = ApiResponse.<PersetujuanDto>builder()
                .success(true)
                .message("Data persetujuan berhasil diambil")
                .data(persetujuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}