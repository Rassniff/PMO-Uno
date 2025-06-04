package com.uno.model;

import java.util.List;

import com.uno.model.interfaces.ITurnManager;

public class TurnManager implements ITurnManager{
    private List<Player> players;       // Lista dei giocatori in partitia
    private int currentPlayerIndex;     // Variabile che tiene traccia del giocatore attuale nella lista
    private boolean isClockwise = true; // Variabile che serve a determinare la direzione del gioco

    // Costruttore che inizializza il gestore dei turni con una lista di giocatori
    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    // Getter per ottenere il giocatore corrente
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Metodo che permette l'inversione del senso di gioco
    @Override
    public void reverseDirection() {
        isClockwise = !isClockwise;
    }

    //Metodo che permette di saltare il prossimo giocatore
    @Override
    public void skipNextPlayer() {
        advance(); // salta uno
    }

    // Metodo che avanza al prossimo giocatore, tenendo conto della direzione del gioco
    @Override
    public void advance() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    // Metodo che guarda il prossimo giocatore senza avanzare
    @Override
    public Player peekNextPlayer() {
        int nextIndex = isClockwise
                ? (currentPlayerIndex + 1) % players.size()
                : (currentPlayerIndex - 1 + players.size()) % players.size();
        return players.get(nextIndex);
    }

    //Getter della lista dei giocatori
    @Override
    public List<Player> getPlayers() {
        return players;
    }
    
    // Metodo che verifica se una carta è giocabile rispetto alla carta in cima al mazzo e al colore corrente
    public static boolean isPlayable(Card toPlay, Card topCard, Color currentColor) {
        // Se è una SpecialCard nera (jolly, jolly+4, mischiatutto), è sempre giocabile
        if (toPlay instanceof SpecialCard) {
            SpecialCard special = (SpecialCard) toPlay;
            Action action = special.getAction();
    
            if (toPlay.getColor() == Color.SPECIAL &&
                (action == Action.WILD || action == Action.WILD_DRAW_FOUR || action == Action.SHUFFLE)) {
                return true;
            }
            // Se è colorata, va controllato il colore
            return toPlay.getColor() == currentColor;
        }
    
        // Carte numeriche: giocabile se ha stesso colore o stesso numero
        return toPlay.getColor() == currentColor
            || (topCard instanceof Card && toPlay instanceof Card && toPlay.getNumber() == ((Card) topCard).getNumber());
    }
}
