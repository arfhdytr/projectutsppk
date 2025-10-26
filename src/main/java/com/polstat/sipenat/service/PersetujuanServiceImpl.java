package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.PersetujuanDto;
import com.polstat.sipenat.entity.Pengajuan;
import com.polstat.sipenat.entity.Persetujuan;
import com.polstat.sipenat.entity.StatusPengajuan;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.entity.Role;
import com.polstat.sipenat.exception.BadRequestException;
import com.polstat.sipenat.exception.ForbiddenException;
import com.polstat.sipenat.exception.ResourceNotFoundException;
import com.polstat.sipenat.mapper.PersetujuanMapper;
import com.polstat.sipenat.repository.PengajuanRepository;
import com.polstat.sipenat.repository.PersetujuanRepository;
import com.polstat.sipenat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersetujuanServiceImpl implements PersetujuanService {

    @Autowired
    private PersetujuanRepository persetujuanRepository;

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PersetujuanDto prosesPengajuan(Long pengajuanId, String petugasEmail, PersetujuanDto persetujuanDto) {
        // Get petugas user
        User petugas = userRepository.findByEmail(petugasEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Petugas keuangan tidak ditemukan"));

        // Check role
        if (petugas.getRole() != Role.PETUGAS_KEUANGAN) {
            throw new ForbiddenException("Hanya petugas keuangan yang dapat memproses persetujuan");
        }

        // Get pengajuan
        Pengajuan pengajuan = pengajuanRepository.findById(pengajuanId)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check status - hanya DIVERIFIKASI yang bisa diproses
        if (pengajuan.getStatus() != StatusPengajuan.DIVERIFIKASI) {
            throw new BadRequestException("Hanya pengajuan dengan status DIVERIFIKASI yang dapat diproses");
        }

        // Check if already processed
        if (persetujuanRepository.findByPengajuan(pengajuan).isPresent()) {
            throw new BadRequestException("Pengajuan sudah diproses sebelumnya");
        }

        // Create persetujuan
        Persetujuan persetujuan = Persetujuan.builder()
                .pengajuan(pengajuan)
                .petugas(petugas)
                .keputusan(persetujuanDto.getKeputusan())
                .catatanKeputusan(persetujuanDto.getCatatanKeputusan())
                .build();

        Persetujuan savedPersetujuan = persetujuanRepository.save(persetujuan);

        // Update status pengajuan
        if (persetujuanDto.getKeputusan()) {
            // Disetujui
            pengajuan.setStatus(StatusPengajuan.DISETUJUI);
        } else {
            // Ditolak
            pengajuan.setStatus(StatusPengajuan.DITOLAK);
        }
        pengajuanRepository.save(pengajuan);

        return PersetujuanMapper.toDto(savedPersetujuan);
    }

    @Override
    public PersetujuanDto getPersetujuanByPengajuanId(Long pengajuanId) {
        Pengajuan pengajuan = pengajuanRepository.findById(pengajuanId)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        Persetujuan persetujuan = persetujuanRepository.findByPengajuan(pengajuan)
                .orElseThrow(() -> new ResourceNotFoundException("Persetujuan tidak ditemukan"));

        return PersetujuanMapper.toDto(persetujuan);
    }
}