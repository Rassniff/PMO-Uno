package com.uno.model;

import java.util.Scanner;

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
    private static Color chooseColor(Player player) {
        if (player.isBot()) {
            // Il bot sceglie un colore a caso
            return Color.values()[(int) (Math.random() * Color.values().length)];
        } else {
            // Il giocatore umano può scegliere un colore tramite input
            Scanner scanner = new Scanner(System.in);
            System.out.println("Scegli un colore: ");
            System.out.println("1 - Blu");
            System.out.println("2 - Rosso");
            System.out.println("3 - Verde");
            System.out.println("4 - Giallo");

            // Ciclo per validare l'input
            int scelta = -1;
            while (scelta < 1 || scelta > 4) {
                System.out.print("Inserisci il numero del colore desiderato (1-4): ");
                if (scanner.hasNextInt()) {
                    scelta = scanner.nextInt();
                } else {
                    scanner.next(); // Scarta l'input non valido
                }
            }

            // Restituisce il colore corrispondente alla scelta
            return Color.values()[scelta - 1]; // Sottrai 1 per adattarlo all'indice dell'array Color
        }
    }
}
