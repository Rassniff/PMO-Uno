package com.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class SpecialCardTest {
    @Test
    public void testIsWild() {
        SpecialCard wild = new SpecialCard(Color.SPECIAL, Action.WILD);
        assertTrue(wild.isWild());
    }

    @Test
    public void testGetAction() {
        SpecialCard skip = new SpecialCard(Color.RED, Action.SKIP);
        assertEquals(Action.SKIP, skip.getAction());
    }
}
