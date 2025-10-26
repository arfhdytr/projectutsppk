package com.polstat.sipenat.repository;

import com.polstat.sipenat.entity.Verifikasi;
import com.polstat.sipenat.entity.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifikasiRepository extends JpaRepository<Verifikasi, Long> {
    Optional<Verifikasi> findByPengajuan(Pengajuan pengajuan);
}