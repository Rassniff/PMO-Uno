package com.uno.model;

public class Card{
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


    // Metodo per stampare i dettagli della carta
    @Override
    public String toString() {
        return color.name() + " " + number;
    }
}
