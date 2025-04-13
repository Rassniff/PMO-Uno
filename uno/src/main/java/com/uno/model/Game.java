package com.uno.model;

import java.util.*;

public class Game {
    private List<Player> players;       // Lista dei giocatori
    private Color currentColor;         // Colore della carta in gioco
    private CoveredDeck coveredDeck;    // Mazzo coperto
    private PlayedDeck playedDeck;      // Mazzo scoperto
    private TurnManager turnManager;    // Gestore dei turni

    public Game(List<Player> players) {
        this.players = players;
        this.coveredDeck = new CoveredDeck();
        this.playedDeck = new PlayedDeck();
        this.turnManager = new TurnManager(players);

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

    private void handleSpecialCard(SpecialCard card, Player currentPlayer) {
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
    }    

    public void playGame() {
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

                // gestione base delle carte speciali (puoi estendere)
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
    }
}