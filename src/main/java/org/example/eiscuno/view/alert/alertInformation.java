package org.example.eiscuno.view.alert;

import javafx.scene.control.Alert;

public class alertInformation{
    public static void createAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
