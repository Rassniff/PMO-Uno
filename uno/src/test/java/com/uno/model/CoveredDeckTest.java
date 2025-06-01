package com.uno.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoveredDeckTest {
    @Test
    public void testDrawCardReducesDeckSize() {
        CoveredDeck deck = new CoveredDeck();
        int initialSize = deck.size();
        deck.drawCard(new PlayedDeck());
        assertEquals(initialSize - 1, deck.size());
    }

    @Test
    public void testRecycleFromPlayedDeck() {
        CoveredDeck covered = new CoveredDeck();
        PlayedDeck played = new PlayedDeck();
        // Svuota il mazzo coperto
        while (covered.size() > 0) {
            covered.drawCard(played);
        }
        // Aggiungi carte al mazzo degli scarti
        played.addCard(new Card(Color.RED, 1));
        played.addCard(new Card(Color.BLUE, 2));
        played.addCard(new Card(Color.GREEN, 3));
        // Pesca, dovrebbe riciclare dal PlayedDeck
        Card drawn = covered.drawCard(played);
        assertNotNull(drawn);
        assertEquals(1, played.size()); // Solo la carta in cima resta
    }
}
