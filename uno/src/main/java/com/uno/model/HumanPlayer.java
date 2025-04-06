package com.uno.model;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        
        return null; 
    }

    @Override
    public boolean isBot() {
        return false;
    }
}
