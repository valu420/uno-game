package org.example.eiscuno.model.game;


import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.PopUpStage;

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

    /**
     * Constructs a new GameUno instance.
     *
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

    public void initialCard(Table table, ImageView imageView){
        Card initialCard = null;
        boolean isValidCardToStart = false;
        while (!isValidCardToStart) {
            Card card = deck.takeCard();
            String value = card.getValue();
            if (!(value.matches("[0-9]"))){
                System.out.println("Esta carta no puede comenzar el juego");
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
     *
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
//game over funcion
    private void postMoveActions(String playerType) {
        if (playerType.equals(humanPlayer.getTypePlayer())) {
            if (humanPlayer.getCardsPlayer().isEmpty()) {
                System.out.println("\nFin de la partida!\n");
                isGameOver();
            } else if (playerType.equals(machinePlayer.getTypePlayer())) {
                if (machinePlayer.getCardsPlayer().isEmpty()) {
                    System.out.println("\nFin de la partida!\n");
                    isGameOver();
                }
            }
    }    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
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
     *
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
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {
        return null;
    }
}
