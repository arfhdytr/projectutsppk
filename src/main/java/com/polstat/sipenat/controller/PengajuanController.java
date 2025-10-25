package com.polstat.sipenat.controller;

import com.polstat.sipenat.entity.Pengajuan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// controller/PengajuanController.java
@RestController
@RequestMapping("/api/pengajuan")
@RequiredArgsConstructor
public class PengajuanController {
    private final PengajuanService pengajuanService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePengajuanDto dto, Principal principal) {
        Pengajuan p = pengajuanService.create(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreatePengajuanDto dto, Principal principal) {
        return ResponseEntity.ok(pengajuanService.update(id, dto, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<?> list(Principal principal, @RequestParam(required=false) String status) {
        return ResponseEntity.ok(pengajuanService.list(principal.getName(), status));
    }

    @PostMapping("/{id}/ajukan")
    public ResponseEntity<?> ajukan(@PathVariable Long id, Principal principal) {
        pengajuanService.submit(id, principal.getName());
        return ResponseEntity.ok(Map.of("message","Pengajuan diajukan"));
    }

    @PostMapping("/{id}/verifikasi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifikasi(@PathVariable Long id, @RequestBody VerifikasiDto dto, Principal principal) {
        pengajuanService.verifikasi(id, dto, principal.getName());
        return ResponseEntity.ok(Map.of("message","Verifikasi disimpan"));
    }

    @PostMapping("/{id}/persetujuan")
    @PreAuthorize("hasRole('PETUGAS_KEUANGAN')")
    public ResponseEntity<?> persetujuan(@PathVariable Long id, @RequestBody PersetujuanDto dto, Principal principal) {
        pengajuanService.persetujuan(id, dto, principal.getName());
        return ResponseEntity.ok(Map.of("message","Keputusan dicatat"));
    }
}

