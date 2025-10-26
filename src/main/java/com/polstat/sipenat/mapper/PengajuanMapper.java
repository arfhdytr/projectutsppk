package com.polstat.sipenat.mapper;

import com.polstat.sipenat.dto.PengajuanDto;
import com.polstat.sipenat.entity.Pengajuan;

public class PengajuanMapper {

    public static PengajuanDto toDto(Pengajuan pengajuan) {
        if (pengajuan == null) return null;

        return PengajuanDto.builder()
                .id(pengajuan.getId())
                .nomorPengajuan(pengajuan.getNomorPengajuan())
                .jenisBantuan(pengajuan.getJenisBantuan())
                .jumlahDana(pengajuan.getJumlahDana())
                .keperluan(pengajuan.getKeperluan())
                .deskripsi(pengajuan.getDeskripsi())
                .status(pengajuan.getStatus())
                .tanggalPengajuan(pengajuan.getTanggalPengajuan())
                .filePendukung(pengajuan.getFilePendukung())
                .userId(pengajuan.getUser().getId())
                .userName(pengajuan.getUser().getFullName())
                .userEmail(pengajuan.getUser().getEmail())
                .createdAt(pengajuan.getCreatedAt())
                .updatedAt(pengajuan.getUpdatedAt())
                .build();
    }

    public static Pengajuan toEntity(PengajuanDto dto) {
        if (dto == null) return null;

        return Pengajuan.builder()
                .id(dto.getId())
                .nomorPengajuan(dto.getNomorPengajuan())
                .jenisBantuan(dto.getJenisBantuan())
                .jumlahDana(dto.getJumlahDana())
                .keperluan(dto.getKeperluan())
                .deskripsi(dto.getDeskripsi())
                .status(dto.getStatus())
                .tanggalPengajuan(dto.getTanggalPengajuan())
                .filePendukung(dto.getFilePendukung())
                .build();
    }
}