package com.polstat.sipenat.mapper;

import com.polstat.sipenat.dto.VerifikasiDto;
import com.polstat.sipenat.entity.Verifikasi;

public class VerifikasiMapper {

    public static VerifikasiDto toDto(Verifikasi verifikasi) {
        if (verifikasi == null) return null;

        return VerifikasiDto.builder()
                .id(verifikasi.getId())
                .pengajuanId(verifikasi.getPengajuan().getId())
                .nomorPengajuan(verifikasi.getPengajuan().getNomorPengajuan())
                .statusVerifikasi(verifikasi.getStatusVerifikasi())
                .catatanVerifikasi(verifikasi.getCatatanVerifikasi())
                .adminId(verifikasi.getAdmin().getId())
                .adminName(verifikasi.getAdmin().getFullName())
                .tanggalVerifikasi(verifikasi.getTanggalVerifikasi())
                .build();
    }
}