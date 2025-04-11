package com.uno.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        Scanner scanner = new Scanner(System.in);

        List<Card> playable = new ArrayList<>();
        for (Card c : hand) {
            if (Rules.isPlayable(c, topCard, currentColor)) {
                playable.add(c);
            }
        }

        if (playable.isEmpty()) {
            return null; // deve pescare
        }

        System.out.println("Carte giocabili:");
        for (int i = 0; i < playable.size(); i++) {
            System.out.println((i + 1) + ": " + playable.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > playable.size()) {
            System.out.print("Scegli una carta da giocare (1-" + playable.size() + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > playable.size()) {
                    System.out.println("Scelta non valida. Riprova.");
                }
            } else {
                System.out.println("Inserisci un numero valido.");
                scanner.next(); // Consuma l’input non numerico
            }
        }

        return playable.get(choice - 1);
    }

    @Override
    public boolean isBot() {
        return false;
    }
}
