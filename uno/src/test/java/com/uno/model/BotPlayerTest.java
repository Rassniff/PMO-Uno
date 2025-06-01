package com.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class BotPlayerTest {
    @Test
    public void testIsBot() {
        BotPlayer bot = new BotPlayer("Bot1");
        assertTrue(bot.isBot());
    }
}
