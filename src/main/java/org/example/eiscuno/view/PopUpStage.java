package org.example.eiscuno.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.model.card.Card;

import java.io.IOException;

public class PopUpStage extends Stage {
    public PopUpStage(Card card) throws IOException {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UTILITY);

        popupStage.setTitle("Ventana emergente");

        Label label = new Label("Escoge el color al que quieres cambiar la partida");
        Button BLUE = new Button("AZUL");
        Button RED = new Button("ROJO");
        Button YELLOW = new Button("AMARILLO");
        Button GREEN = new Button("VERDE");


        BLUE.setOnAction(e -> {
            card.setColor("BLUE");
            System.out.println("El color de la partida ha sido cambiado a AZUL");
            popupStage.close();
        });

        RED.setOnAction(e -> {
            card.setColor("RED");
            System.out.println("El color de la partida ha sido cambiado a ROJO");
            popupStage.close();
        });

        YELLOW.setOnAction(e -> {
            card.setColor("YELLOW");
            System.out.println("El color de la partida ha sido cambiado a AMARILLO");
            popupStage.close();
        });

        GREEN.setOnAction(e -> {
            card.setColor("GREEN");
            System.out.println("El color de la partida ha sido cambiado a VERDE");
            popupStage.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, BLUE, RED, YELLOW, GREEN);

        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Muestra y espera hasta que se cierre la ventana
    }
    }