package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;

import java.io.IOException;

/**
 * Interface representing the Uno game functionality.
 */
public interface IGameUno {

    /**
     * Starts the Uno game.
     */
    void startGame();

    /**
     * Plays a card in the game, adding it to the table.
     *
     * @param card the card to be played
     */
    void playCard(Card card) throws IOException;

    /**
     * Handles the action when a player shouts "Uno".
     *
     * @param playerWhoSang the identifier of the player who shouted "Uno"
     */
    void haveSungOne(String playerWhoSang);

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow the starting position of the cards to be shown
     * @return an array of cards that are currently visible to the human player
     */
    Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow);

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    Boolean isGameOver();
}
