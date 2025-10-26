package com.polstat.sipenat.controller;

import com.polstat.sipenat.dto.CreatePengajuanDto;
import com.polstat.sipenat.dto.PengajuanDto;
import com.polstat.sipenat.entity.Role;
import com.polstat.sipenat.entity.StatusPengajuan;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.exception.ApiResponse;
import com.polstat.sipenat.exception.ForbiddenException;
import com.polstat.sipenat.repository.UserRepository;
import com.polstat.sipenat.service.PengajuanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pengajuan")
public class PengajuanController {

    @Autowired
    private PengajuanService pengajuanService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('MAHASISWA')")
    @PostMapping
    public ResponseEntity<ApiResponse<PengajuanDto>> createPengajuan(@Valid @RequestBody CreatePengajuanDto createDto) {
        String email = getCurrentUserEmail();

        // Convert CreatePengajuanDto to PengajuanDto
        PengajuanDto pengajuanDto = PengajuanDto.builder()
                .jenisBantuan(createDto.getJenisBantuan())
                .jumlahDana(createDto.getJumlahDana())
                .keperluan(createDto.getKeperluan())
                .deskripsi(createDto.getDeskripsi())
                .build();

        PengajuanDto createdPengajuan = pengajuanService.createPengajuan(email, pengajuanDto);

        ApiResponse<PengajuanDto> response = ApiResponse.<PengajuanDto>builder()
                .success(true)
                .message("Pengajuan berhasil dibuat")
                .data(createdPengajuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PengajuanDto>> getPengajuanById(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        PengajuanDto pengajuan = pengajuanService.getPengajuanById(id, email);

        ApiResponse<PengajuanDto> response = ApiResponse.<PengajuanDto>builder()
                .success(true)
                .message("Pengajuan berhasil diambil")
                .data(pengajuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('MAHASISWA')")
    @GetMapping("/my-submissions")
    public ResponseEntity<ApiResponse<List<PengajuanDto>>> getMyPengajuan() {
        String email = getCurrentUserEmail();
        List<PengajuanDto> pengajuanList = pengajuanService.getMyPengajuan(email);

        ApiResponse<List<PengajuanDto>> response = ApiResponse.<List<PengajuanDto>>builder()
                .success(true)
                .message("Daftar pengajuan berhasil diambil")
                .data(pengajuanList)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PETUGAS_KEUANGAN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PengajuanDto>>> getPengajuanByStatus(@PathVariable StatusPengajuan status) {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getRole() == Role.ADMIN && status != StatusPengajuan.DIAJUKAN) {
            throw new ForbiddenException("Admin hanya dapat melihat pengajuan dengan status DIAJUKAN");
        }

        if (user.getRole() == Role.PETUGAS_KEUANGAN && status != StatusPengajuan.DIVERIFIKASI) {
            throw new ForbiddenException("Petugas keuangan hanya dapat melihat pengajuan dengan status DIVERIFIKASI");
        }

        List<PengajuanDto> pengajuanList = pengajuanService.getPengajuanByStatus(status);

        ApiResponse<List<PengajuanDto>> response = ApiResponse.<List<PengajuanDto>>builder()
                .success(true)
                .message("Daftar pengajuan berhasil diambil")
                .data(pengajuanList)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('MAHASISWA')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PengajuanDto>> updatePengajuan(
            @PathVariable Long id,
            @Valid @RequestBody CreatePengajuanDto updateDto) {

        String email = getCurrentUserEmail();

        PengajuanDto pengajuanDto = PengajuanDto.builder()
                .jenisBantuan(updateDto.getJenisBantuan())
                .jumlahDana(updateDto.getJumlahDana())
                .keperluan(updateDto.getKeperluan())
                .deskripsi(updateDto.getDeskripsi())
                .build();

        PengajuanDto updatedPengajuan = pengajuanService.updatePengajuan(id, email, pengajuanDto);

        ApiResponse<PengajuanDto> response = ApiResponse.<PengajuanDto>builder()
                .success(true)
                .message("Pengajuan berhasil diperbarui")
                .data(updatedPengajuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('MAHASISWA')")
    @PatchMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<PengajuanDto>> submitPengajuan(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        PengajuanDto submittedPengajuan = pengajuanService.submitPengajuan(id, email);

        ApiResponse<PengajuanDto> response = ApiResponse.<PengajuanDto>builder()
                .success(true)
                .message("Pengajuan berhasil diajukan")
                .data(submittedPengajuan)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('MAHASISWA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePengajuan(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        pengajuanService.deletePengajuan(id, email);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Pengajuan berhasil dihapus")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}