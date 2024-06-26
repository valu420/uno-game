package org.example.eiscuno.model.game;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.PopUpStage;
import org.example.eiscuno.view.alert.alertInformation;

import java.io.IOException;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno {

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Constructs a new GameUno instance.
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
    }

    /**
     * Draws the initial card for the table from the deck.
     * @param table      The table where the initial card is placed.
     * @param imageView  The ImageView to display the initial card.
     */
    public void initialCard(Table table, ImageView imageView){
        Card initialCard = null;
        boolean isValidCardToStart = false;
        while (!isValidCardToStart) {
            Card card = deck.takeCard();
            String value = card.getValue();
            if (!(value.matches("[0-9]"))){
                deck.addCardToDeck(card);
            }
            else{
                initialCard = card;
                isValidCardToStart = true;
            }
        }
        table.addCardOnTheTable(initialCard);
        imageView.setImage(initialCard.getImage());
    }

    /**
     * Places a card on the table during the game.
     * The card is added to the table and the appropriate actions are taken based on the card played.
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) throws IOException {
        String playerType = humanPlayer.getTypePlayer();
        String playerMachime = machinePlayer.getTypePlayer();
        this.table.addCardOnTheTable(card);
        if (card.getValue().equals("W")){
            new PopUpStage(card);
        }
        if (card.getValue().equals("+4")){
            machinePlayer.drawCards(deck, 4);
            new PopUpStage(card);
        }
        if (card.getValue().equals("+2")){
            machinePlayer.drawCards(deck, 2);
        }
        postMoveActions(playerType);
        postMoveActions(playerMachime);

    }

    /**
     * Performs actions after a move.
     * Checks if the game is over and displays a message if a player has won.
     * @param playerType The type of player who made the move.
     */
    public void postMoveActions(String playerType) {

        if (playerType.equals(humanPlayer.getTypePlayer())) {
            if (humanPlayer.getCardsPlayer().isEmpty()) {
                alertInformation.createAlert("Felicidades haz ganado", "Victoria!");
                isGameOver();
            }
        } else if (playerType.equals(machinePlayer.getTypePlayer())) {
            if (machinePlayer.getCardsPlayer().isEmpty()) {
                alertInformation.createAlert("La maquina ha ganado", "Victoria");
                isGameOver();
            }
        }
    }

    /**
     * Checks if the game is over and ends the game.
     * @return Always returns null.
     */
   @Override
   public Boolean isGameOver() {
       threadPlayMachine.isRunning(false);
       System.out.println("\nFin de la partida!\n");
       Platform.runLater(() -> {
           GameUnoStage.deleteInstance();
       });
       return null;
   }


    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Retrieves the current visible cards of the machine player.
     * @return an array of cards that are currently visible to the machine player
     */
    @Override
    public Card[] getCurrentVisibleCardsMachinePlayer(int posInitCardToShow) {
        int totalCards = this.machinePlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);

        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.machinePlayer.getCard(posInitCardToShow + i);
        }
        return cards;
    }
}
