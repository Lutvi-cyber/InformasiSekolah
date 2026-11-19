package com.mycompany.informasisekolah;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/** Helper pop-up Alert supaya tidak ditulis berulang di setiap panel. */
public class Notifikasi {

    public static void tampil(AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    public static boolean konfirmasi(String pesan) {
        Alert a = new Alert(AlertType.CONFIRMATION, pesan, ButtonType.YES, ButtonType.NO);
        a.setTitle("Konfirmasi");
        a.setHeaderText(null);
        Optional<ButtonType> hasil = a.showAndWait();
        return hasil.isPresent() && hasil.get() == ButtonType.YES;
    }
}
