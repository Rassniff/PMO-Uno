package com.uno.model;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.*;

public class TurnManagerTest {
    @Test
    public void testAdvanceAndGetCurrentPlayer() {
        List<Player> players = Arrays.asList(
            new HumanPlayer("Alice"),
            new BotPlayer("Bot1"),
            new BotPlayer("Bot2")
        );
        TurnManager tm = new TurnManager(players);
        assertEquals("Alice", tm.getCurrentPlayer().getName());
        tm.advance();
        assertEquals("Bot1", tm.getCurrentPlayer().getName());
        tm.advance();
        assertEquals("Bot2", tm.getCurrentPlayer().getName());
        tm.advance();
        assertEquals("Alice", tm.getCurrentPlayer().getName());
    }

    @Test
    public void testReverseDirection() {
        List<Player> players = Arrays.asList(
            new HumanPlayer("Alice"),
            new BotPlayer("Bot1"),
            new BotPlayer("Bot2")
        );
        TurnManager tm = new TurnManager(players);
        tm.reverseDirection();
        tm.advance();
        assertEquals("Bot2", tm.getCurrentPlayer().getName());
        tm.advance();
        assertEquals("Bot1", tm.getCurrentPlayer().getName());
    }

    @Test
    public void testPeekNextPlayer() {
        List<Player> players = Arrays.asList(
            new HumanPlayer("Alice"),
            new BotPlayer("Bot1"),
            new BotPlayer("Bot2")
        );
        TurnManager tm = new TurnManager(players);
        assertEquals("Bot1", tm.peekNextPlayer().getName());
        tm.reverseDirection();
        assertEquals("Bot2", tm.peekNextPlayer().getName());
    }
}