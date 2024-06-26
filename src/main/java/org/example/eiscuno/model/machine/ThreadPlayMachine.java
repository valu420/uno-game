package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.alert.alertInformation;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import static org.example.eiscuno.view.alert.alertInformation.createAlert;

/**
 * ThreadPlayMachine is a class that extends Thread to handle the machine player's actions during the game.
 * The machine player plays a card if it has a valid card to play, otherwise it draws a card.
 * The machine player also checks if the human player has one card left and penalizes the human player if "UNO" is not called.
 */
public class ThreadPlayMachine extends Thread {
    private Player humanPlayer;
    private Table table;
    private Deck deck;
    private Player machinePlayer;
    private GameUno gameUno;
    private GameUnoController controller;
    private ThreadSingUNOMachine threadSingUNOMachine;
    private ImageView tableImageView;
    private GridPane gridPaneCardsMachine;
    private volatile boolean hasPlayerPlayed;
    private volatile boolean running = true;
    private int posInitCardToShow;

    /**
     * Constructs a new ThreadPlayMachine instance.
     * @param deck                 The deck of cards used in the game.
     * @param humanPlayer          The human player participating in the game.
     * @param table                The table where cards are placed during the game.
     * @param machinePlayer        The machine player participating in the game.
     * @param tableImageView       The ImageView displaying the current card on the table.
     * @param gridPaneCardsMachine The GridPane displaying the machine player's cards.
     */
    public ThreadPlayMachine(Deck deck, Player humanPlayer, Table table, Player machinePlayer, ImageView tableImageView, GridPane gridPaneCardsMachine,GameUnoController controller) {
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.deck = deck;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.gameUno = gameUno;
        this.hasPlayerPlayed = false;
        this.controller = controller;
        this.posInitCardToShow = 0;
    }

    /**
     * Sets the ThreadSingUNOMachine instance.
     *
     * @param threadSingUNOMachine The ThreadSingUNOMachine instance to set.
     */
    public void setThreadSingUNOMachine(ThreadSingUNOMachine threadSingUNOMachine) {
        this.threadSingUNOMachine = threadSingUNOMachine;
    }

    /**
     * The run method defines the actions taken by the machine player during the game.
     * The machine player plays a card if it has a valid card to play, otherwise it draws a card.
     */
    @Override
    public void run() {
        while (running) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(1000); // Simula el tiempo de pensamiento de la máquina
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean hasMachinePlayedCard = false;
                while (!hasMachinePlayedCard) {
                    if (!machinePlayer.findPlayableCard(table.getCurrentCardOnTheTable().getColor(), table.getCurrentCardOnTheTable().getValue())) {
                        machinePlayer.drawCards(deck, 1);
                        hasMachinePlayedCard = true;
                        Platform.runLater(this::updateMachineCardsView);
                    } else {
                        Card card = chooseRandomCard();
                        if (this.table.isValidCard(card)) {
                            handleSpecialCards(card);
                            if (!card.getValue().equals("R") && !card.getValue().equals("S") && !card.getValue().equals("+2") && !card.getValue().equals("+4")){
                                putCardOnTheTable(card);
                                threadSingUNOMachine.checkUNO();
                                checkIsGameOver();
                                Platform.runLater(this::updateMachineCardsView);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                hasPlayerPlayed = false;
                                hasMachinePlayedCard = true;
                            } else if (card.getValue().equals("R") || card.getValue().equals("S") || card.getValue().equals("+2") || card.getValue().equals("+4")){
                                putCardOnTheTable(card);
                                threadSingUNOMachine.checkUNO();
                                checkIsGameOver();
                                Platform.runLater(this::updateMachineCardsView);
                                machinePlayer.removeCard(machinePlayer.getCardsPlayer().indexOf(card));
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            threadSingUNOMachine.checkUNO();
                            checkIsGameOver();
                        }
                    }
                }
//                threadSingUNOMachine.checkUNO();
//                // Verifica si la máquina debe ser penalizada por no decir "UNO"
//                if (machinePlayer.getCardsPlayer().size() == 1) {
//                    threadSingUNOMachine.checkUNO();
//                    createAlert("La máquina no ha dicho UNO y será penalizada con 2 cartas", "Penalización");
//                    machinePlayer.drawCards(deck, 2);
//                    Platform.runLater(this::updateMachineCardsView);
//                }
                hasPlayerPlayed = false;
            }
        }
    }

        /**
         * Handles special cards (Wild, +4, +2) played by the machine.
         * @param card The special card played.
         */
        private void handleSpecialCards (Card card){
            if (card.getValue().equals("W")) {
                card.setColor(chooseRandomColor());
                createAlert("El color de la partida ha sido cambiado a: " + card.getColor(), "¡Cambio de color!");
            }
            if (card.getValue().equals("+4")) {
                humanPlayer.drawCards(deck, 4);
                card.setColor(chooseRandomColor());
                createAlert("El color de la partida ha sido cambiado a: " + card.getColor(), "¡Cambio de color!");
            }
            if (card.getValue().equals("+2")) {
                humanPlayer.drawCards(deck, 2);
            }
        }

        /**
         * Informs the ThreadPlayMachine instance that the human player has called "UNO".
         */
        public void onUnoCalled () {
            Platform.runLater(() -> {
                createAlert("La máquina ha dicho UNO", "UNO!");
            });
        }

    private void checkIsGameOver(){
        if(machinePlayer.getCardsPlayer().isEmpty()){
            createAlert("¡La maquina ha ganado!", "¡Victoria!");
            GameUnoStage.deleteInstance();
            Platform.exit();
            System.exit(0);
        }
    }

        /**
         * Places a card on the table and updates the UI.
         * @param card The card to be placed on the table.
         */
        private void putCardOnTheTable(Card card){
            table.addCardOnTheTable(card);
            Platform.runLater(() -> {
                tableImageView.setImage(card.getImage());
                updateMachineCardsView();
            });
        }

        /**
         * Retrieves the current visible cards of the machine player.
         * @param posInitCardToShow The initial position of the cards to show.
         * @return An array of visible cards.
         */
        private Card[] getCurrentVisibleCardsMachine ( int posInitCardToShow){
            int totalCards = machinePlayer.getCardsPlayer().size();
            int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
            Card[] cards = new Card[numVisibleCards];
            for (int i = 0; i < numVisibleCards; i++) {
                cards[i] = machinePlayer.getCard(posInitCardToShow + i);
            }
            return cards;
        }

        /**
         * Updates the machine player's cards view.
         */
    public void updateMachineCardsView() {
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

    /**
     * Chooses a random card from the machine player's hand.
     * @return A randomly selected card.
     */
    private Card chooseRandomCard() {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        return machinePlayer.getCard(index);
    }

    /**
     * Sets the hasPlayerPlayed flag.
     * @param hasPlayerPlayed Indicates if the human player has played a card.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    /**
     * Chooses a random color for Wild cards.
     * @return A randomly selected color.
     */
    private String chooseRandomColor() {
        String[] colors = {"RED", "BLUE", "GREEN", "YELLOW"};
        int numColor = (int) (Math.random() * colors.length);
        return colors[numColor];
    }

    /**
     * Creates an alert dialog with the specified text.
     * @param text The text to display in the alert dialog.
     */
    private void createAlert(String text, String title){
        Platform.runLater(() -> {
            alertInformation.createAlert(text, title);
        });
}

    /**
     * Sets the running flag to stop the thread.
     * @param running The running state to set.
     * @return The updated running state.
     */
    public boolean isRunning(boolean running) {
        return running;
    }
}