package org.example.eiscuno.view.alert;

import javafx.scene.control.Alert;

public class alertInformation{
    public static void createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
