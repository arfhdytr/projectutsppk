package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.PersetujuanDto;

public interface PersetujuanService {
    PersetujuanDto prosesPengajuan(Long pengajuanId, String petugasEmail, PersetujuanDto persetujuanDto);
    PersetujuanDto getPersetujuanByPengajuanId(Long pengajuanId);
}