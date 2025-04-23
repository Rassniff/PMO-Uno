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

    public String getImageName() {
        // Carte speciali colorate: red_skip.png, green_draw.png, etc.
        // Carte jolly: wild.png, draw4.png, shuffle.png

        String colorPrefix = (getColor() != Color.SPECIAL) ? getColor().name().toLowerCase() + "_" : "";
        String imageName = "";

        switch (action) {
            case SKIP:  
                imageName = colorPrefix + "skip.png";
                break;
            case REVERSE:  
                imageName = colorPrefix + "reverse.png";
                break;
            case DRAW_TWO:  
                imageName = colorPrefix + "draw.png";
                break;
            case WILD:  
                imageName = "wild.png";
                break;
            case WILD_DRAW_FOUR:  
                imageName = "draw4.png";
                break;
            case SHUFFLE:  
                imageName = "shuffle.png";
                break;
        }
        return imageName;
    }


    // Metodo per stampare i dettagli della carta
    @Override
    public String toString() {
        return getColor().name() + " " + action.name();
    }
}
