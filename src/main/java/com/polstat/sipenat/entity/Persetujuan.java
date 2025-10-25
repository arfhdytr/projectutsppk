package com.polstat.sipenat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "persetujuan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persetujuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengajuan_id", nullable = false)
    private Pengajuan pengajuan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petugas_id", nullable = false)
    private User petugas;

    @Column(nullable = false)
    private Boolean keputusan; // true = disetujui, false = ditolak

    @Column(columnDefinition = "TEXT")
    private String catatanKeputusan;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime tanggalKeputusan;
}