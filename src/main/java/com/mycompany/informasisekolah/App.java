package com.mycompany.informasisekolah;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Aplikasi GUI JavaFX - Sistem Informasi SMA SUZURAN.
 * Tahap 1: desain antarmuka (BorderPane, TabPane, GridPane, VBox, HBox)
 * Tahap 2: event handling (setOnAction)
 * Tahap 3: validasi & Alert (kolom kosong, format angka/jam, ID duplikat, jadwal bentrok)
 *
 * Fitur: data siswa & guru (tambah/ubah/hapus/cari/filter), jadwal pelajaran,
 * papan pengumuman, dan ringkasan statistik.
 */
public class App extends Application {

    private static final String FILE_DATA = "data_sekolah.dat";
    private static final String FILE_JADWAL = "data_jadwal.dat";
    private static final String FILE_PENGUMUMAN = "data_pengumuman.dat";

    private final Sekolah sekolah = new Sekolah("SMA SUZURAN");
    private Label lblStatistik;

    @Override
    public void start(Stage stage) {
        sekolah.muatDariFile(FILE_DATA);
        sekolah.muatJadwal(FILE_JADWAL);
        sekolah.muatPengumuman(FILE_PENGUMUMAN);

        // ===== Header: judul + statistik =====
        Label judul = new Label("SISTEM INFORMASI " + sekolah.getNamaSekolah().toUpperCase());
        judul.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        lblStatistik = new Label();
        lblStatistik.setStyle("-fx-text-fill: white;");
        VBox header = new VBox(4, judul, lblStatistik);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #1f6f9f;");
        perbaruiStatistik();

        // ===== Tab =====
        Runnable onBerubah = this::perbaruiStatistik;
        PanelOrang panelOrang = new PanelOrang(sekolah, onBerubah);
        PanelJadwal panelJadwal = new PanelJadwal(sekolah, onBerubah);
        PanelPengumuman panelPengumuman = new PanelPengumuman(sekolah, onBerubah);

        Tab tabOrang = new Tab("Siswa & Guru", panelOrang);
        Tab tabJadwal = new Tab("Jadwal Pelajaran", panelJadwal);
        Tab tabPengumuman = new Tab("Pengumuman", panelPengumuman);
        TabPane tabPane = new TabPane(tabOrang, tabJadwal, tabPengumuman);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabJadwal.setOnSelectionChanged(e -> {
            if (tabJadwal.isSelected()) panelJadwal.segarkanGuru();
        });

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(tabPane);

        stage.setTitle("Informasi " + sekolah.getNamaSekolah());
        stage.setScene(new Scene(root, 960, 600));
        stage.show();
    }

    private void perbaruiStatistik() {
        lblStatistik.setText("Siswa: " + sekolah.hitungPeran("Siswa")
                + "   |   Guru: " + sekolah.hitungPeran("Guru")
                + "   |   Jadwal: " + sekolah.getDaftarJadwal().size()
                + "   |   Pengumuman: " + sekolah.getDaftarPengumuman().size());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
