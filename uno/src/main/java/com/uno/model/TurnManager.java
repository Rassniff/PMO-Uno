package com.uno.model;

import java.util.List;

public class TurnManager {
    private List<Player> players; //Lista dei giocatori in partitia
    private int currentPlayerIndex; //Variabile che tiene traccia del giocatore attuale nella lista
    private boolean isClockwise = true; //Variabile che serve a determinare la direzione del gioco

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    //Metodo che ritorna il giocatore il cui turno è attualmente in corso
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    //Metodo che permette l'inversione del senso di gioco
    public void reverseDirection() {
        isClockwise = !isClockwise;
    }

    //Metodo che permette di saltare il prossimo giocatore
    public void skipNextPlayer() {
        advance(); // salta uno
    }

    //Logica principale per far andare avanti il gioco assicurandosi di rimanere all'interno di un certo intervallo(cerchio)
    //controllando il senso di gioco
    public void advance() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    //Metodo che guarda chi sarà il prossimo giocatore, senza cambiare il turno (gestione carte +2 +4)
    public Player peekNextPlayer() {
        int nextIndex = isClockwise
                ? (currentPlayerIndex + 1) % players.size()
                : (currentPlayerIndex - 1 + players.size()) % players.size();
        return players.get(nextIndex);
    }

    //Getter della lista dei giocatori
    public List<Player> getPlayers() {
        return players;
    }
    
}
