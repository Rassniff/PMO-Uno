package com.uno.controller.interfaces;

import com.uno.model.Card;

public interface IGameController {

    // Metodo per giocare una carta
    void playCard(Card card);

    //Metodo per pescare una carta
    void onDrawCardClicked();

    //Metodo per chiamare "Uno"
    void onUnoClicked();
    
}
