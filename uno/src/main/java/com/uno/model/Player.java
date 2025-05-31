package com.uno.model;

import java.util.*;

public abstract class Player {
    protected String name;      // Nome del giocatore
    protected List<Card> hand;  // Mano del giocatore
    protected boolean unoCalled = false; 
    

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
    
    public boolean canPlayWildDrawFour(Card topCard, Color currentColor) {
        // Se il giocatore ha almeno una carta giocabile (escluso il jolly+4), non può giocare il jolly+4
        return hand.stream()
            .noneMatch(card -> TurnManager.isPlayable(card, topCard, currentColor) &&
                              !(card instanceof SpecialCard specialCard &&
                                specialCard.getAction() == Action.WILD_DRAW_FOUR));
    }
    //Altrimenti la mappa dei punteggi crei più chiavi diverse per lo stesso giocatore.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player p = (Player) o;
        return name.equals(p.name); // o un ID univoco se lo usi
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // o ID
    }
    
    //serve per il bottone uno
    public boolean isUnoCalled() {
        return unoCalled;
    }

    public void setUnoCalled(boolean unoCalled) {
        this.unoCalled = unoCalled;
    }

    public void clearHand() {
        hand.clear();
    }   
    // Metodi che definiscono se il giocatore è umano o bot
    public abstract Card playTurn(Card topCard, Color currentColor);

    public abstract boolean isBot();

    public abstract Color chooseColor();
}
