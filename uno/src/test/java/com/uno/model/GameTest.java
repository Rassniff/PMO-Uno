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
        // Svuota la mano del primo giocatore e aggiungi solo carte non giocabili
        Player current = game.getCurrentPlayer();
        current.clearHand();
        current.drawCard(new Card(Color.GREEN, 1));
        // Supponiamo che la carta in cima sia rossa
        game.setCurrentColor(Color.RED);
        assertFalse(game.canCurrentPlayerPlay());
        // Aggiungi una carta giocabile
        current.drawCard(new Card(Color.RED, 2));
        assertTrue(game.canCurrentPlayerPlay());
    }
}
