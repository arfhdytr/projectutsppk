package com.polstat.sipenat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersetujuanDto {
    private Long id;


    private Long pengajuanId;

    private String nomorPengajuan;

    @NotNull(message = "Keputusan wajib diisi")
    private Boolean keputusan;

    @NotBlank(message = "Catatan keputusan wajib diisi")
    private String catatanKeputusan;

    private Long petugasId;
    private String petugasName;
    private LocalDateTime tanggalKeputusan;
}