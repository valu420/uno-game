package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.alert.alertInformation;
import org.example.eiscuno.view.alert.alertInformation;

import java.io.IOException;

public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;
    @FXML
    private GridPane gridPaneCardsPlayer;
    @FXML
    private ImageView tableImageView;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    private long playerTime;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        this.gameUno.initialCard(table, tableImageView);

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        threadPlayMachine = new ThreadPlayMachine(this.deck, this.humanPlayer, this.table, this.machinePlayer, this.tableImageView, this.gridPaneCardsMachine, this);

        threadSingUNOMachine.setThreadPlayMachine(threadPlayMachine);
        threadPlayMachine.setThreadSingUNOMachine(threadSingUNOMachine);

        Thread t1 = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        Thread t2 = new Thread(threadPlayMachine, "ThreadPlayMachine");

        t1.start();
        t2.start();
        Platform.runLater(() -> threadPlayMachine.updateMachineCardsView());
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                 if (this.table.isValidCard(card)) {
                     try {
                         gameUno.playCard(card);
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                     if (card.getValue().equals("R") || card.getValue().equals("S")) {
                         printCardsHumanPlayer();
                         alertInformation.createAlert("Has jugado una carta "+card.getValue()+"\nVuelve a tirar una carta",
                                 "¡Vuelve a jugar!");
                     } else {
                         threadPlayMachine.setHasPlayerPlayed(true);
                         printCardsHumanPlayer();
                     }
                    } else {
                        alertInformation.createAlert("No puedes jugar esa carta", "Carta no valida");
                    }
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Finds the position of a card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    public void onHandleTakeCard(ActionEvent event) {
        if (!deck.isEmpty()) {
            Card newCard = deck.takeCard();
            humanPlayer.addCard(newCard);
            deck.discardCard(newCard);
            printCardsHumanPlayer();
            threadPlayMachine.setHasPlayerPlayed(true);
        }else {
            deck.refillDeckFromDiscardPile();
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Verifica si el jugador humano tiene solo una carta
        if (humanPlayer.getCardsPlayer().size() == 1) {
            System.out.println("El jugador ha dicho UNO");
            playerTime = System.currentTimeMillis();

            // Aquí puedes añadir lógica adicional si hay reglas específicas para cuando se dice "UNO"
        } else {
            // Penalización para el jugador humano: debe tomar 2 cartas
            System.out.println("El jugador no ha dicho UNO y será penalizado con 2 cartas");
            humanPlayer.drawCards(deck, 2);
            printCardsHumanPlayer();
            System.out.println("Tus cartas: ");
            humanPlayer.printCardsPlayer();
        }

        // Verifica si la máquina tiene solo una carta
        if (machinePlayer.getCardsPlayer().size() == 1) {
            // Penalización para la máquina: debe tomar 2 cartas
            System.out.println("La máquina no ha dicho UNO y será penalizada con 2 cartas");
            machinePlayer.drawCards(deck, 2);
            threadPlayMachine.updateMachineCardsView(); // Actualiza la vista de las cartas de la máquina
        }
    }

    @FXML
    void onHandleExit(ActionEvent event) throws IOException {
        GameUnoStage.deleteInstance();
        Platform.exit();  // Esto cerrará completamente la aplicación
        System.exit(0);
    }
}

