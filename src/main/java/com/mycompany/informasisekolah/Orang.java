package com.mycompany.informasisekolah;

import java.io.Serializable;

/**
 * Superclass: data umum milik setiap orang di sekolah.
 * Implements Serializable supaya objeknya bisa disimpan ke file (Pertemuan 11).
 */
public class Orang implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;

    public Orang(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getPeran() {
        return "Orang";
    }

    /** Info tambahan (kelas / mata pelajaran), ditampilkan di tabel GUI. */
    public String getDetail() {
        return "-";
    }

    /** Ubah info tambahan (kelas / mata pelajaran). Di-override oleh subclass. */
    public void setDetail(String detail) { }

    public void tampilkanInfo() {
        System.out.println("[" + getPeran() + "] ID: " + id + " | Nama: " + nama);
    }
}
