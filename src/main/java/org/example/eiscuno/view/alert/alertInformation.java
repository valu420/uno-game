package org.example.eiscuno.view.alert;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying informational alert dialogs in a JavaFX application.
 */
public class alertInformation{

    /**
     * Creates and displays an informational alert dialog with the given message.
     *
     * @param message The message to display in the alert dialog.
     */
    public static void createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}