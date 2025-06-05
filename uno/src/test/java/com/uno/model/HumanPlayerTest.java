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
    public void testUnoCalledFlag() {
        HumanPlayer player = new HumanPlayer("Alice");
        assertFalse(player.isUnoCalled());
        player.setUnoCalled(true);
        assertTrue(player.isUnoCalled());
        player.setUnoCalled(false);
        assertFalse(player.isUnoCalled());
    }
}
