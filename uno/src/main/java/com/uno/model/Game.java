package com.uno.model;

import java.util.*;

public class Game {
    private List<Player> players;       // Lista dei giocatori
    private int currentPlayerIndex;     // Indice del giocatore corrente
    private Color currentColor;         // Colore della carta in gioco
    private CoveredDeck coveredDeck;    // Mazzo coperto
    private PlayedDeck playedDeck;      // Mazzo scoperto
    private boolean isClockwise = true; // Verso del turno

    public Game(List<Player> players) {
        this.players = players;
        this.coveredDeck = new CoveredDeck();
        this.playedDeck = new PlayedDeck();
        this.currentPlayerIndex = 0;

        // Distribuisci 7 carte a ogni giocatore
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(coveredDeck.drawCard());
            }
        }

        // Pesca la prima carta da mettere in cima alla pila degli scarti
        Card firstCard = coveredDeck.drawCard();
        while (firstCard.isWild()) {  // Evita di iniziare con un jolly
            coveredDeck.addCard(firstCard);
            coveredDeck.shuffle();
            firstCard = coveredDeck.drawCard();
        }
        playedDeck.addCard(firstCard);
        currentColor = firstCard.getColor();
    }

    public void playGame() {
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            Card topCard = playedDeck.getLastCard();

            System.out.println("\nCarta sul tavolo: " + topCard + " (Colore attuale: " + currentColor + ")");
            System.out.println(currentPlayer.getName() + " ha in mano: " + currentPlayer.getHand());

            Card chosenCard = currentPlayer.playTurn(topCard, currentColor);

            if (chosenCard != null) {
                currentPlayer.removeCard(chosenCard);
                playedDeck.addCard(chosenCard);
                currentColor = chosenCard.getColor(); // aggiornare solo se non è jolly, altrimenti chiedi all’utente
                System.out.println(currentPlayer.getName() + " ha giocato: " + chosenCard);

                if (currentPlayer.isHandEmpty()) {
                    System.out.println(currentPlayer.getName() + " ha vinto!");
                    break;
                }

                // gestione base delle carte speciali (puoi estendere)
                if (chosenCard instanceof SpecialCard) {
                    SpecialCard special = (SpecialCard) chosenCard;
                    switch (special.getAction()) {
                        case REVERSE -> isClockwise = !isClockwise;
                        case SKIP -> nextPlayer();
                        case DRAW_TWO -> {
                            nextPlayer();
                            players.get(currentPlayerIndex).drawCard(coveredDeck.drawCard());
                            players.get(currentPlayerIndex).drawCard(coveredDeck.drawCard());
                        }
                        case WILD_DRAW_FOUR -> {
                            nextPlayer();
                            for (int i = 0; i < 4; i++) {
                                players.get(currentPlayerIndex).drawCard(coveredDeck.drawCard());
                            }
                        }
                        case SHUFFLE -> {
                            System.out.println("Carta SHUFFLE giocata! Tutte le mani vengono mischiate.");
                        
                            // 1. Raccogli tutte le carte dalle mani
                            List<Card> allCards = new ArrayList<>();
                            Map<Player, Integer> cardCountPerPlayer = new HashMap<>();
                        
                            for (Player p : players) {
                                int count = p.getHand().size();
                                cardCountPerPlayer.put(p, count);
                                allCards.addAll(p.getHand());
                                p.getHand().clear();  // svuota la mano
                            }
                        
                            // 2. Mischia tutte le carte
                            Collections.shuffle(allCards);
                        
                            // 3. Ridistribuisci le carte
                            Iterator<Card> iterator = allCards.iterator();
                            for (Player p : players) {
                                int cardsToGive = cardCountPerPlayer.get(p);
                                for (int i = 0; i < cardsToGive && iterator.hasNext(); i++) {
                                    p.drawCard(iterator.next());
                                }
                            }
                        }
                        case WILD -> {} // solo cambia colore
                    }
                }

            } else {
                // pesca una carta
                Card drawn = coveredDeck.drawCard();
                System.out.println(currentPlayer.getName() + " pesca: " + drawn);
                currentPlayer.drawCard(drawn);
            }

            nextPlayer();
        }
    }

    private void nextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }
}