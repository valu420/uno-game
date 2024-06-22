package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;
    private Stack<Card> discardPile;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        discardPile = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("RESERVE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {

                String value = getCardValue(cardEnum.name());
                String color = getCardColor(cardEnum.name());

                if (value != null && color != null) {
                    Card card = new Card(cardEnum.getFilePath(), value, color);
                    deckOfCards.push(card);
                } else {
                    System.err.println("Invalid card configuration: " + cardEnum.name());
                }
            }
        }
        Collections.shuffle(deckOfCards);
    }

    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    public Stack<Card> getDeckOfCards() {
        return deckOfCards;
    }

    /**
     * Returns the value of the card based on its name.
     *
     * @param name the name of the card
     * @return the value of the card
     */
    private String getCardValue(String name) {
        if (name.endsWith("0")){
            return "0";
        } else if (name.endsWith("1")){
            return "1";
        } else if (name.endsWith("2")){
            return "2";
        } else if (name.endsWith("3")){
            return "3";
        } else if (name.endsWith("4")){
            return "4";
        } else if (name.endsWith("5")){
            return "5";
        } else if (name.endsWith("6")){
            return "6";
        } else if (name.endsWith("7")){
            return "7";
        } else if (name.endsWith("8")){
            return "8";
        } else if (name.endsWith("9")){
            return "9";
        } else if (name.startsWith("TWO_WILD_DRAW_")) {
            return "+2";
        } else if (name.startsWith("RESERVE_")) {
            return "R";
        } else if (name.contains("SKIP_")) {
            return "S";
        } else if (name.equals("WILD")) {
            return "W";
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return "+4";
        }
        else {
            return null;
        }

    }
    /**
     * Returns the color of the card based on its name.
     *
     * @param name the name of the card
     * @return the color of the card
     */
    private String getCardColor(String name){
        if(name.contains("GREEN")){
            return "GREEN";
        } else if(name.contains("YELLOW")){
            return "YELLOW";
        } else if(name.contains("BLUE")){
            return "BLUE";
        } else if(name.contains("RED")){
            return "RED";
        } else if (name.equals("FOUR_WILD_DRAW") || name.equals("WILD")) {
            return "NONE";
        } else {
            return "BLACK";
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            refillDeckFromDiscardPile();
            if (deckOfCards.isEmpty()) {
                throw new IllegalStateException("No hay más cartas en el mazo.");
            }
        }
        return deckOfCards.pop();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    /**
     * Refill the deck from the discard pile and shuffles it.
     */
    public void refillDeckFromDiscardPile() {
        if (discardPile.isEmpty()) {
            return;
        }
        Card lastDiscardedCard = discardPile.pop();
        while (!discardPile.isEmpty()) {
            deckOfCards.push(discardPile.pop());
        }
        discardPile.push(lastDiscardedCard);
        Collections.shuffle(deckOfCards);
    }

    /**
     * Adds a card to the discard pile.
     *
     * @param card the card to be added to the discard pile
     */
    public void discardCard(Card card) {
        discardPile.push(card);
    }

    public void addCardToDeck(Card card) {
        deckOfCards.push(card);
    }
}