package com.uno.model;

public class SpecialCard extends Card {
    private Action action;  // Tipo di azione

    // Costruttore per carte speciali
    public SpecialCard(Color color, Action action) {
        super(color, -1);  // Le carte speciali non hanno numero
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
