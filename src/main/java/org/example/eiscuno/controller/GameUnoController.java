package org.example.eiscuno.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.alert.alertInformation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

import static org.example.eiscuno.view.alert.alertInformation.createAlert;

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
    private boolean hasSungOne = false;
    private long playerTime;
    private Timeline unoTimer;


    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        this.gameUno.initialCard(table, tableImageView);

        threadSingUNOMachine = new ThreadSingUNOMachine(this.machinePlayer.getCardsPlayer());
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
     * The cards are clickable, and the player can play a card by clicking on it.
     * If the card is valid, it is played, and the machine player plays its turn.
     * If the card is not valid, an alert is shown to the player.
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
                     if (card.getValue().equals("R") || card.getValue().equals("S") || card.getValue().equals("+2") || card.getValue().equals("+4")){
                         printCardsHumanPlayer();
                         checkIsUno();
                         checkIsGameOver();
                         createAlert("Has jugado una carta "+card.getValue()+"\nVuelve a tirar una carta",
                                 "¡Vuelve a jugar!");
                     } else {
                         checkIsGameOver();
                         checkIsUno();
                         threadPlayMachine.setHasPlayerPlayed(true);
                         printCardsHumanPlayer();
                     }
//                     checkIsGameOver();
                    } else {
                        createAlert("No puedes jugar esa carta", "Carta no valida");
                    }
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Finds the position of a card in the human player's hand.
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
     * Checks if the game is over.
     * If the human player has no cards left, the player wins.
     */
    private void checkIsGameOver(){
        if(humanPlayer.getCardsPlayer().isEmpty()){
            createAlert("¡Has ganado!", "¡Felicidades!");
            GameUnoStage.deleteInstance();
            Platform.exit();
            System.exit(0);
        }
    }

    private void checkIsUno(){
        if(humanPlayer.getCardsPlayer().size() == 1){
            if(unoTimer == null) {
                unoTimer = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    if(!hasSungOne){
                        createAlert("Has tardado demasiado en decir UNO, penalización de 2 cartas", "Penalización");
                        humanPlayer.drawCards(deck, 2);
                        printCardsHumanPlayer();
                    }
                    unoTimer = null; // Reset the timer
                }));
                unoTimer.setCycleCount(1);
                unoTimer.play();
            }
        } else {
            if(unoTimer != null) {
                unoTimer.stop();
                unoTimer = null;
            }
            hasSungOne = false;
        }
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
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
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        if (humanPlayer.getCardsPlayer().size() == 1) {
            alertInformation.createAlert("Dijiste UNO", "UNO!");
            hasSungOne = true;
//            playerTime = System.currentTimeMillis();
        } else {
            alertInformation.createAlert("Dijiste UNO falsamente, penalización de 2 cartas", "Penalización");
            humanPlayer.drawCards(deck, 2);
            printCardsHumanPlayer();
        }

        if (machinePlayer.getCardsPlayer().size() == 1 && !threadSingUNOMachine.isUnoCalled()) {
            alertInformation.createAlert("La máquina no ha dicho UNO y será penalizada con 2 cartas", "Penalización");
            machinePlayer.drawCards(deck, 2);
            threadPlayMachine.updateMachineCardsView();
        }
    }

    /**
     * Handles the action of exiting the game.
     * @param event the action event
     * @throws IOException if an error occurs while loading the stage
     */
    @FXML
    void onHandleExit(ActionEvent event) throws IOException {
        GameUnoStage.deleteInstance();
        Platform.exit();  // Esto cerrará completamente la aplicación
        System.exit(0);
    }
}

