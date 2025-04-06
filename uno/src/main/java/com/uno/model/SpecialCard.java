package com.uno.model;

// Enum per azione (salta, cambia giro, pesca 2, jolly, jolly pesca 4, mischiatutto)
enum Action {
    SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR, SHUFFLE
}

public class SpecialCard extends Card {
    private Action action;  // Tipo di azione

    // Costruttore per carte speciali
    public SpecialCard(Color color, Action action) {
        super(color, -1);  // Le carte speciali non hanno numero
        if (color == Color.SPECIAL && (action != Action.WILD && action != Action.WILD_DRAW_FOUR && action != Action.SHUFFLE)) {
            throw new IllegalArgumentException("Le carte jolly e mischiatutto non hanno colore");
        }
        this.action = action;
    }

    // Getter per l'azione
    public Action getAction() {
        return action;
    }

    // Metodo per stampare i dettagli della carta
    @Override
    public String toString() {
        return getColor().name() + " " + action.name();
    }
}
