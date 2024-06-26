package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private ArrayList<Card> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<Card>();
    }

    /**
     * Adds a card to the table.
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table.
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    /**
     * Checks if a given card is valid to play on the current card on the table.
     * @param card The card to check for validity.
     * @return True if the card is valid to play, otherwise false.
     */
    public Boolean isValidCard(Card card) {
        Card currentCardOnTheTable = getCurrentCardOnTheTable();
        if (card.getColor().equals(currentCardOnTheTable.getColor()) || card.getValue().equals(currentCardOnTheTable.getValue())
                || card.getValue().equals("W") || card.getValue().equals("+4") || card.getColor().equals("BLACK")) {
            return true;
        } else {
            return false;
        }
    }
}