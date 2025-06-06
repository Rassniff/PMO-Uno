package com.uno.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BotPlayer extends Player {

    // Costruttore del giocatore bot
    public BotPlayer(String name) {
        super(name);
    }

    // Metodo per far giocare il turno del bot
    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        for (Card card : new ArrayList<>(hand)) {
            if (TurnManager.isPlayable(card, topCard, currentColor)) {
            	//regola per il +4
            	if(card instanceof SpecialCard special &&
            	   special.getAction() == Action.WILD_DRAW_FOUR &&
            	   !canPlayWildDrawFour(topCard, currentColor)) {
            		continue;
            	}
            	
                hand.remove(card);
                System.out.println(name + " gioca: " + card);
                return card;
            }
        }
        System.out.println(name + " pesca una carta.");
        return null;
    }
    
    // Metodo per verificare se il bot è un bot
    @Override
    public boolean isBot() {
        return true;
    }

    // Metodo per far scegliere il colore da un bot
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
