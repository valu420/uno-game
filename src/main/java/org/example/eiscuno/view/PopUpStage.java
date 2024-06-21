package org.example.eiscuno.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.view.alert.alertInformation;

import java.io.IOException;

/**
 * This class represents a custom pop-up stage for selecting a color for a card game.
 * It extends Stage and creates a modal utility stage to choose and display color options.
 */
public class PopUpStage extends Stage {

    /**
     * Constructor for PopUpStage.
     * @param card The card object whose color is to be changed based on user selection.
     * @throws IOException If an I/O exception occurs while creating the stage.
     */
    private GridPane gridPane;
    public PopUpStage(Card card) throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        popupStage.setTitle("Ventana emergente");

        // Components for selecting color
        Label label = new Label("Escoge el color al que quieres cambiar la partida");
        Button BLUE = new Button("AZUL");
        Button RED = new Button("ROJO");
        Button YELLOW = new Button("AMARILLO");
        Button GREEN = new Button("VERDE");

        // Setting background colors for buttons
        BLUE.setStyle("-fx-background-color: blue;");
        RED.setStyle("-fx-background-color: red;");
        YELLOW.setStyle("-fx-background-color: yellow;");
        GREEN.setStyle("-fx-background-color: green;");

        gridPane = new GridPane();

        // Event handlers for color buttons
        BLUE.setOnAction(e -> {
            card.setColor("BLUE");
            gridPane.getChildren().clear();
            Label newContent = new Label("El color de la partida ha sido cambiado a AZUL");
            newContent.setAlignment(Pos.CENTER);
            gridPane.add(newContent, 0, 0);
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a AZUL");
        });

        RED.setOnAction(e -> {
            card.setColor("RED");
            gridPane.getChildren().clear();
            Label newContent = new Label("El color de la partida ha sido cambiado a ROJO");
            newContent.setAlignment(Pos.CENTER);
            gridPane.add(newContent, 0, 0);
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a ROJO");
        });

        YELLOW.setOnAction(e -> {
            card.setColor("YELLOW");
            gridPane.getChildren().clear();
            Label newContent = new Label("El color de la partida ha sido cambiado a AMARILLO");
            newContent.setAlignment(Pos.CENTER);
            gridPane.add(newContent, 0, 0);
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a AMARILLO");
        });

        GREEN.setOnAction(e -> {
            card.setColor("GREEN");
            gridPane.getChildren().clear();
            Label newContent = new Label("El color de la partida ha sido cambiado a VERDE");
            newContent.setAlignment(Pos.CENTER);
            gridPane.add(newContent, 0, 0);
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a VERDE");
        });

        // Setting up grid layout for the stage
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(label, 0, 0, 4, 1);
        gridPane.add(BLUE, 0, 1);
        gridPane.add(RED, 1, 1);
        gridPane.add(YELLOW, 2, 1);
        gridPane.add(GREEN, 3, 1);

        Scene scene = new Scene(gridPane, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

//    private void createAlert(String text){
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Información");
//            alert.setHeaderText(null);
//            alert.setContentText(text);
//            alert.showAndWait();
//        });
//    }
}