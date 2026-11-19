package com.mycompany.informasisekolah;

/** Subclass dari Orang: menambah atribut mata pelajaran. */
public class Guru extends Orang {
    private static final long serialVersionUID = 1L;

    private String mataPelajaran;

    public Guru(String id, String nama, String mataPelajaran) {
        super(id, nama);
        this.mataPelajaran = mataPelajaran;
    }

    public String getMataPelajaran() { return mataPelajaran; }
    public void setMataPelajaran(String mataPelajaran) { this.mataPelajaran = mataPelajaran; }

    @Override
    public String getPeran() {
        return "Guru";
    }

    @Override
    public String getDetail() {
        return mataPelajaran;
    }

    @Override
    public void setDetail(String detail) {
        this.mataPelajaran = detail;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("           Mata Pelajaran: " + mataPelajaran);
    }
}
