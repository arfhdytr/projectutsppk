package com.polstat.sipenat.dto;

import com.polstat.sipenat.entity.JenisBantuan;
import com.polstat.sipenat.entity.StatusPengajuan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PengajuanDto {
    private Long id;
    private String nomorPengajuan;

    @NotNull(message = "Jenis bantuan wajib dipilih")
    private JenisBantuan jenisBantuan;

    @NotNull(message = "Jumlah dana wajib diisi")
    @Positive(message = "Jumlah dana harus lebih dari 0")
    private BigDecimal jumlahDana;

    @NotBlank(message = "Keperluan wajib diisi")
    private String keperluan;

    private String deskripsi;
    private StatusPengajuan status;
    private LocalDateTime tanggalPengajuan;
    private String filePendukung;

    // Info User (untuk response)
    private Long userId;
    private String userName;
    private String userEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}