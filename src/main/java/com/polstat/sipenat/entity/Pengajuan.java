package com.polstat.sipenat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pengajuan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pengajuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nomorPengajuan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisBantuan jenisBantuan;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal jumlahDana;

    @Column(nullable = false, length = 200)
    private String keperluan;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPengajuan status;

    @Column(nullable = false)
    private LocalDateTime tanggalPengajuan;

    @Column(length = 255)
    private String filePendukung;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
