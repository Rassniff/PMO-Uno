package com.uno.model;

public class HumanPlayer extends Player {

    // Costruttore del giocatore umano
    public HumanPlayer(String name) {
        super(name);
    }

    // Il turno del giocatore umano viene gestito dal controller, quindi non implementiamo qui la logica di gioco.
    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        // Questo metodo viene implementato dal controller, non qui.
        throw new UnsupportedOperationException("Il turno del giocatore umano è gestito dal controller.");
    }
    
    // Metodo per verificare se il giocatore è un bot
    @Override
    public boolean isBot() {
        return false;
    }

    // La scelta del colore per il giocatore umano viene gestita dal controller, quindi non implementiamo qui la logica.
    @Override
    public Color chooseColor() {
        // Questo metodo viene implementato dal controller, non qui.
        throw new UnsupportedOperationException("La scelta colore per l'umano è gestita dal controller.");
    }
    
}
