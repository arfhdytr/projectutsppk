package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.PengajuanDto;
import com.polstat.sipenat.entity.StatusPengajuan;

import java.util.List;

public interface PengajuanService {
    PengajuanDto createPengajuan(String email, PengajuanDto pengajuanDto);
    PengajuanDto getPengajuanById(Long id, String email);
    List<PengajuanDto> getMyPengajuan(String email);
    List<PengajuanDto> getPengajuanByStatus(StatusPengajuan status);
    PengajuanDto updatePengajuan(Long id, String email, PengajuanDto pengajuanDto);
    PengajuanDto submitPengajuan(Long id, String email);
    void deletePengajuan(Long id, String email);
}