package com.uno.model;

import java.util.Map;

public class SpecialCard extends Card {
    private static final Map<Action, String> actionToImage = Map.of(
        Action.SKIP, "skip.png",
        Action.REVERSE, "reverse.png",
        Action.DRAW_TWO, "draw.png",
        Action.WILD, "wild.png",
        Action.WILD_DRAW_FOUR, "draw4.png",
        Action.SHUFFLE, "shuffle.png"
        ); // Mappa per associare azioni a nomi di immagini
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

    // Metodo per ottenere il nome dell'immagine della carta speciale
    public String getImageName() {   
        String colorPrefix = (getColor() != Color.SPECIAL) ? getColor().name().toLowerCase() + "_" : "";
        String imageName = actionToImage.get(action);
        
        return colorPrefix + imageName;
    }

    // Metodo per stampare i dettagli della carta speciale
    @Override
    public String toString() {
        return getColor().name() + " " + action.name();
    }
}
