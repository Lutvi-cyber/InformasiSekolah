package com.mycompany.informasisekolah;

/** Subclass dari Orang: menambah atribut kelas. */
public class Siswa extends Orang {
    private static final long serialVersionUID = 1L;

    private String kelas;

    public Siswa(String id, String nama, String kelas) {
        super(id, nama);
        this.kelas = kelas;
    }

    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    @Override
    public String getPeran() {
        return "Siswa";
    }

    @Override
    public String getDetail() {
        return "Kelas " + kelas;
    }

    @Override
    public void setDetail(String detail) {
        this.kelas = detail;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("           Kelas: " + kelas);
    }
}
