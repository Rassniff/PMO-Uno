package com.uno.model;

public class Rules {

    // Smeplificata per i test, è da cambiare
    public static boolean isPlayable(Card toPlay, Card topCard, Color currentColor) {
        // Se è un jolly, sempre giocabile
        if (toPlay instanceof SpecialCard) {
            Action action = ((SpecialCard) toPlay).getAction();
            return action == Action.WILD || action == Action.WILD_DRAW_FOUR || action == Action.SHUFFLE
                    || toPlay.getColor() == currentColor;
        }

        // Per le carte numeriche: stesso colore o stesso numero
        return toPlay.getColor() == currentColor
                || (topCard instanceof Card && toPlay instanceof Card && toPlay.getNumber() == ((Card) topCard).getNumber());
    }
}
