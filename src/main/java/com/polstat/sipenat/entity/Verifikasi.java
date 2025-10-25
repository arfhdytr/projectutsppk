package com.polstat.sipenat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifikasi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verifikasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengajuan_id", nullable = false)
    private Pengajuan pengajuan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Column(columnDefinition = "TEXT")
    private String catatanVerifikasi;

    @Column(nullable = false)
    private Boolean statusVerifikasi; // true = lolos, false = tidak lolos

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime tanggalVerifikasi;
}