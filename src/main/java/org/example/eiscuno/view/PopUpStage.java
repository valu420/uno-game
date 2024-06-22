package org.example.eiscuno.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.view.alert.alertInformation;
import org.example.eiscuno.view.alert.alertInformation;

import java.io.IOException;

/**
 * This class represents a custom pop-up stage for selecting a color for a card game.
 * It extends Stage and creates a modal utility stage to choose and display color options.
 */
public class PopUpStage extends Stage {
    private GridPane gridPane;

    /**
     * Constructor for PopUpStage.
     * @param card The card object whose color is to be changed based on user selection.
     * @throws IOException If an I/O exception occurs while creating the stage.
     */
    public PopUpStage(Card card) throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        popupStage.setTitle("¡Cambio de color!");

        // Components for selecting color
        Label label = new Label("Escoge el color al que quieres cambiar la partida");
        Button BLUE = new Button("AZUL");
        Button RED = new Button("ROJO");
        Button YELLOW = new Button("AMARILLO");
        Button GREEN = new Button("VERDE");

        BLUE.setStyle("-fx-background-color: blue;");
        RED.setStyle("-fx-background-color: red;");
        YELLOW.setStyle("-fx-background-color: yellow;");
        GREEN.setStyle("-fx-background-color: green;");

        gridPane = new GridPane();

        // Event handlers for color buttons
        BLUE.setOnAction(e -> {
            card.setColor("BLUE");
            gridPane.getChildren().clear();
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a AZUL", "¡Cambio de color!");
        });

        RED.setOnAction(e -> {
            card.setColor("RED");
            gridPane.getChildren().clear();
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a ROJO", "¡Cambio de color!");
        });

        YELLOW.setOnAction(e -> {
            card.setColor("YELLOW");
            gridPane.getChildren().clear();
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a AMARILLO", "¡Cambio de color!");
        });

        GREEN.setOnAction(e -> {
            card.setColor("GREEN");
            gridPane.getChildren().clear();
            popupStage.close();
            alertInformation.createAlert("El color de la partida ha cambiado a VERDE", "¡Cambio de color!");
        });

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

}