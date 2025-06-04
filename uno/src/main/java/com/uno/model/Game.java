package com.uno.model;

import java.util.*;

import com.uno.model.interfaces.IGameListener;
import com.uno.model.interfaces.ITurnManager;

public class Game {
    private List<Player> players;       // Lista dei giocatori
    private Color currentColor;         // Colore della carta in gioco
    private CoveredDeck coveredDeck;    // Mazzo coperto
    private PlayedDeck playedDeck;      // Mazzo scoperto
    private ITurnManager turnManager;    // Gestore dei turni
    private Player winner = null;       // Flag per il vincitore del gioco
    
    private List<IGameListener> listeners = new ArrayList<>(); // Lista di ascoltatori per gli eventi del gioco
    private Map<Player, Integer> scores = new HashMap<>();   // Gestore punteggi giocatori
    
    // Costruttore del gioco che inizializza i giocatori, i mazzi e distribuisce le carte
    public Game(List<Player> players) {
        this.players = players;
        this.coveredDeck = new CoveredDeck();
        this.playedDeck = new PlayedDeck();
        this.turnManager = new TurnManager(players);
        
        // Inizializza a 0 i punteggi
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

    // Metodo per aggiungere gli ascoltatori al gioco
    public void addListener(IGameListener listener) {
        listeners.add(listener);
    }
    // Metodo che notifica gli ascoltatori quando il gioco finisce
    private void notifyGameOver(Player winner) {
        for (IGameListener l : listeners) {
            l.onGameOver(winner);
        }
    }
    // Metodo che notifica gli ascoltatori quando il turno cambia
    private void notifyTurnChanged(Player currentPlayer) {
        for (IGameListener l : listeners) {
            l.onTurnChanged(currentPlayer);
        }
    }
    // Metodo che notifica gli ascoltatori quando un giocatore chiama "Uno"
    public void notifyUnoCalled(Player player) {
        for (IGameListener l : listeners) {
            l.onUnoCalled(player);
        }
    }
    // Metodo che notifica gli ascoltatori quando il colore corrente cambia
    private void notifyColorChanged(Color newColor) {
        for (IGameListener l : listeners) {
            l.onColorChanged(newColor);
        }
    }

    // Metodo per giocare un turno con una carta specifica e un colore scelto
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
            winner = player; 
            notifyGameOver(winner);
            int roundPoints = calculatePoints(winner);
            int updatedScore = scores.get(winner) + roundPoints;
            scores.put(winner, updatedScore);
            
            System.out.println(winner.getName() + " ha vinto il round e guadagna " + roundPoints + " punti!");
            printScores();

            if (updatedScore >= 500) {
                System.out.println(winner.getName() + " ha raggiunto i 500 punti e vince la partita!");   
            }
            return true;
        }
        return false;
    }
    
    // Metodo per gestire le carte speciali
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
                setCurrentColor(chosenColor);
            } else {
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
                setCurrentColor(chosenColor);
            } else {
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
                setCurrentColor(chosenColor);
            } else {
                setCurrentColor(currentPlayer.chooseColor());
            }
        }
    }
}

    //Getter per la carta in cima al mazzo degli scarti
    public Card getTopCard() {
        return playedDeck.getLastCard();
    }

    // Getter per il colore corrente
    public Color getCurrentColor(){
        return currentColor;
    }

    // Setter per il colore corrente
    public void setCurrentColor(Color color){
        this.currentColor = color;
        notifyColorChanged(color);
    }
    
    // Metodo per pescare una carta per il giocatore corrente
    public Card drawCardFor(Player player) {
        Card c = coveredDeck.drawCard(playedDeck);
        
        if(c != null){
            player.drawCard(c);
        } else{
            for (IGameListener l : listeners) {
                l.onDrawPatta(); // Notifica che il mazzo è vuoto
            }
        }
        return c;
    }        

    // Getter per ottenere il gestore dei turni
    public ITurnManager getTurnManager(){
        return turnManager;
    }

    // Metodo per verificare se il giocatore corrente può giocare
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
    
    // Metodo per verificare se il gioco è finito
    public boolean isGameOver() {
        return winner != null;
    }

    // Getter per ottenere il vincitore del gioco
    public Player getWinner() {
        return winner;
    }

    // Getter per ottenere il giocatore corrente
    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

    // Getter per ottenere la lista dei giocatori
    public List<Player> getPlayers() {
        return players;
    }
    
    // Metodo per avanzare il turno al prossimo giocatore
    public void advanceTurn() {
        turnManager.advance();
        notifyTurnChanged(getCurrentPlayer());
    }

    // Getter per ottenere i punteggi dei giocatori
    public int getScoreForPlayer(Player player) {
        return scores.getOrDefault(player, 0);
    }

    // Metodo per calcolare i punti per il vincitore
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
    
    // Metodo per stampare la classifica attuale versione CLI
    private void printScores() {
        System.out.println("Classifica attuale:");
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            System.out.println("- " + entry.getKey().getName() + ": " + entry.getValue() + " punti");
        }
    }
    
    // Metodo per salvare i punteggi su file (non implementato)
    /* private void saveScoresToFile() {
    try (PrintWriter out = new PrintWriter("scores.txt")) {
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            out.println(entry.getKey().getName() + "," + entry.getValue());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }*/

    // Metodo per resettare il gioco per un nuovo round
    public void resetForNewRound() {
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
        winner = null;
        currentColor = firstCard.getColor();

        this.turnManager = new TurnManager(players);
        notifyTurnChanged(turnManager.getCurrentPlayer());
    }
}