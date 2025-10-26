package com.polstat.sipenat.repository;

import com.polstat.sipenat.entity.Persetujuan;
import com.polstat.sipenat.entity.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersetujuanRepository extends JpaRepository<Persetujuan, Long> {
    Optional<Persetujuan> findByPengajuan(Pengajuan pengajuan);
}