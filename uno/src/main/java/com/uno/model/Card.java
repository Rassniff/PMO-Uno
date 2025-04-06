package com.uno.model;

// Gli unici colori disponibili (rosso, giallo, verde, blu e speciali)
enum Color {
    RED, YELLOW, GREEN, BLUE, SPECIAL
}

public class Card {
    private Color color;   // Colore della carta
    private int number;    // Numero della carta (0-9)

    // Costruttore per le carte numerate
    public Card(Color color, int number) {
        if (color == Color.SPECIAL) {
            throw new IllegalArgumentException("Le carte speciali non hanno numero.");
        }
        this.color = color;
        this.number = number;
    }

    // Getter per colore e numero
    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    // Metodo per stampare i dettagli della carta
    @Override
    public String toString() {
        return color.name() + " " + number;
    }

    // Controllo di carta speciale
    public boolean isWild() {
        return color == Color.SPECIAL;
    }
}
