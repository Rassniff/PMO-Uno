package com.uno.model;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card playTurn(Card topCard, Color currentColor) {
        /*Scanner scanner = new Scanner(System.in);

        List<Card> playable = new ArrayList<>();
        for (Card c : hand) {
            if (TurnManager.isPlayable(c, topCard, currentColor)) {
            	if (c instanceof SpecialCard special && special.getAction() == Action.WILD_DRAW_FOUR) {
                    if (!canPlayWildDrawFour(topCard, currentColor)) {
                        continue;
                    }
                }
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

                if (choice >= 1 && choice <= playable.size()) {
                    Card selected = playable.get(choice - 1);

                    if (selected instanceof SpecialCard special &&
                        special.getAction() == Action.WILD_DRAW_FOUR &&
                        !canPlayWildDrawFour(topCard, currentColor)) {

                        System.out.println("⚠️ Non puoi giocare il +4 se hai altre carte valide!");
                        continue; // torna a chiedere la scelta
                    }

                    hand.remove(selected);
                    return selected;
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                }
            } else {
                System.out.println("Inserisci un numero valido.");
                scanner.next(); // Consuma input non numerico
            }
        }
        */
        return null; // fallback sicuro
        
    }
    

    @Override
    public boolean isBot() {
        return false;
    }

    //da implementare
    @Override
    public Color chooseColor() {

        throw new UnsupportedOperationException("La scelta colore per l'umano è gestita dal controller.");
    }
    

    //Questo metodo funziona solo per la versione CLI
    /* @Override
    public Color chooseColor() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Scegli un colore: ");
        System.out.println("1: ROSSO\n2: GIALLO\n3: VERDE\n4: BLU");

        int choice;
        while (true) {
            try {
                System.out.print("Inserisci il numero del colore: ");
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) break;
                System.out.println("Scelta non valida. Riprova.");
            } catch (Exception e) {
                System.out.println("Input non valido. Riprova.");
                scanner.nextLine(); // pulisce il buffer
            }
        }

        return switch (choice) {
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            case 3 -> Color.GREEN;
            case 4 -> Color.BLUE;
            default -> Color.RED; // fallback sicuro
        };
    }*/
}
