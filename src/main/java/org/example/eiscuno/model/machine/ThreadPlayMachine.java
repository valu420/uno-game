package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Player humanPlayer;
    private Table table;
    private Deck deck;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;


    public ThreadPlayMachine(Deck deck, Player humanPlayer, Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.deck = deck;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;

    }

    public void run() {
        boolean hasMachinePlayedCard = false;
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!hasMachinePlayedCard) {
                    if(!machinePlayer.findPlayableCard(table.getCurrentCardOnTheTable().getColor(), table.getCurrentCardOnTheTable().getValue())) {
                        machinePlayer.drawCards(deck, 1);
                        System.out.println("La maquina se comio 1 carta");
                        System.out.println(machinePlayer.getCardsPlayer().size());
                        hasMachinePlayedCard = true;
                    }
                    else{
                        Card card = chooseRandomCard();
                        if (this.table.isValidCard(card)) {
                            if (card.getValue().equals("W")) {
                                card.setColor(chooseRandomColor());
                                createAlert("El color de la partida ha cambiado a "+card.getColor().toUpperCase());
                            }
                            if (card.getValue().equals("+4")) {
                                humanPlayer.drawCards(deck, 4);
                                card.setColor(chooseRandomColor());
                                createAlert("El color de la partida ha cambiado a "+card.getColor().toUpperCase());
                            }
                            if (card.getValue().equals("+2")) {
                                humanPlayer.drawCards(deck, 2);
                            }

                            if (!card.getValue().equals("R") && !card.getValue().equals("S")){
                                putCardOnTheTable(card);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                hasPlayerPlayed = false;
                                hasMachinePlayedCard = true;
                                System.out.println(machinePlayer.getCardsPlayer().size());
                            }
                            else if(card.getValue().equals("R") || card.getValue().equals("S")) {
                                putCardOnTheTable(card);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                System.out.println("La maquina ha jugado una carta "+card.getValue()+"\nVolvera a tirar una carta");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                hasMachinePlayedCard = false;
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(Card card) {
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
    }

    private Card chooseRandomCard () {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        return machinePlayer.getCard(index);
    }

    public void setHasPlayerPlayed ( boolean hasPlayerPlayed){
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    private String chooseRandomColor () {
        String color = "";
        int numColor = (int) ((Math.random() * 4) + 1);
        color = switch (numColor) {
            case 1 -> "RED";
            case 2 -> "BLUE";
            case 3 -> "GREEN";
            case 4 -> "YELLOW";
            default -> color;
        };
        return color;
    }

    private void createAlert(String text){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
}
}

