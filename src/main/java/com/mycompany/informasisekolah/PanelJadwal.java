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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Tab 2: jadwal pelajaran (pilihan guru diambil dari data guru di Tab 1). */
public class PanelJadwal extends BorderPane {

    private static final String FILE_JADWAL = "data_jadwal.dat";
    private static final String[] HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};

    private final Sekolah sekolah;
    private final Runnable onDataBerubah;
    private final ObservableList<Jadwal> dataTabel = FXCollections.observableArrayList();
    private final FilteredList<Jadwal> dataTersaring = new FilteredList<>(dataTabel, j -> true);

    private ComboBox<String> cbHari, cbGuru, cbFilterHari;
    private TextField tfJam, tfMapel, tfKelas;
    private Label lblStatus;
    private TableView<Jadwal> tabel;

    public PanelJadwal(Sekolah sekolah, Runnable onDataBerubah) {
        this.sekolah = sekolah;
        this.onDataBerubah = onDataBerubah;
        dataTabel.setAll(sekolah.getDaftarJadwal());

        cbHari = new ComboBox<>(FXCollections.observableArrayList(HARI));
        cbHari.setValue("Senin");
        cbHari.setMaxWidth(Double.MAX_VALUE);
        tfJam = new TextField();
        tfJam.setPromptText("Contoh: 07.00 - 08.30");
        tfMapel = new TextField();
        tfMapel.setPromptText("Contoh: Matematika");
        tfKelas = new TextField();
        tfKelas.setPromptText("Contoh: XII IPA 1");
        cbGuru = new ComboBox<>();
        cbGuru.setPromptText("Pilih guru");
        cbGuru.setMaxWidth(Double.MAX_VALUE);
        segarkanGuru();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Hari"), 0, 0);
        form.add(cbHari, 1, 0);
        form.add(new Label("Jam"), 0, 1);
        form.add(tfJam, 1, 1);
        form.add(new Label("Mata Pelajaran"), 0, 2);
        form.add(tfMapel, 1, 2);
        form.add(new Label("Kelas"), 0, 3);
        form.add(tfKelas, 1, 3);
        form.add(new Label("Guru"), 0, 4);
        form.add(cbGuru, 1, 4);

        Button btnTambah = new Button("Tambah");
        Button btnHapus = new Button("Hapus");
        Button btnReset = new Button("Reset Form");
        lblStatus = new Label("Siap.");
        lblStatus.setWrapText(true);

        VBox panelKiri = new VBox(15, new Label("Form Jadwal Pelajaran"), form,
                new HBox(8, btnTambah, btnHapus, btnReset), lblStatus);
        panelKiri.setPadding(new Insets(15));
        panelKiri.setPrefWidth(330);

        cbFilterHari = new ComboBox<>(FXCollections.observableArrayList("Semua Hari", "Senin", "Selasa",
                "Rabu", "Kamis", "Jumat", "Sabtu"));
        cbFilterHari.setValue("Semua Hari");

        tabel = new TableView<>(dataTersaring);
        tabel.getColumns().add(kolom("Hari", j -> j.getHari()));
        tabel.getColumns().add(kolom("Jam", j -> j.getJam()));
        tabel.getColumns().add(kolom("Mata Pelajaran", j -> j.getMapel()));
        tabel.getColumns().add(kolom("Kelas", j -> j.getKelas()));
        tabel.getColumns().add(kolom("Guru", j -> j.getGuru()));
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tabel, Priority.ALWAYS);

        VBox panelKanan = new VBox(10, new HBox(10, new Label("Jadwal Pelajaran"), cbFilterHari), tabel);
        panelKanan.setPadding(new Insets(15));

        setLeft(panelKiri);
        setCenter(panelKanan);

        btnTambah.setOnAction(e -> tambahData());
        btnHapus.setOnAction(e -> hapusData());
        btnReset.setOnAction(e -> resetForm());
        cbFilterHari.setOnAction(e -> {
            String h = cbFilterHari.getValue();
            dataTersaring.setPredicate(j -> "Semua Hari".equals(h) || j.getHari().equals(h));
        });
    }

    private TableColumn<Jadwal, String> kolom(String judul, java.util.function.Function<Jadwal, String> ambil) {
        TableColumn<Jadwal, String> col = new TableColumn<>(judul);
        col.setCellValueFactory(c -> new SimpleStringProperty(ambil.apply(c.getValue())));
        return col;
    }

    /** Dipanggil saat tab dibuka, supaya daftar guru selalu terbaru. */
    public void segarkanGuru() {
        String dipilih = cbGuru.getValue();
        cbGuru.setItems(FXCollections.observableArrayList(sekolah.getNamaGuru()));
        if (dipilih != null && cbGuru.getItems().contains(dipilih)) cbGuru.setValue(dipilih);
    }

    private void tambahData() {
        String jam = tfJam.getText().trim();
        String mapel = tfMapel.getText().trim();
        String kelas = tfKelas.getText().trim();
        String guru = cbGuru.getValue();
        String hari = cbHari.getValue();

        if (jam.isEmpty() || mapel.isEmpty() || kelas.isEmpty() || guru == null) {
            Notifikasi.tampil(AlertType.WARNING, "Data Belum Lengkap",
                    "Semua kolom wajib diisi. Jika daftar guru kosong, tambahkan guru dulu di tab Siswa & Guru.");
            return;
        }
        if (!jam.matches("\\d{2}[.:]\\d{2}\\s*-\\s*\\d{2}[.:]\\d{2}")) {
            Notifikasi.tampil(AlertType.ERROR, "Format Jam Salah", "Gunakan format 07.00 - 08.30");
            return;
        }
        for (Jadwal j : sekolah.getDaftarJadwal()) {
            if (j.getHari().equals(hari) && j.getJam().equals(jam) && j.getKelas().equalsIgnoreCase(kelas)) {
                Notifikasi.tampil(AlertType.ERROR, "Jadwal Bentrok",
                        "Kelas " + kelas + " sudah punya pelajaran pada " + hari + " jam " + jam + ".");
                return;
            }
        }
        sekolah.getDaftarJadwal().add(new Jadwal(hari, jam, mapel, kelas, guru));
        simpanDanSegarkan();
        lblStatus.setText("Jadwal " + mapel + " (" + hari + ") ditambahkan.");
        resetForm();
    }

    private void hapusData() {
        Jadwal terpilih = tabel.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            Notifikasi.tampil(AlertType.INFORMATION, "Pilih Data", "Klik salah satu baris jadwal terlebih dahulu.");
            return;
        }
        if (Notifikasi.konfirmasi("Hapus jadwal " + terpilih.getMapel() + " (" + terpilih.getHari() + ")?")) {
            sekolah.getDaftarJadwal().remove(terpilih);
            simpanDanSegarkan();
            lblStatus.setText("Jadwal dihapus.");
        }
    }

    private void resetForm() {
        cbHari.setValue("Senin");
        tfJam.clear();
        tfMapel.clear();
        tfKelas.clear();
        cbGuru.setValue(null);
    }

    private void simpanDanSegarkan() {
        sekolah.simpanJadwal(FILE_JADWAL);
        dataTabel.setAll(sekolah.getDaftarJadwal());
        onDataBerubah.run();
    }
}
