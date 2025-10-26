package com.polstat.sipenat.mapper;

import com.polstat.sipenat.dto.PersetujuanDto;
import com.polstat.sipenat.entity.Persetujuan;

public class PersetujuanMapper {

    public static PersetujuanDto toDto(Persetujuan persetujuan) {
        if (persetujuan == null) return null;

        return PersetujuanDto.builder()
                .id(persetujuan.getId())
                .pengajuanId(persetujuan.getPengajuan().getId())
                .nomorPengajuan(persetujuan.getPengajuan().getNomorPengajuan())
                .keputusan(persetujuan.getKeputusan())
                .catatanKeputusan(persetujuan.getCatatanKeputusan())
                .petugasId(persetujuan.getPetugas().getId())
                .petugasName(persetujuan.getPetugas().getFullName())
                .tanggalKeputusan(persetujuan.getTanggalKeputusan())
                .build();
    }
}