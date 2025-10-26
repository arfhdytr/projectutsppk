package com.polstat.sipenat.service;

import com.polstat.sipenat.dto.PengajuanDto;
import com.polstat.sipenat.entity.Pengajuan;
import com.polstat.sipenat.entity.StatusPengajuan;
import com.polstat.sipenat.entity.User;
import com.polstat.sipenat.exception.BadRequestException;
import com.polstat.sipenat.exception.ForbiddenException;
import com.polstat.sipenat.exception.ResourceNotFoundException;
import com.polstat.sipenat.mapper.PengajuanMapper;
import com.polstat.sipenat.repository.PengajuanRepository;
import com.polstat.sipenat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PengajuanServiceImpl implements PengajuanService {

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PengajuanDto createPengajuan(String email, PengajuanDto pengajuanDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));

        // Generate nomor pengajuan
        String nomorPengajuan = generateNomorPengajuan();

        // Create pengajuan
        Pengajuan pengajuan = Pengajuan.builder()
                .nomorPengajuan(nomorPengajuan)
                .user(user)
                .jenisBantuan(pengajuanDto.getJenisBantuan())
                .jumlahDana(pengajuanDto.getJumlahDana())
                .keperluan(pengajuanDto.getKeperluan())
                .deskripsi(pengajuanDto.getDeskripsi())
                .status(StatusPengajuan.DRAFT)
                .tanggalPengajuan(LocalDateTime.now())
                .build();

        Pengajuan savedPengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.toDto(savedPengajuan);
    }

    @Override
    public PengajuanDto getPengajuanById(Long id, String email) {
        Pengajuan pengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check authorization
        if (!pengajuan.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("Anda tidak memiliki akses ke pengajuan ini");
        }

        return PengajuanMapper.toDto(pengajuan);
    }

    @Override
    public List<PengajuanDto> getMyPengajuan(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));

        List<Pengajuan> pengajuanList = pengajuanRepository.findByUser(user);
        return pengajuanList.stream()
                .map(PengajuanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PengajuanDto> getPengajuanByStatus(StatusPengajuan status) {
        List<Pengajuan> pengajuanList = pengajuanRepository.findByStatus(status);
        return pengajuanList.stream()
                .map(PengajuanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PengajuanDto updatePengajuan(Long id, String email, PengajuanDto pengajuanDto) {
        Pengajuan pengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check authorization
        if (!pengajuan.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("Anda tidak memiliki akses ke pengajuan ini");
        }

        // Check status - only DRAFT can be updated
        if (pengajuan.getStatus() != StatusPengajuan.DRAFT) {
            throw new BadRequestException("Hanya pengajuan dengan status DRAFT yang dapat diubah");
        }

        // Update fields
        pengajuan.setJenisBantuan(pengajuanDto.getJenisBantuan());
        pengajuan.setJumlahDana(pengajuanDto.getJumlahDana());
        pengajuan.setKeperluan(pengajuanDto.getKeperluan());
        pengajuan.setDeskripsi(pengajuanDto.getDeskripsi());

        Pengajuan updatedPengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.toDto(updatedPengajuan);
    }

    @Override
    @Transactional
    public PengajuanDto submitPengajuan(Long id, String email) {
        Pengajuan pengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check authorization
        if (!pengajuan.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("Anda tidak memiliki akses ke pengajuan ini");
        }

        // Check status - only DRAFT can be submitted
        if (pengajuan.getStatus() != StatusPengajuan.DRAFT) {
            throw new BadRequestException("Hanya pengajuan dengan status DRAFT yang dapat diajukan");
        }

        // Update status
        pengajuan.setStatus(StatusPengajuan.DIAJUKAN);
        pengajuan.setTanggalPengajuan(LocalDateTime.now());

        Pengajuan submittedPengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.toDto(submittedPengajuan);
    }

    @Override
    @Transactional
    public void deletePengajuan(Long id, String email) {
        Pengajuan pengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengajuan tidak ditemukan"));

        // Check authorization
        if (!pengajuan.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("Anda tidak memiliki akses ke pengajuan ini");
        }

        // Check status - only DRAFT can be deleted
        if (pengajuan.getStatus() != StatusPengajuan.DRAFT) {
            throw new BadRequestException("Hanya pengajuan dengan status DRAFT yang dapat dihapus");
        }

        pengajuanRepository.delete(pengajuan);
    }

    /**
     * Generate nomor pengajuan format: SIPENAT/YYYY/NNN
     */
    private String generateNomorPengajuan() {
        int year = Year.now().getValue();
        long count = pengajuanRepository.count() + 1;
        return String.format("SIPENAT/%d/%03d", year, count);
    }
}