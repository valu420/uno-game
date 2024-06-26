package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer) {
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card) {
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }


    /**
     * Return the type of Player
     * @return The type of player.
     */
    public String getTypePlayer() {
        return typePlayer;
    }

    /**
     * Draws a specified number of cards from the deck and adds them to the player's hand.
     * @param deck The deck from which to draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void drawCards(Deck deck, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            addCard(deck.takeCard());
        }
    }

    /**
     * Finds a playable card that matches the current card's color or value.     * @param currentColor the color of the current card on the table
     * @param currentValue the value of the current card on the table
     * @return a playable card, or null if no card can be played
     */
    public boolean findPlayableCard(String currentColor, String currentValue) {
        for (Card card : cardsPlayer) {
            if (card.getColor().equals(currentColor) || card.getValue().equals(currentValue)
                    || card.getValue().equals("W") || card.getValue().equals("+4")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints all cards currently held by the player.
     * Each card in the player's hand is printed to the console.
     */
    public void printCardsPlayer() {
        for (Card card : this.cardsPlayer) {
            System.out.println(card);
        }
    }
}