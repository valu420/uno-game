package org.example.eiscuno.model.game;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class GameUnoTest extends ApplicationTest {

    private GameUno gameUno;
    private Table table;
    private ImageView imageView;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;


    @BeforeEach
    void setUp() {
        humanPlayer = new Player("Human");
        machinePlayer = new Player("Machine");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
        imageView = new ImageView();
    }

    @Test
    void testStartGame() {
        gameUno.startGame();
        assertEquals(5, humanPlayer.getCardsPlayer().size(), "Human player should have 5 cards");
        assertEquals(5, machinePlayer.getCardsPlayer().size(), "Machine player should have 5 cards");
    }

    @Test
    void testInitialCard() {
        gameUno.initialCard(table, imageView);
        Card initialCard = table.getCurrentCardOnTheTable();
        assertNotNull(initialCard, "Initial card should not be null");
        assertTrue(initialCard.getValue().matches("[0-9]"), "Initial card should not be a special card");
    }
}