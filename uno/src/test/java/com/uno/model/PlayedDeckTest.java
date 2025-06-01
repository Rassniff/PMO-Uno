package com.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class PlayedDeckTest {
    @Test
    public void testAddAndGetLastCard() {
        PlayedDeck deck = new PlayedDeck();
        Card card1 = new Card(Color.RED, 3);
        Card card2 = new Card(Color.BLUE, 5);
        deck.addCard(card1);
        deck.addCard(card2);
        assertEquals(card2, deck.getLastCard());
    }

    @Test
    public void testResetAndReturnAllExceptTop() {
        PlayedDeck deck = new PlayedDeck();
        Card card1 = new Card(Color.RED, 3);
        Card card2 = new Card(Color.BLUE, 5);
        Card card3 = new Card(Color.GREEN, 7);
        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        List<Card> rest = deck.resetAndReturnAllExceptTop();
        assertEquals(2, rest.size());
        assertEquals(card3, deck.getLastCard());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastCardThrowsIfEmpty() {
        PlayedDeck deck = new PlayedDeck();
        deck.getLastCard();
    }
}