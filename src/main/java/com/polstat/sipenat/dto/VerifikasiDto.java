package com.polstat.sipenat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifikasiDto {
    private Long id;


    private Long pengajuanId;

    private String nomorPengajuan;

    @NotNull(message = "Status verifikasi wajib diisi")
    private Boolean statusVerifikasi;

    @NotBlank(message = "Catatan verifikasi wajib diisi")
    private String catatanVerifikasi;

    private Long adminId;
    private String adminName;
    private LocalDateTime tanggalVerifikasi;
}