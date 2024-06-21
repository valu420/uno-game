package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

public class ThreadPlayMachine extends Thread {
    private Player humanPlayer;
    private Table table;
    private Deck deck;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GridPane gridPaneCardsMachine;
    private volatile boolean hasPlayerPlayed;
    private volatile boolean running = true;

    private int posInitCardToShow;

    public ThreadPlayMachine(Deck deck, Player humanPlayer, Table table, Player machinePlayer, ImageView tableImageView, GridPane gridPaneCardsMachine) {
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.deck = deck;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.hasPlayerPlayed = false;
        this.posInitCardToShow = 0;
    }

    @Override
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
                                createAlert("El color de la partida ha cambiado a " + card.getColor().toUpperCase());
                            }
                            if (card.getValue().equals("+4")) {
                                humanPlayer.drawCards(deck, 4);
                                card.setColor(chooseRandomColor());
                                createAlert("El color de la partida ha cambiado a " + card.getColor().toUpperCase());
                            }
                            if (card.getValue().equals("+2")) {
                                humanPlayer.drawCards(deck, 2);
                            }

                            if (!card.getValue().equals("R") && !card.getValue().equals("S")) {
                                putCardOnTheTable(card);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                hasPlayerPlayed = false;
                                hasMachinePlayedCard = true;
                                System.out.println(machinePlayer.getCardsPlayer().size());
                            } else if (card.getValue().equals("R") || card.getValue().equals("S")) {
                                putCardOnTheTable(card);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                System.out.println("La maquina ha jugado una carta " + card.getValue() + "\nVolvera a tirar una carta");
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

    private void handleSpecialCards(Card card) {
        if (card.getValue().equals("W")) {
            card.setColor(chooseRandomColor());
            System.out.println("El color de la partida ha sido cambiado a: " + card.getColor());
        }
        if (card.getValue().equals("+4")) {
            humanPlayer.drawCards(deck, 4);
            card.setColor(chooseRandomColor());
            System.out.println("El color de la partida ha sido cambiado a: " + card.getColor());
        }
        if (card.getValue().equals("+2")) {
            humanPlayer.drawCards(deck, 2);
        }
    }

    private void putCardOnTheTable(Card card) {
        table.addCardOnTheTable(card);
        Platform.runLater(() -> {
            tableImageView.setImage(card.getImage());
            int cardIndex = machinePlayer.getCardsPlayer().indexOf(card);
            if (cardIndex != -1) {
                machinePlayer.removeCard(cardIndex);
                updateMachineCardsView();
            } else {
                System.err.println("Error: La carta no se encontró en la mano del jugador de la máquina.");
            }
        });
    }

    public void updateMachineCardsView() {
        Platform.runLater(() -> {
            gridPaneCardsMachine.getChildren().clear();
            Card[] currentVisibleCardsMachine = getCurrentVisibleCardsMachine(posInitCardToShow);
            System.out.println("Número de cartas visibles de la máquina: " + currentVisibleCardsMachine.length);
            for (int i = 0; i < currentVisibleCardsMachine.length; i++) {
                String backImagePath = EISCUnoEnum.CARD_UNO.getFilePath();
                Card cardBack = new Card(backImagePath, "BACK", "NONE");
                ImageView cardBackImageView = cardBack.getCard();
                cardBackImageView.setFitWidth(100);
                cardBackImageView.setPreserveRatio(true);
                gridPaneCardsMachine.add(cardBackImageView, i, 0);
            }
        });
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

    private Card chooseRandomCard() {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        return machinePlayer.getCard(index);
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    private String chooseRandomColor() {
        String[] colors = {"RED", "BLUE", "GREEN", "YELLOW"};
        int numColor = (int) (Math.random() * colors.length);
        return colors[numColor];
    }

    private void createAlert(String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }
    public boolean isRunning(boolean running) {
        return running;
    }
}