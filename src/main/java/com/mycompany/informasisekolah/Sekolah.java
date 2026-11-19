package com.mycompany.informasisekolah;

import java.io.*;
import java.util.ArrayList;

/** Mengelola daftar siswa dan guru, serta menyimpan/memuat data dari file. */
public class Sekolah {
    private String namaSekolah;
    private ArrayList<Orang> daftarOrang = new ArrayList<>();
    private ArrayList<Jadwal> daftarJadwal = new ArrayList<>();
    private ArrayList<Pengumuman> daftarPengumuman = new ArrayList<>();

    public Sekolah(String namaSekolah) {
        this.namaSekolah = namaSekolah;
    }

    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }

    public ArrayList<Orang> getDaftarOrang() { return daftarOrang; }
    public ArrayList<Jadwal> getDaftarJadwal() { return daftarJadwal; }
    public ArrayList<Pengumuman> getDaftarPengumuman() { return daftarPengumuman; }

    /** Statistik: hitung jumlah siswa / guru (polymorphism lewat getPeran()). */
    public int hitungPeran(String peran) {
        int total = 0;
        for (Orang o : daftarOrang) {
            if (o.getPeran().equals(peran)) total++;
        }
        return total;
    }

    /** Daftar nama guru, dipakai untuk pilihan di jadwal. */
    public ArrayList<String> getNamaGuru() {
        ArrayList<String> hasil = new ArrayList<>();
        for (Orang o : daftarOrang) {
            if (o instanceof Guru) hasil.add(o.getNama());
        }
        return hasil;
    }

    public boolean idSudahAda(String id) {
        for (Orang o : daftarOrang) {
            if (o.getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    public void tambah(Orang orang) {
        daftarOrang.add(orang);
    }

    public boolean hapus(String id) {
        for (int i = 0; i < daftarOrang.size(); i++) {
            if (daftarOrang.get(i).getId().equalsIgnoreCase(id)) {
                daftarOrang.remove(i);
                return true;
            }
        }
        return false;
    }

    public void tampilkanSemua() {
        if (daftarOrang.isEmpty()) {
            System.out.println("Belum ada data.");
            return;
        }
        for (Orang o : daftarOrang) {
            o.tampilkanInfo();
        }
        System.out.println("Total: " + daftarOrang.size() + " orang");
    }

    /** Serialization: simpan seluruh daftar objek ke file. */
    public void simpanKeFile(String namaFile) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(namaFile))) {
            out.writeObject(daftarOrang);
            System.out.println("Data tersimpan ke " + namaFile);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    /** Deserialization: muat daftar objek dari file (jika ada). */
    @SuppressWarnings("unchecked")
    public void muatDariFile(String namaFile) {
        File f = new File(namaFile);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            daftarOrang = (ArrayList<Orang>) in.readObject();
            System.out.println("Data dimuat dari " + namaFile + " (" + daftarOrang.size() + " data)");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Gagal memuat data: " + e.getMessage());
        }
    }

    // ===== Simpan/muat jadwal & pengumuman (file terpisah) =====
    public void simpanJadwal(String namaFile) { simpanList(namaFile, daftarJadwal); }
    public void simpanPengumuman(String namaFile) { simpanList(namaFile, daftarPengumuman); }

    public void muatJadwal(String namaFile) {
        ArrayList<Jadwal> hasil = muatList(namaFile);
        if (hasil != null) daftarJadwal = hasil;
    }

    public void muatPengumuman(String namaFile) {
        ArrayList<Pengumuman> hasil = muatList(namaFile);
        if (hasil != null) daftarPengumuman = hasil;
    }

    private <T> void simpanList(String namaFile, ArrayList<T> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(namaFile))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan " + namaFile + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> muatList(String namaFile) {
        File f = new File(namaFile);
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Gagal memuat " + namaFile + ": " + e.getMessage());
            return null;
        }
    }
}
