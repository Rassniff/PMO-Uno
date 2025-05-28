package com.uno.model;

public interface GameListener {
    void onGameOver(Player winner);
    void onTurnChanged(Player currentPlayer);
    void onColorChanged(Color newColor);
    void onDrawPatta();
}
