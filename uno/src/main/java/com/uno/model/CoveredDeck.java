package com.uno.model;
import java.util.*;

public class CoveredDeck {
    private Stack<Card> cards;

    // Costruttore del mazzo
    public CoveredDeck() {
        cards = new Stack<>();
        generateDeck();
        shuffle();
    }

    // Genera tutte le carte del mazzo secondo le regole di UNO
    private void generateDeck() {
        // Carte numerate: 0 (1 per colore), 1-9 (2 per colore)
        for (Color color : Color.values()) {
            if (color == Color.SPECIAL) continue;

            // Una carta 0 per colore
            cards.add(new Card(color, 0));

            // Due carte per ciascun numero da 1 a 9
            for (int i = 1; i <= 9; i++) {
                cards.add(new Card(color, i));
                cards.add(new Card(color, i));
            }

            // Carte speciali: Skip, Reverse, Draw Two (2 per tipo per colore)
            for (int i = 0; i < 2; i++) {
                cards.add(new SpecialCard(color, Action.SKIP));
                cards.add(new SpecialCard(color, Action.REVERSE));
                cards.add(new SpecialCard(color, Action.DRAW_TWO));
            }
        }

        // Carte jolly (4 per tipo, colore = SPECIAL)
        for (int i = 0; i < 4; i++) {
            cards.add(new SpecialCard(Color.SPECIAL, Action.WILD));
            cards.add(new SpecialCard(Color.SPECIAL, Action.WILD_DRAW_FOUR));
        }

        //  Carta mischiatutto
        cards.add(new SpecialCard(Color.SPECIAL, Action.SHUFFLE));
    }

    // Metdodo per mischiare il mazzo
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Metodo per controllare se il mazzo è vuoto
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Metodo per pescare
    public Card drawCard(PlayedDeck playedDeck) {
        if (isEmpty()) {
            List<Card> toRecycle = playedDeck.resetAndReturnAllExceptTop();
    
            if (toRecycle.isEmpty()) {
                System.out.println("⚠️ Solo una carta rimasta sul tavolo. Non posso riciclare, ma continuo.");
                // Forza a rimanere vuoto ma evita crash
                return null;
            }
    
            addToBottom(toRecycle);
            shuffle();
        }
    
        return cards.pop();
    }

    // Metodo per aggiungere le carte in fondo al mazzo
    public void addToBottom(List<Card> newCards) {
        Collections.shuffle(newCards);
        cards.addAll(0, newCards);
    }

    // Metodo per controllare il numero di carte nel mazzo
    public int size() {
        return cards.size();
    }

    // Metodo per aggiungere una carta al mazzo
    public void addCard(Card card) {
        cards.add(card);
    }
}
