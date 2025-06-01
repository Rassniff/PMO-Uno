package com.uno.model;

import java.util.*;

public abstract class Player {
    protected String name;      // Nome del giocatore
    protected List<Card> hand;  // Mano del giocatore
    protected boolean unoCalled = false; // Flag per indicare se il giocatore ha chiamato "Uno"
    
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

    // Metodo per aggiungere una carta alla mano del giocatore
    public void drawCard(Card card) {
        hand.add(card);
    }

    // Metodo per rimuovere una carta dalla mano del giocatore
    public void removeCard(Card card) {
        hand.remove(card);
    }

    // Metodo che controlla se il giocatore ha finito le carte in mano
    public boolean isHandEmpty() {
        return hand.isEmpty();
    }
    
    // Metodo che verifica se il giocatore può giocare una carta Wild Draw Four
    public boolean canPlayWildDrawFour(Card topCard, Color currentColor) {
        // Se il giocatore ha almeno una carta giocabile (escluso il jolly+4), non può giocare il jolly+4
        return hand.stream()
            .noneMatch(card -> TurnManager.isPlayable(card, topCard, currentColor) &&
                              !(card instanceof SpecialCard specialCard &&
                                specialCard.getAction() == Action.WILD_DRAW_FOUR));
    }
    
    // Metodo per confrontare i giocatori
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player p = (Player) o;
        return name.equals(p.name); // o un ID univoco se lo usi
    }
    
    // Metodo per calcolare l'hash del giocatore
    @Override
    public int hashCode() {
        return Objects.hash(name); // o ID
    }
    
    // Getter per il flag unoCalled
    public boolean isUnoCalled() {
        return unoCalled;
    }
    
    // Setter per il flag unoCalled
    public void setUnoCalled(boolean unoCalled) {
        this.unoCalled = unoCalled;
    }
    
    // Metodo che pulisce la mano del giocatore
    public void clearHand() {
        hand.clear();
    } 
    
    // Metodi astratti che devono essere implementati dalle classi derivate
    public abstract Card playTurn(Card topCard, Color currentColor);
    public abstract boolean isBot();
    public abstract Color chooseColor();
}
