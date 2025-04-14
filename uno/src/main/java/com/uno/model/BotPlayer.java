package com.uno.model;

import java.util.HashMap;
import java.util.Map;

public class BotPlayer extends Player {

    public BotPlayer(String name) {
        super(name);
    }

    // Metodo per far giocare i bot
    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        for (Card card : hand) {
            if (TurnManager.isPlayable(card, topCard, currentColor)) {
                hand.remove(card);
                System.out.println(name + " gioca: " + card);
                return card;
            }
        }
        System.out.println(name + " pesca una carta.");
        return null;
    }

    @Override
    public boolean isBot() {
        return true;
    }

    @Override
    public Color chooseColor() {
        Map<Color, Integer> colorCount = new HashMap<>();

        // Conta i colori nella mano
        for (Card card : hand) {
            if (card.getColor() != Color.SPECIAL) {
                colorCount.put(card.getColor(), colorCount.getOrDefault(card.getColor(), 0) + 1);
            }
        }

        // Scegli il colore con il maggior numero di carte
        return colorCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Color.RED); // fallback se mano vuota
    }
}
