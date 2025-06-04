package com.uno.model.interfaces;

import com.uno.model.Card;

public interface IPlayableCard {

    // Metodo che verifica se una carta può essere giocata su un'altra
    boolean isPlayableOn(Card topCard);
    
}