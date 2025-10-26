package com.polstat.sipenat.dto;

import com.polstat.sipenat.entity.JenisBantuan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePengajuanDto {

    @NotNull(message = "Jenis bantuan wajib dipilih")
    private JenisBantuan jenisBantuan;

    @NotNull(message = "Jumlah dana wajib diisi")
    @Positive(message = "Jumlah dana harus lebih dari 0")
    private BigDecimal jumlahDana;

    @NotBlank(message = "Keperluan wajib diisi")
    private String keperluan;

    private String deskripsi;
}