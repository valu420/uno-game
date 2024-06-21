package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest extends ApplicationTest {

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