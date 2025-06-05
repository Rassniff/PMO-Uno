package com.uno.model;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void testGameInitializesPlayersWith7Cards() {
        List<Player> players = Arrays.asList(
            new HumanPlayer("Alice"),
            new BotPlayer("Bot1")
        );
        Game game = new Game(players);
        for (Player p : players) {
            assertEquals(7, p.getHand().size());
        }
    }
    

    @Test
    public void testCanCurrentPlayerPlay() {
        List<Player> players = Arrays.asList(
            new HumanPlayer("Alice"),
            new BotPlayer("Bot1")
        );
        Game game = new Game(players);

        // Imposta manualmente la top card
        PlayedDeck playedDeck = new PlayedDeck();
        Card topCard = new Card(Color.RED, 5);
        playedDeck.addCard(topCard);
        game.setCurrentColor(Color.RED);

        Player current = game.getCurrentPlayer();
        current.clearHand();

        // Carta NON compatibile
        current.drawCard(new Card(Color.GREEN, 1));
        assertFalse(game.canCurrentPlayerPlay());

        // Carta compatibile
        current.drawCard(new Card(Color.RED, 2));
        assertTrue(game.canCurrentPlayerPlay());
    }
}
