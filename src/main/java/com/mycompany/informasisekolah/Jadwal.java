package com.mycompany.informasisekolah;

import java.io.Serializable;

/** Satu baris jadwal pelajaran (hari, jam, mapel, kelas, guru pengajar). */
public class Jadwal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String hari;
    private String jam;
    private String mapel;
    private String kelas;
    private String guru;

    public Jadwal(String hari, String jam, String mapel, String kelas, String guru) {
        this.hari = hari;
        this.jam = jam;
        this.mapel = mapel;
        this.kelas = kelas;
        this.guru = guru;
    }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public String getJam() { return jam; }
    public void setJam(String jam) { this.jam = jam; }

    public String getMapel() { return mapel; }
    public void setMapel(String mapel) { this.mapel = mapel; }

    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    public String getGuru() { return guru; }
    public void setGuru(String guru) { this.guru = guru; }
}
