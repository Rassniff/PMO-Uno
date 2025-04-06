package com.uno.model;

public class BotPlayer extends Player {

    public BotPlayer(String name) {
        super(name);
    }

    // Metodo per far giocare i bot
    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        for (Card card : hand) {
            /*if (GameRules.isPlayable(card, topCard, currentColor)) {
                hand.remove(card);
                System.out.println(name + " gioca: " + card);
                return card;
            }*/
        }
        System.out.println(name + " pesca una carta.");
        return null;
    }

    @Override
    public boolean isBot() {
        return true;
    }
}
