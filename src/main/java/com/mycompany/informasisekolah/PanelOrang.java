package com.mycompany.informasisekolah;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Tab 1: data siswa & guru (tambah, ubah, hapus, cari, filter, detail). */
public class PanelOrang extends BorderPane {

    private static final String FILE_DATA = "data_sekolah.dat";

    private final Sekolah sekolah;
    private final Runnable onDataBerubah;
    private final ObservableList<Orang> dataTabel = FXCollections.observableArrayList();
    private final FilteredList<Orang> dataTersaring = new FilteredList<>(dataTabel, o -> true);

    private ComboBox<String> cbJenis;
    private TextField tfId, tfNama, tfDetail, tfCari;
    private ComboBox<String> cbFilter;
    private Label lblDetail, lblStatus;
    private TextArea taInfo;
    private TableView<Orang> tabel;

    public PanelOrang(Sekolah sekolah, Runnable onDataBerubah) {
        this.sekolah = sekolah;
        this.onDataBerubah = onDataBerubah;
        dataTabel.setAll(sekolah.getDaftarOrang());

        // ===== Form (kiri) =====
        cbJenis = new ComboBox<>(FXCollections.observableArrayList("Siswa", "Guru"));
        cbJenis.setValue("Siswa");
        cbJenis.setMaxWidth(Double.MAX_VALUE);
        tfId = new TextField();
        tfId.setPromptText("Contoh: 1001 (angka)");
        tfNama = new TextField();
        tfNama.setPromptText("Nama lengkap");
        tfDetail = new TextField();
        tfDetail.setPromptText("Contoh: XII IPA 1");
        lblDetail = new Label("Kelas");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Jenis"), 0, 0);
        form.add(cbJenis, 1, 0);
        form.add(new Label("ID"), 0, 1);
        form.add(tfId, 1, 1);
        form.add(new Label("Nama"), 0, 2);
        form.add(tfNama, 1, 2);
        form.add(lblDetail, 0, 3);
        form.add(tfDetail, 1, 3);

        Button btnTambah = new Button("Tambah");
        Button btnUbah = new Button("Ubah");
        Button btnHapus = new Button("Hapus");
        Button btnReset = new Button("Reset Form");
        HBox boxTombol = new HBox(8, btnTambah, btnUbah, btnHapus, btnReset);

        lblStatus = new Label("Siap.");
        lblStatus.setWrapText(true);

        VBox panelKiri = new VBox(15, new Label("Form Input Data"), form, boxTombol, lblStatus);
        panelKiri.setPadding(new Insets(15));
        panelKiri.setPrefWidth(330);

        // ===== Pencarian + filter + tabel (kanan) =====
        tfCari = new TextField();
        tfCari.setPromptText("Cari ID / nama / kelas / mapel...");
        HBox.setHgrow(tfCari, Priority.ALWAYS);
        cbFilter = new ComboBox<>(FXCollections.observableArrayList("Semua", "Siswa", "Guru"));
        cbFilter.setValue("Semua");
        HBox barisCari = new HBox(8, tfCari, cbFilter);

        tabel = new TableView<>(dataTersaring);
        TableColumn<Orang, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));
        TableColumn<Orang, String> colPeran = new TableColumn<>("Peran");
        colPeran.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPeran()));
        TableColumn<Orang, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNama()));
        TableColumn<Orang, String> colDetail = new TableColumn<>("Detail");
        colDetail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDetail()));
        tabel.getColumns().addAll(colId, colPeran, colNama, colDetail);
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        taInfo = new TextArea("Pilih baris pada tabel untuk melihat detail.");
        taInfo.setEditable(false);
        taInfo.setPrefRowCount(4);

        VBox panelKanan = new VBox(10, new Label("Daftar Siswa & Guru"), barisCari, tabel,
                new Label("Informasi Detail"), taInfo);
        panelKanan.setPadding(new Insets(15));

        setLeft(panelKiri);
        setCenter(panelKanan);

        // ===== Event handling =====
        cbJenis.setOnAction(e -> updateLabelDetail());
        btnTambah.setOnAction(e -> tambahData());
        btnUbah.setOnAction(e -> ubahData());
        btnHapus.setOnAction(e -> hapusData());
        btnReset.setOnAction(e -> resetForm());
        tfCari.textProperty().addListener((obs, lama, baru) -> terapkanFilter());
        cbFilter.setOnAction(e -> terapkanFilter());
        tabel.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) isiFormDariData(baru);
        });
    }

    private void terapkanFilter() {
        String kata = tfCari.getText() == null ? "" : tfCari.getText().trim().toLowerCase();
        String jenis = cbFilter.getValue();
        dataTersaring.setPredicate(o -> {
            boolean cocokJenis = "Semua".equals(jenis) || o.getPeran().equals(jenis);
            boolean cocokKata = kata.isEmpty()
                    || o.getId().toLowerCase().contains(kata)
                    || o.getNama().toLowerCase().contains(kata)
                    || o.getDetail().toLowerCase().contains(kata);
            return cocokJenis && cocokKata;
        });
    }

    private void updateLabelDetail() {
        if ("Siswa".equals(cbJenis.getValue())) {
            lblDetail.setText("Kelas");
            tfDetail.setPromptText("Contoh: XII IPA 1");
        } else {
            lblDetail.setText("Mata Pelajaran");
            tfDetail.setPromptText("Contoh: Matematika");
        }
    }

    /** Klik baris tabel -> isi form (siap diubah) dan tampilkan detail. */
    private void isiFormDariData(Orang o) {
        cbJenis.setValue(o.getPeran());
        updateLabelDetail();
        tfId.setText(o.getId());
        tfNama.setText(o.getNama());
        if (o instanceof Siswa) {
            tfDetail.setText(((Siswa) o).getKelas());
        } else if (o instanceof Guru) {
            tfDetail.setText(((Guru) o).getMataPelajaran());
        }
        taInfo.setText("Peran   : " + o.getPeran() + "\nID      : " + o.getId()
                + "\nNama    : " + o.getNama() + "\nDetail  : " + o.getDetail());
    }

    private void tambahData() {
        String id = tfId.getText().trim();
        String nama = tfNama.getText().trim();
        String detail = tfDetail.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || detail.isEmpty()) {
            Notifikasi.tampil(AlertType.WARNING, "Data Belum Lengkap",
                    "Semua kolom wajib diisi sebelum menambah data!");
            return;
        }
        try {
            Long.parseLong(id);
        } catch (NumberFormatException ex) {
            Notifikasi.tampil(AlertType.ERROR, "Format ID Salah", "ID harus berupa angka! Contoh: 1001");
            return;
        }
        if (sekolah.idSudahAda(id)) {
            Notifikasi.tampil(AlertType.ERROR, "ID Duplikat", "ID " + id + " sudah dipakai. Gunakan ID lain.");
            return;
        }

        if ("Siswa".equals(cbJenis.getValue())) {
            sekolah.tambah(new Siswa(id, nama, detail));
        } else {
            sekolah.tambah(new Guru(id, nama, detail));
        }
        simpanDanSegarkan();
        lblStatus.setText(cbJenis.getValue() + " \"" + nama + "\" berhasil ditambahkan.");
        resetForm();
    }

    private void ubahData() {
        Orang terpilih = tabel.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            Notifikasi.tampil(AlertType.INFORMATION, "Pilih Data",
                    "Klik salah satu baris di tabel dulu, ubah isinya di form, lalu klik Ubah.");
            return;
        }
        String nama = tfNama.getText().trim();
        String detail = tfDetail.getText().trim();
        if (nama.isEmpty() || detail.isEmpty()) {
            Notifikasi.tampil(AlertType.WARNING, "Data Belum Lengkap", "Nama dan detail tidak boleh kosong!");
            return;
        }
        // ID & jenis tidak diubah; polymorphism: setDetail() beda tiap subclass
        terpilih.setNama(nama);
        terpilih.setDetail(detail);
        simpanDanSegarkan();
        lblStatus.setText("Data \"" + nama + "\" berhasil diubah.");
        resetForm();
    }

    private void hapusData() {
        Orang terpilih = tabel.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            Notifikasi.tampil(AlertType.INFORMATION, "Pilih Data", "Klik salah satu baris di tabel terlebih dahulu.");
            return;
        }
        if (Notifikasi.konfirmasi("Hapus data " + terpilih.getNama() + "?")) {
            sekolah.hapus(terpilih.getId());
            simpanDanSegarkan();
            lblStatus.setText("Data " + terpilih.getNama() + " dihapus.");
            resetForm();
        }
    }

    private void resetForm() {
        tabel.getSelectionModel().clearSelection();
        tfId.clear();
        tfNama.clear();
        tfDetail.clear();
        cbJenis.setValue("Siswa");
        updateLabelDetail();
        taInfo.setText("Pilih baris pada tabel untuk melihat detail.");
    }

    private void simpanDanSegarkan() {
        sekolah.simpanKeFile(FILE_DATA);
        dataTabel.setAll(sekolah.getDaftarOrang());
        tabel.refresh();
        onDataBerubah.run();
    }
}
