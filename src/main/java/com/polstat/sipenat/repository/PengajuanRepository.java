package com.polstat.sipenat.repository;

import com.polstat.sipenat.entity.Pengajuan;
import com.polstat.sipenat.entity.StatusPengajuan;
import com.polstat.sipenat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, Long> {
    List<Pengajuan> findByUser(User user);
    List<Pengajuan> findByStatus(StatusPengajuan status);
    List<Pengajuan> findByUserAndStatus(User user, StatusPengajuan status);
}