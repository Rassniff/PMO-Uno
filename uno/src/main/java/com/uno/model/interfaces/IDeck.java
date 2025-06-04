package com.uno.model.interfaces;

import com.uno.model.Card;

public interface IDeck {

    // Metodo per controllare il numero di carte nel mazzo
    int size();

    // Metodo per aggiungere una carta al mazzo
    void addCard(Card card);

}