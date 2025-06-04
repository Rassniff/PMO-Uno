package com.uno.model.interfaces;

import com.uno.model.Color;
import com.uno.model.Player;

// Interfaccia per ascoltare gli eventi del gioco UNO
public interface IGameListener {
    
    // Evento chiamato quando il gioco è finito, con il vincitore
    void onGameOver(Player winner); 
    
    // Evento chiamato quando il turno cambia, con il giocatore corrente
    void onTurnChanged(Player currentPlayer); 
    
    // Evento chiamato quando il colore corrente cambia
    void onColorChanged(Color newColor); 
    
    // Evento chiamato quando si verifica una situazione di pareggio (draw patta)
    void onDrawPatta(); 
    
    // Evento chiamato quando un giocatore chiama "Uno"
    void onUnoCalled(Player player); 
}
