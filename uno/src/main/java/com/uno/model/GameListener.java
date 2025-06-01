package com.uno.model;

// Interfaccia per ascoltare gli eventi del gioco UNO
public interface GameListener {
    void onGameOver(Player winner); // Evento chiamato quando il gioco è finito, con il vincitore
    void onTurnChanged(Player currentPlayer); // Evento chiamato quando il turno cambia, con il giocatore corrente
    void onColorChanged(Color newColor); // Evento chiamato quando il colore corrente cambia
    void onDrawPatta(); // Evento chiamato quando si verifica una situazione di pareggio (draw patta)
    void onUnoCalled(Player player); // Evento chiamato quando un giocatore chiama "Uno"
}
