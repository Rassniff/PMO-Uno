package com.uno.model;

import java.util.*;

public class Game {
    private List<Player> players;       // Lista dei giocatori
    private Color currentColor;         // Colore della carta in gioco
    private CoveredDeck coveredDeck;    // Mazzo coperto
    private PlayedDeck playedDeck;      // Mazzo scoperto
    private TurnManager turnManager;    // Gestore dei turni
    private Player winner = null;
    private List<GameListener> listeners = new ArrayList<>();
    private Map<Player, Integer> scores = new HashMap<>(); // Gestore punteggi giocatori
    
    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    private void notifyGameOver(Player winner) {
        for (GameListener l : listeners) {
            l.onGameOver(winner);
        }
    }

    private void notifyTurnChanged(Player currentPlayer) {
        for (GameListener l : listeners) {
            l.onTurnChanged(currentPlayer);
        }
    }

    private void notifyColorChanged(Color newColor) {
        for (GameListener l : listeners) {
            l.onColorChanged(newColor);
        }
    }



    public Game(List<Player> players) {
        this.players = players;
        this.coveredDeck = new CoveredDeck();
        this.playedDeck = new PlayedDeck();
        this.turnManager = new TurnManager(players);
        //inizializza a 0 i punteggi
        for (Player player : players) {
            scores.put(player, 0);
        }

        // Distribuisci 7 carte a ogni giocatore
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(coveredDeck.drawCard(playedDeck));
            }
        }

        // Pesca la prima carta da mettere in cima alla pila degli scarti
        Card firstCard = coveredDeck.drawCard(playedDeck);
        while (firstCard.isWild()) {  // Evita di iniziare con un jolly
            coveredDeck.addCard(firstCard);
            coveredDeck.shuffle();
            firstCard = coveredDeck.drawCard(playedDeck);
        }
        playedDeck.addCard(firstCard);
        currentColor = firstCard.getColor();
    }

    /*private void handleSpecialCard(SpecialCard card, Player currentPlayer) {
        switch (card.getAction()) {
            case REVERSE -> {
                turnManager.reverseDirection();
                System.out.println("Direzione invertita!");
            }
    
            case SKIP -> {
                turnManager.advance();
                System.out.println("Salto del turno!");
            }
    
            case DRAW_TWO -> {
                turnManager.advance();
                Player next = turnManager.getCurrentPlayer();
                System.out.println(next.getName() + " pesca 2 carte.");
                next.drawCard(coveredDeck.drawCard(playedDeck));
                next.drawCard(coveredDeck.drawCard(playedDeck));
            }
    
            case WILD_DRAW_FOUR -> {
                currentColor = currentPlayer.chooseColor();
                turnManager.advance();
                Player next = turnManager.getCurrentPlayer();
                System.out.println(next.getName() + " pesca 4 carte.");
                for (int i = 0; i < 4; i++) {
                    next.drawCard(coveredDeck.drawCard(playedDeck));
                }
            }
    
            case WILD -> {
                currentColor = currentPlayer.chooseColor();
            }
    
            case SHUFFLE -> {
                System.out.println("Carta SHUFFLE giocata! Tutte le mani vengono mischiate.");
    
                List<Card> allCards = new ArrayList<>();
                Map<Player, Integer> cardCountPerPlayer = new HashMap<>();
    
                for (Player p : players) {
                    int count = p.getHand().size();
                    cardCountPerPlayer.put(p, count);
                    allCards.addAll(p.getHand());
                    p.getHand().clear();
                }
    
                Collections.shuffle(allCards);
                Iterator<Card> iterator = allCards.iterator();
                for (Player p : players) {
                    int cardsToGive = cardCountPerPlayer.get(p);
                    for (int i = 0; i < cardsToGive && iterator.hasNext(); i++) {
                        p.drawCard(iterator.next());
                    }
                }
    
                currentColor = currentPlayer.chooseColor();
            }
        }
    } */

    /*public void playGame() {
        while (true) {
            Player currentPlayer = turnManager.getCurrentPlayer();
            Card topCard = playedDeck.getLastCard();

            System.out.println("\nCarta sul tavolo: " + topCard + " (Colore attuale: " + currentColor + ")");
            System.out.println(currentPlayer.getName() + " ha in mano: " + currentPlayer.getHand());

            Card chosenCard = currentPlayer.playTurn(topCard, currentColor);

            if (chosenCard != null) {
                currentPlayer.removeCard(chosenCard);
                playedDeck.addCard(chosenCard);

                System.out.println(currentPlayer.getName() + " ha giocato: " + chosenCard);

                if (currentPlayer.isHandEmpty()) {
                    System.out.println(currentPlayer.getName() + " ha vinto!");
                    break;
                }

                // gestione base delle carte speciali 
                if (chosenCard instanceof SpecialCard specialCard) {
                    handleSpecialCard(specialCard, currentPlayer);
                }
            } else {
                // pesca una carta
                Card drawn = coveredDeck.drawCard(playedDeck);
                System.out.println(currentPlayer.getName() + " pesca: " + drawn);

                // Verifica se può essere giocata subito
                if (TurnManager.isPlayable(drawn, topCard, currentColor)) {
                    System.out.println(currentPlayer.getName() + " gioca la carta appena pescata: " + drawn);
                    playedDeck.addCard(drawn);
                    currentColor = (drawn.getColor() == Color.SPECIAL) ? currentPlayer.chooseColor() : drawn.getColor();

                    if (drawn instanceof SpecialCard specialCard) {
                        handleSpecialCard(specialCard, currentPlayer);
                    }
                } else {
                    currentPlayer.drawCard(drawn); // la tiene in mano
                }
            }

            try {
                Thread.sleep(2000); // Pausa di 2 secondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione del thread
                System.out.println("Il thread è stato interrotto.");
            }

            turnManager.advance();
        }

        

    }*/

    public boolean playTurn(Player player, Card card, Color chosenColor) {
        // Rimuovi la carta dalla mano
        player.removeCard(card);

        // Gioca la carta sul mazzo
        playedDeck.addCard(card);

        // Gestione colore corrente
        if (card instanceof SpecialCard specialCard) {
            // Se è una wild, usa il colore scelto
            if (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE) {
                if (chosenColor != null) {
                    currentColor = chosenColor;
                }
            } else {
                currentColor = card.getColor();
            }
            // Applica effetti speciali
            handleSpecialCard(specialCard, player, chosenColor);
        } else {
            currentColor = card.getColor();
        }

        if (player.isHandEmpty()) {
            winner = player; // <-- Qui salvi il vincitore
            int roundPoints = calculatePoints(winner);
            int updatedScore = scores.get(winner) + roundPoints;
            scores.put(winner, updatedScore);
            
            System.out.println(winner.getName() + " ha vinto il round e guadagna " + roundPoints + " punti!");
            printScores();

            notifyGameOver(winner);
            
            if (updatedScore >= 500) {
                System.out.println(winner.getName() + " ha raggiunto i 500 punti e vince la partita!");
                // eventualmente potresti voler stoppare il gioco o notificare qualcosa
            } else {
                //resetForNewRound(winner);
                winner = null; // reset per la prossima partita
            }
            
            return true;
        }
        return false;
    }
    
    private void handleSpecialCard(SpecialCard card, Player currentPlayer, Color chosenColor) {
        
        if ((card.getAction() == Action.WILD || card.getAction() == Action.WILD_DRAW_FOUR || card.getAction() == Action.SHUFFLE)
            && chosenColor == null && !currentPlayer.isBot()) {
                chosenColor = Color.RED; // Default di sicurezza per l'umano
        }
    
        switch (card.getAction()) {
        case REVERSE -> {
            turnManager.reverseDirection();
            System.out.println("Direzione invertita!");
        }

        case SKIP -> {
            turnManager.advance();
            System.out.println("Salto del turno!");
        }

        case DRAW_TWO -> {
            turnManager.advance();
            Player next = turnManager.getCurrentPlayer();
            System.out.println(next.getName() + " pesca 2 carte.");
            next.drawCard(coveredDeck.drawCard(playedDeck));
            next.drawCard(coveredDeck.drawCard(playedDeck));
        }

        case WILD_DRAW_FOUR -> {
            if (chosenColor != null) {
                //currentColor = chosenColor;
                setCurrentColor(chosenColor);
            } else {
                //currentColor = currentPlayer.chooseColor();
                setCurrentColor(currentPlayer.chooseColor());
            }
            turnManager.advance();
            Player next = turnManager.getCurrentPlayer();
            System.out.println(next.getName() + " pesca 4 carte.");
            for (int i = 0; i < 4; i++) {
                next.drawCard(coveredDeck.drawCard(playedDeck));
            }
        }

        case WILD -> {
            if (chosenColor != null) {
                //currentColor = chosenColor;
                setCurrentColor(chosenColor);
            } else {
                //currentColor = currentPlayer.chooseColor();
                setCurrentColor(currentPlayer.chooseColor());
            }
        }

        case SHUFFLE -> {
            System.out.println("Carta SHUFFLE giocata! Tutte le mani vengono mischiate.");

            List<Card> allCards = new ArrayList<>();
            Map<Player, Integer> cardCountPerPlayer = new HashMap<>();

            for (Player p : players) {
                int count = p.getHand().size();
                cardCountPerPlayer.put(p, count);
                allCards.addAll(p.getHand());
                p.getHand().clear();
            }

            Collections.shuffle(allCards);
            Iterator<Card> iterator = allCards.iterator();
            for (Player p : players) {
                int cardsToGive = cardCountPerPlayer.get(p);
                for (int i = 0; i < cardsToGive && iterator.hasNext(); i++) {
                    p.drawCard(iterator.next());
                }
            }

            if (chosenColor != null) {
                //currentColor = chosenColor;
                setCurrentColor(chosenColor);
            } else {
                //currentColor = currentPlayer.chooseColor();
                setCurrentColor(currentPlayer.chooseColor());
            }
        }
    }
}

    //////////////////////////////////////////////implementazione con javafx
    public Card getTopCard() {
        return playedDeck.getLastCard();
    }

    public Color getCurrentColor(){
        return currentColor;
    }

    public void setCurrentColor(Color color){
        this.currentColor = color;
        notifyColorChanged(color);
    }
    
    public void playCard(Card card) {
        playedDeck.addCard(card);
        currentColor = card.getColor();
    }
    
    public Card drawCardFor(Player player) {
        Card c = coveredDeck.drawCard(playedDeck);
        //player.drawCard(c);
        if(c != null){
            player.drawCard(c);
        } else{
            for (GameListener l : listeners) {
                l.onDrawPatta(); // Notifica che il mazzo è vuoto
            }
        }
        return c;
    }        

    public TurnManager getTurnManager(){
        return turnManager;
    }

    public void handleSpecialCardExternally(SpecialCard card, Player currentPlayer, Color chosenColor) {
        handleSpecialCard(card, currentPlayer, chosenColor);
    }

    public boolean canCurrentPlayerPlay() {
        Player currentPlayer = turnManager.getCurrentPlayer();
        Card topCard = getTopCard();
        Color color = getCurrentColor();
    
        for (Card card : currentPlayer.getHand()) {
            if (TurnManager.isPlayable(card, topCard, color)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isGameOver() {
        return winner != null;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

    public void advanceTurn() {
        turnManager.advance();
        notifyTurnChanged(getCurrentPlayer());
    }


    //calcoliamo i punteggi
    private int calculatePoints(Player winner) {
        int total = 0;
        for (Player player : players) {
            if (player != winner) {
                for (Card card : player.getHand()) {
                    if (card instanceof SpecialCard special) {
                        switch (special.getAction()) {
                            case WILD, WILD_DRAW_FOUR, SHUFFLE -> total += 50;
                            default -> total += 20;
                        }
                    } else {
                        total += card.getNumber();
                    }
                }
            }
        }
        return total;
    }
    //printiamo i punteggi da terminale
    private void printScores() {
        System.out.println("Classifica attuale:");
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            System.out.println("- " + entry.getKey().getName() + ": " + entry.getValue() + " punti");
        }
    }
    /*
     * private void saveScoresToFile() {
    try (PrintWriter out = new PrintWriter("scores.txt")) {
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            out.println(entry.getKey().getName() + "," + entry.getValue());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}*/

    /* 
    private void resetForNewRound(Player winner) {
        for (Player player : players) {
            player.clearHand();
        }

        this.coveredDeck = new CoveredDeck();
        this.playedDeck = new PlayedDeck();

        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(coveredDeck.drawCard(playedDeck));
            }
        }

        Card firstCard = coveredDeck.drawCard(playedDeck);
        while (firstCard.isWild()) {
            coveredDeck.addCard(firstCard);
            coveredDeck.shuffle();
            firstCard = coveredDeck.drawCard(playedDeck);
        }
        playedDeck.addCard(firstCard);
        currentColor = firstCard.getColor();

        this.turnManager = new TurnManager(players);
        notifyTurnChanged(turnManager.getCurrentPlayer());
    }
    */
}