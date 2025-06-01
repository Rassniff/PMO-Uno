package com.uno.model;

public class Card {
    private Color color;   // Colore della carta
    private int number;    // Numero della carta (0-9)

    // Costruttore per le carte numerate
    public Card(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    // Getter per il colore 
    public Color getColor() {
        return color;
    }

    // Getter per il numero
    public int getNumber() {
        return number;
    }

    // Controllo di carta speciale
    public boolean isWild() {
        return color == Color.SPECIAL;
    }

    // Metodo per ottenere il nome dell'immagine della carta
    public String getImageName() {
       return color.name().toLowerCase() + "_" + number + ".png";
    }

    // Metodo che verifica se una carta può essere legalmente giocata sopra un'altra (da finire manca lo stesso tipo di SPECIAL CARD) 
    public boolean isPlayableOn(Card topCard) {
        // Caso 1: stesso colore
        if (this.color.equals(topCard.color)) {
            return true;
        }
    
        // Caso 2: stesso valore
        if (this.number == topCard.number) {
            return true;
        }
    
        // Caso 3: carta jolly
        if (this.color.equals(Color.SPECIAL)) {
            return true;
        }

        // Altrimenti, non giocabile
        return false;
    }

    // Metodo per stampare i dettagli della carta
    @Override
    public String toString() {
        return color.name() + " " + number;
    }
}
