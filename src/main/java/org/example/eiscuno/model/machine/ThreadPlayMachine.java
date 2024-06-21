package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import javafx.application.Platform;
public class ThreadPlayMachine extends Thread {
    private Player humanPlayer;
    private Table table;
    private Deck deck;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GridPane gridPaneCardsMachine;

    private volatile boolean hasPlayerPlayed;
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
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!hasMachinePlayedCard) {
                    if (!machinePlayer.findPlayableCard(table.getCurrentCardOnTheTable().getColor(), table.getCurrentCardOnTheTable().getValue())) {
                        machinePlayer.drawCards(deck, 1);
                        System.out.println("La maquina se comio 1 carta");
                        System.out.println(machinePlayer.getCardsPlayer().size());
                        hasMachinePlayedCard = true;
                    } else {
                        Card card = chooseRandomCard();
                        if (this.table.isValidCard(card)) {
                            handleSpecialCards(card);
                            putCardOnTheTable(card);
                            hasMachinePlayedCard = true;
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
                printCardsMachine();
            } else {
                System.err.println("Error: La carta no se encontró en la mano del jugador de la máquina.");
            }
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
}

