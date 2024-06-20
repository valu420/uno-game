package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import javafx.application.Platform;
public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GridPane gridPaneCardsMachine;

    private volatile boolean hasPlayerPlayed;
    private int posInitCardToShow;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GridPane gridPaneCardsMachine) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.hasPlayerPlayed = false;
        this.posInitCardToShow = 0;
    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

                putCardOnTheTable();

                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        table.addCardOnTheTable(card);

        Platform.runLater(() -> {
            tableImageView.setImage(card.getImage());
            machinePlayer.removeCard(index); // Eliminar la carta jugada de la mano del jugador de la máquina
            printCardsMachine(); // Actualizar la interfaz gráfica
        });

    }

    public void printCardsMachine() {
        gridPaneCardsMachine.getChildren().clear();
        Card[] currentVisibleCardsMachine = getCurrentVisibleCardsMachine(posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsMachine.length; i++) {
            String backImagePath = EISCUnoEnum.CARD_UNO.getFilePath();
            Card cardBack = new Card(backImagePath, "BACK", "NONE");
            ImageView cardBackImageView = cardBack.getCard();

            cardBackImageView.setFitWidth(100);
            cardBackImageView.setPreserveRatio(true);

            gridPaneCardsMachine.add(cardBackImageView, i, 0);
        }
    }
    private Card[] getCurrentVisibleCardsMachine(int posInitCardToShow) {
        int totalCards = machinePlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = machinePlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
