package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test the functionality of the Deck class.
 * It extends ApplicationTest from the TestFX framework for JavaFX, which provides support for testing JavaFX applications.
 * The test methods in this class use JUnit 5 annotations.
 */
class DeckTest extends ApplicationTest {

    /**
     * This test method verifies the functionality of the refillDeckFromDiscardPile method in the Deck class.
     * It first creates a new Deck object and moves all cards from the deck to the discard pile.
     * Then it calls the refillDeckFromDiscardPile method and checks the following:
     * - The deck should have 53 cards after refilling from the discard pile.
     * - The discard pile should have 1 card after refilling the deck.
     * - The last discarded card should remain on the discard pile after refilling the deck.
     */
    @Test
    void testRefillDeckFromDiscardPile() {
        Deck deck = new Deck();
        while (!deck.getDeckOfCards().isEmpty()) {
            deck.getDiscardPile().push(deck.getDeckOfCards().pop());
        }
        Card lastDiscardedCard = deck.getDiscardPile().peek();
        deck.refillDeckFromDiscardPile();
        assertEquals(53, deck.getDeckOfCards().size(), "The deck should have 53 cards after refilling from the discard pile");
        assertEquals(1, deck.getDiscardPile().size(), "The discard pile should have 1 card after refilling the deck");
        assertEquals(lastDiscardedCard, deck.getDiscardPile().peek(), "The last discarded card should remain on the discard pile after refilling the deck");
    }
}