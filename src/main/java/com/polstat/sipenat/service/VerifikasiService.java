package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.VerifikasiDto;

public interface VerifikasiService {
    VerifikasiDto verifikasiPengajuan(Long pengajuanId, String adminEmail, VerifikasiDto verifikasiDto);
    VerifikasiDto getVerifikasiByPengajuanId(Long pengajuanId);
}