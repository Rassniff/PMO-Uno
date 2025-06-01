package com.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class HumanPlayerTest {
    @Test
    public void testIsBot() {
        HumanPlayer player = new HumanPlayer("Alice");
        assertFalse(player.isBot());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPlayTurnThrows() {
        HumanPlayer player = new HumanPlayer("Alice");
        player.playTurn(null, null);
    }

    @Test
    public void testAddAndRemoveCard() {
        HumanPlayer player = new HumanPlayer("Alice");
        Card card = new Card(Color.RED, 5);
        player.drawCard(card);
        assertEquals(1, player.getHand().size());
        player.removeCard(card);
        assertEquals(0, player.getHand().size());
    }

    @Test
    public void testCanPlayWildDrawFour() {
        HumanPlayer player = new HumanPlayer("Alice");
        // Ha solo carte non giocabili
        player.drawCard(new Card(Color.RED, 5));
        assertTrue(player.canPlayWildDrawFour(new Card(Color.BLUE, 7), Color.BLUE));
        // Ha una carta giocabile
        player.drawCard(new Card(Color.BLUE, 2));
        assertFalse(player.canPlayWildDrawFour(new Card(Color.BLUE, 7), Color.BLUE));
    }

    @Test
    public void testUnoCalledFlag() {
        HumanPlayer player = new HumanPlayer("Alice");
        assertFalse(player.isUnoCalled());
        player.setUnoCalled(true);
        assertTrue(player.isUnoCalled());
        player.setUnoCalled(false);
        assertFalse(player.isUnoCalled());
    }
}
