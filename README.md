# Informasi Sekolah - SMA SUZURAN

Aplikasi GUI JavaFX untuk mengelola data sekolah (Mata Kuliah PBO).

## Fitur
- **Siswa & Guru**: tambah, ubah, hapus, cari (ID/nama/kelas/mapel), filter Semua/Siswa/Guru, panel detail
- **Jadwal Pelajaran**: input jadwal (hari, jam, mapel, kelas, guru dipilih dari data guru), filter per hari, deteksi jadwal bentrok
- **Pengumuman**: papan pengumuman dengan tanggal otomatis
- **Statistik** di header: jumlah siswa, guru, jadwal, pengumuman
- Validasi dengan Alert: kolom kosong, ID harus angka (try-catch), ID duplikat, format jam, jadwal bentrok
- Data tersimpan otomatis (serialization): `data_sekolah.dat`, `data_jadwal.dat`, `data_pengumuman.dat`

## Konsep OOP
Encapsulation (getter/setter), Inheritance (`Orang` -> `Siswa`, `Guru`), Polymorphism (override `getPeran()`, `getDetail()`, `setDetail()`), komposisi (`Sekolah` memiliki daftar `Orang`, `Jadwal`, `Pengumuman`).

## Cara menjalankan
Buka di NetBeans, jalankan `App.java`, atau `mvn clean javafx:run`.
Versi terminal (Tugas 4): `MainKonsol.java`.
