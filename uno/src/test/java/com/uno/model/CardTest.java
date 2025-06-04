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

}
