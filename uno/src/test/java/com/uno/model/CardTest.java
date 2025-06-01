package com.uno.model;


import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void testIsWild() {
        Card normal = new Card(Color.RED, 5);
        assertFalse(normal.isWild());
    }

    @Test
    public void testGetColorAndNumber() {
        Card card = new Card(Color.BLUE, 7);
        assertEquals(Color.BLUE, card.getColor());
        assertEquals(7, card.getNumber());
    }

    @Test
    public void testIsPlayableOnWithSpecialCard() {
        Card normal = new Card(Color.RED, 5);
        SpecialCard skip = new SpecialCard(Color.RED, Action.SKIP);
        SpecialCard wild = new SpecialCard(Color.SPECIAL, Action.WILD);

        assertTrue(skip.isPlayableOn(normal)); // stesso colore
        assertTrue(wild.isPlayableOn(normal)); // jolly sempre giocabile
        assertFalse(skip.isPlayableOn(new Card(Color.BLUE, 5))); // diverso colore e non stesso numero
    }
}
