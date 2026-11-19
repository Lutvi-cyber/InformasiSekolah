package com.mycompany.informasisekolah;

import java.io.Serializable;

/** Pengumuman sekolah (tanggal diisi otomatis saat dibuat). */
public class Pengumuman implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tanggal;
    private String judul;
    private String isi;

    public Pengumuman(String tanggal, String judul, String isi) {
        this.tanggal = tanggal;
        this.judul = judul;
        this.isi = isi;
    }

    public String getTanggal() { return tanggal; }
    public String getJudul() { return judul; }
    public String getIsi() { return isi; }
}
