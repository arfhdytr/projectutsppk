# SIPENAT - Sistem Pengajuan Dana Bantuan Polstat STIS

RESTful API untuk sistem pengajuan dana bantuan mahasiswa Polstat STIS menggunakan Spring Boot.

## Tech Stack

- Java 17
- Spring Boot 3.1.2
- MySQL 8.0
- JWT Authentication
- Swagger/OpenAPI
- Maven

## Prerequisites

- JDK 17
- MySQL 8.0
- Maven 3.9+

## Setup & Installation

1. **Clone repository**
```bash
git clone https://git.stis.ac.id/your-repo/projectutsppk.git
cd projectutsppk
```

2. **Setup Database**
```sql
CREATE DATABASE sipenat;
```

3. **Configure Database**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sipenat
spring.datasource.username=root
spring.datasource.password=your_password
```

4. **Build & Run**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Server akan berjalan di `http://localhost:8080`

## API Documentation

Swagger UI: `http://localhost:8080/docs/swagger-ui`

OpenAPI JSON: `http://localhost:8080/docs/api-docs`

## Roles & Access

- **MAHASISWA**: Buat dan kelola pengajuan dana
- **ADMIN**: Verifikasi pengajuan
- **PETUGAS_KEUANGAN**: Approve/reject pengajuan

## Testing Flow

1. Register user (mahasiswa, admin, petugas keuangan)
2. Login → dapatkan JWT token
3. Authorize di Swagger UI dengan token
4. Test endpoint sesuai role

### Sample Test Account
```json
// Register via POST /api/auth/register
{
  "username": "mahasiswa01",
  "email": "mahasiswa@stis.ac.id",
  "password": "password123",
  "fullName": "John Doe",
  "nimNip": "222312001",
  "role": "MAHASISWA"
}
```

## Main Endpoints

### Authentication
- `POST /api/auth/register` - Registrasi
- `POST /api/auth/login` - Login (dapat JWT token)

### User Management
- `GET /api/users/profile` - Lihat profil
- `PUT /api/users/profile` - Update profil
- `PUT /api/users/change-password` - Ganti password
- `DELETE /api/users/account` - Hapus akun

### Pengajuan (Mahasiswa)
- `POST /api/pengajuan` - Buat pengajuan
- `GET /api/pengajuan/my-submissions` - Lihat pengajuan saya
- `PUT /api/pengajuan/{id}` - Edit pengajuan (DRAFT only)
- `PATCH /api/pengajuan/{id}/submit` - Submit pengajuan
- `DELETE /api/pengajuan/{id}` - Hapus pengajuan (DRAFT only)

### Verifikasi (Admin)
- `GET /api/pengajuan/status/DIAJUKAN` - Lihat pengajuan yang diajukan
- `POST /api/verifikasi/pengajuan/{id}` - Verifikasi pengajuan

### Persetujuan (Petugas Keuangan)
- `GET /api/pengajuan/status/DIVERIFIKASI` - Lihat pengajuan terverifikasi
- `POST /api/persetujuan/pengajuan/{id}` - Approve/reject pengajuan

## Status Pengajuan Flow
```
DRAFT → DIAJUKAN → DIVERIFIKASI → DISETUJUI
                 ↘              ↘
                   DITOLAK        DITOLAK
```

## Author

Arif Hidayat Ramadhan - 222312995 - 3SI1

---

*Tugas UTS Pemrograman Platform Khusus - Politeknik Statistika STIS 2025*