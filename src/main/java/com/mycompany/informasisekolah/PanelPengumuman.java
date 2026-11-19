package com.mycompany.informasisekolah;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Tab 3: papan pengumuman sekolah. */
public class PanelPengumuman extends BorderPane {

    private static final String FILE_PENGUMUMAN = "data_pengumuman.dat";

    private final Sekolah sekolah;
    private final Runnable onDataBerubah;
    private final ObservableList<Pengumuman> dataTabel = FXCollections.observableArrayList();

    private TextField tfJudul;
    private TextArea taIsi, taBaca;
    private TableView<Pengumuman> tabel;
    private Label lblStatus;

    public PanelPengumuman(Sekolah sekolah, Runnable onDataBerubah) {
        this.sekolah = sekolah;
        this.onDataBerubah = onDataBerubah;
        dataTabel.setAll(sekolah.getDaftarPengumuman());

        tfJudul = new TextField();
        tfJudul.setPromptText("Judul pengumuman");
        taIsi = new TextArea();
        taIsi.setPromptText("Isi pengumuman...");
        taIsi.setWrapText(true);
        taIsi.setPrefRowCount(6);

        Button btnTambah = new Button("Tambah");
        Button btnHapus = new Button("Hapus");
        lblStatus = new Label("Siap.");

        VBox panelKiri = new VBox(12, new Label("Buat Pengumuman"), new Label("Judul"), tfJudul,
                new Label("Isi"), taIsi, new HBox(8, btnTambah, btnHapus), lblStatus);
        panelKiri.setPadding(new Insets(15));
        panelKiri.setPrefWidth(330);

        tabel = new TableView<>(dataTabel);
        TableColumn<Pengumuman, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTanggal()));
        TableColumn<Pengumuman, String> colJudul = new TableColumn<>("Judul");
        colJudul.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJudul()));
        tabel.getColumns().addAll(colTgl, colJudul);
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        taBaca = new TextArea("Pilih pengumuman untuk membaca isinya.");
        taBaca.setEditable(false);
        taBaca.setWrapText(true);
        taBaca.setPrefRowCount(6);

        VBox panelKanan = new VBox(10, new Label("Papan Pengumuman"), tabel, new Label("Isi Pengumuman"), taBaca);
        panelKanan.setPadding(new Insets(15));

        setLeft(panelKiri);
        setCenter(panelKanan);

        btnTambah.setOnAction(e -> tambahData());
        btnHapus.setOnAction(e -> hapusData());
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) taBaca.setText(baru.getIsi());
        });
    }

    private void tambahData() {
        String judul = tfJudul.getText().trim();
        String isi = taIsi.getText().trim();
        if (judul.isEmpty() || isi.isEmpty()) {
            Notifikasi.tampil(AlertType.WARNING, "Data Belum Lengkap", "Judul dan isi pengumuman wajib diisi!");
            return;
        }
        String tanggal = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        sekolah.getDaftarPengumuman().add(0, new Pengumuman(tanggal, judul, isi));  // terbaru di atas
        simpanDanSegarkan();
        tfJudul.clear();
        taIsi.clear();
        lblStatus.setText("Pengumuman \"" + judul + "\" diterbitkan.");
    }

    private void hapusData() {
        Pengumuman terpilih = tabel.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            Notifikasi.tampil(AlertType.INFORMATION, "Pilih Data", "Klik salah satu pengumuman terlebih dahulu.");
            return;
        }
        if (Notifikasi.konfirmasi("Hapus pengumuman \"" + terpilih.getJudul() + "\"?")) {
            sekolah.getDaftarPengumuman().remove(terpilih);
            simpanDanSegarkan();
            taBaca.setText("Pilih pengumuman untuk membaca isinya.");
            lblStatus.setText("Pengumuman dihapus.");
        }
    }

    private void simpanDanSegarkan() {
        sekolah.simpanPengumuman(FILE_PENGUMUMAN);
        dataTabel.setAll(sekolah.getDaftarPengumuman());
        onDataBerubah.run();
    }
}
