package com.uno.model;

import java.util.*;

public class PlayedDeck {
    private Stack<Card> pile; // Pila degli scarti (mazzo degli scarti)

    // Costruttore della pila degli scarti(mazzo degli scarti)
    public PlayedDeck() {
        pile = new Stack<>();
    }

    // Metodo per scartare una carta dopo averla giocata
    public void addCard(Card card) {
        pile.push(card);
    }

    // Metodo per controllare l'ultima carta giocata
    public Card getLastCard() {
        if (pile.isEmpty()) {
            throw new IllegalStateException("La pila degli scarti è vuota.");
        }
        return pile.peek();
    }

    // Metodo per svuotare la pila degli scarti tranne l'ultima carta giocata
    public List<Card> resetAndReturnAllExceptTop() {
        if (pile.size() < 2) {
            return new ArrayList<>();
        }
        Card top = pile.pop();
        List<Card> rest = new ArrayList<>(pile);
        pile.clear();
        pile.push(top);
        return rest;
    }

    // Metodo per controllare il numero di carte scartate
    public int size() {
        return pile.size();
    }
}
