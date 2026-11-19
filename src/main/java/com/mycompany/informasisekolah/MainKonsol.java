package com.mycompany.informasisekolah;

import java.util.Scanner;

public class MainKonsol {
    private static final String FILE_DATA = "data_sekolah.dat";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Sekolah sekolah = new Sekolah("SMA SUZURAN");   // instansiasi object
        sekolah.muatDariFile(FILE_DATA);

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n=== SISTEM INFORMASI " + sekolah.getNamaSekolah().toUpperCase() + " ===");
            System.out.println("1. Tambah Siswa");
            System.out.println("2. Tambah Guru");
            System.out.println("3. Tampilkan Semua Data");
            System.out.println("4. Hapus Data (berdasarkan ID)");
            System.out.println("5. Simpan & Keluar");
            System.out.print("Pilih menu: ");
            String pilihan = input.nextLine().trim();

            switch (pilihan) {
                case "1":
                    System.out.print("ID    : ");
                    String idS = input.nextLine();
                    if (sekolah.idSudahAda(idS)) {
                        System.out.println("ID sudah dipakai!");
                        break;
                    }
                    System.out.print("Nama  : ");
                    String namaS = input.nextLine();
                    System.out.print("Kelas : ");
                    String kelas = input.nextLine();
                    sekolah.tambah(new Siswa(idS, namaS, kelas));
                    System.out.println("Siswa berhasil ditambahkan.");
                    break;
                case "2":
                    System.out.print("ID             : ");
                    String idG = input.nextLine();
                    if (sekolah.idSudahAda(idG)) {
                        System.out.println("ID sudah dipakai!");
                        break;
                    }
                    System.out.print("Nama           : ");
                    String namaG = input.nextLine();
                    System.out.print("Mata Pelajaran : ");
                    String mapel = input.nextLine();
                    sekolah.tambah(new Guru(idG, namaG, mapel));
                    System.out.println("Guru berhasil ditambahkan.");
                    break;
                case "3":
                    sekolah.tampilkanSemua();
                    break;
                case "4":
                    System.out.print("ID yang dihapus: ");
                    if (sekolah.hapus(input.nextLine())) {
                        System.out.println("Data dihapus.");
                    } else {
                        System.out.println("ID tidak ditemukan.");
                    }
                    break;
                case "5":
                    sekolah.simpanKeFile(FILE_DATA);
                    jalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
        System.out.println("Terima kasih!");
        input.close();
    }
}
