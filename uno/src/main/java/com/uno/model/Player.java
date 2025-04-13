package com.uno.model;

import java.util.*;

public abstract class Player {
    protected String name;      // Nome del giocatore
    protected List<Card> hand;  // Mano del giocatore

    // Costruttore del giocatore
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    // Getter del nome
    public String getName() {
        return name;
    }

    // Getter della mano
    public List<Card> getHand() {
        return hand;
    }

    // Metodi comuni a tutti i giocatori per: pescare, rimuovere e controllare la vittoria
    public void drawCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    // Metodo che controlla se il giocatore ha finito le carte in mano
    public boolean isHandEmpty() {
        return hand.isEmpty();
    }

    // Metodi che definiscono se il giocatore è umano o bot
    public abstract Card playTurn(Card topCard, Color currentColor);

    public abstract boolean isBot();

    public abstract Color chooseColor();
}
