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

/**
 * This class is used to test the functionality of the GameUno class.
 * It extends ApplicationTest from the TestFX framework for JavaFX, which provides support for testing JavaFX applications.
 * The test methods in this class use JUnit 5 annotations.
 */
class GameUnoTest extends ApplicationTest {

    private GameUno gameUno;
    private Table table;
    private ImageView imageView;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;

    /**
     * This method sets up the necessary objects for the tests.
     * It is annotated with @BeforeEach, so it runs before each test method in this class.
     */
    @BeforeEach
    void setUp() {
        humanPlayer = new Player("Human");
        machinePlayer = new Player("Machine");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
        imageView = new ImageView();
    }

    /**
     * This test method verifies the functionality of the startGame method in the GameUno class.
     * It first creates a new GameUno object and calls the startGame method.
     * Then it checks the following:
     * - The human player should have 5 cards after starting the game.
     * - The machine player should have 5 cards after starting the game.
     */
    @Test
    void testStartGame() {
        gameUno.startGame();
        assertEquals(5, humanPlayer.getCardsPlayer().size(), "Human player should have 5 cards");
        assertEquals(5, machinePlayer.getCardsPlayer().size(), "Machine player should have 5 cards");
    }

    /**
     * This test method verifies the functionality of the initialCard method in the GameUno class.
     * It first creates a new GameUno object and calls the initialCard method.
     * Then it checks the following:
     * - The initial card should not be null.
     * - The initial card should not be a special card.
     */
    @Test
    void testInitialCard() {
        gameUno.initialCard(table, imageView);
        Card initialCard = table.getCurrentCardOnTheTable();
        assertNotNull(initialCard, "Initial card should not be null");
        assertTrue(initialCard.getValue().matches("[0-9]"), "Initial card should not be a special card");
    }
}