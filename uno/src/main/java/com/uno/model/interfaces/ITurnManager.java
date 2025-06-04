package com.uno.model.interfaces;

import java.util.List;

import com.uno.model.Player;

public interface ITurnManager {

    // Getter per ottenere il giocatore corrente
    Player getCurrentPlayer();

    // Metodo che permette l'inversione del senso di gioco
    void reverseDirection();

    //Metodo che permette di saltare il prossimo giocatore
    void skipNextPlayer();

    // Metodo che avanza al prossimo giocatore, tenendo conto della direzione del gioco
    void advance();

    // Metodo che guarda il prossimo giocatore senza avanzare
    Player peekNextPlayer();

    //Getter della lista dei giocatori
    List<Player> getPlayers();

}