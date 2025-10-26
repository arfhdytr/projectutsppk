package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.VerifikasiDto;
import com.polstat.sipenat.entity.Pengajuan;
import com.polstat.sipenat.entity.StatusPengajuan;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.entity.Verifikasi;
import com.polstat.sipenat.entity.Role;
import com.polstat.sipenat.exception.BadRequestException;
import com.polstat.sipenat.exception.ForbiddenException;
import com.polstat.sipenat.exception.ResourceNotFoundException;
import com.polstat.sipenat.mapper.VerifikasiMapper;
import com.polstat.sipenat.repository.PengajuanRepository;
import com.polstat.sipenat.repository.UserRepository;
import com.polstat.sipenat.repository.VerifikasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerifikasiServiceImpl implements VerifikasiService {

    @Autowired
    private VerifikasiRepository verifikasiRepository;

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public VerifikasiDto verifikasiPengajuan(Long pengajuanId, String adminEmail, VerifikasiDto verifikasiDto) {
        // Get admin user
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin tidak ditemukan"));

        // Check role
        if (admin.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Hanya admin yang dapat melakukan verifikasi");
        }

        // Get pengajuan
        Pengajuan pengajuan = pengajuanRepository.findById(pengajuanId)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check status - hanya DIAJUKAN yang bisa diverifikasi
        if (pengajuan.getStatus() != StatusPengajuan.DIAJUKAN) {
            throw new BadRequestException("Hanya pengajuan dengan status DIAJUKAN yang dapat diverifikasi");
        }

        // Check if already verified
        if (verifikasiRepository.findByPengajuan(pengajuan).isPresent()) {
            throw new BadRequestException("Pengajuan sudah diverifikasi sebelumnya");
        }

        // Create verifikasi
        Verifikasi verifikasi = Verifikasi.builder()
                .pengajuan(pengajuan)
                .admin(admin)
                .statusVerifikasi(verifikasiDto.getStatusVerifikasi())
                .catatanVerifikasi(verifikasiDto.getCatatanVerifikasi())
                .build();

        Verifikasi savedVerifikasi = verifikasiRepository.save(verifikasi);

        // Update status pengajuan
        if (verifikasiDto.getStatusVerifikasi()) {
            // Lolos verifikasi
            pengajuan.setStatus(StatusPengajuan.DIVERIFIKASI);
        } else {
            // Tidak lolos verifikasi
            pengajuan.setStatus(StatusPengajuan.DITOLAK);
        }
        pengajuanRepository.save(pengajuan);

        return VerifikasiMapper.toDto(savedVerifikasi);
    }

    @Override
    public VerifikasiDto getVerifikasiByPengajuanId(Long pengajuanId) {
        Pengajuan pengajuan = pengajuanRepository.findById(pengajuanId)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        Verifikasi verifikasi = verifikasiRepository.findByPengajuan(pengajuan)
                .orElseThrow(() -> new ResourceNotFoundException("Verifikasi tidak ditemukan"));

        return VerifikasiMapper.toDto(verifikasi);
    }
}