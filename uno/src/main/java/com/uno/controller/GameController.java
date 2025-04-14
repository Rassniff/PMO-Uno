package com.uno.controller;

import com.uno.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.*;

public class GameController {

    @FXML private Label gameStatusLabel;
    @FXML private Label topCardLabel;
    @FXML private HBox playerHand;

    private Game game;
    private Player currentPlayer;

    @FXML
    public void initialize() {
        // Setup iniziale
        List<Player> players = List.of(new HumanPlayer("Tu"), new BotPlayer("Bot"));
        game = new Game(players);
        currentPlayer = players.get(0); // Il giocatore umano

        handleBotTurns();
        updateUI();
        
    }

    private void updateUI() {
        topCardLabel.setText(game.getTopCard().toString());

        playerHand.getChildren().clear();
        for (Card card : currentPlayer.getHand()) {
            Button cardBtn = new Button(card.toString());
            cardBtn.setOnAction(e -> playCard(card));
            playerHand.getChildren().add(cardBtn);
        }
    }

    private void playCard(Card card) {
        if (card.isPlayableOn(game.getTopCard())) {
            currentPlayer.removeCard(card);
            game.playCard(card);
            gameStatusLabel.setText("Hai giocato: " + card);
    
            // Avanza al turno successivo
            game.getTurnManager().advance();
            handleBotTurns(); // Nuova funzione che vediamo sotto
    
        } else {
            gameStatusLabel.setText("Carta non giocabile!");
        }
    
        updateUI();
    }

    @FXML
    private void handleDrawCard() {
        Card drawn = game.drawCardFor(currentPlayer);
        gameStatusLabel.setText("Hai pescato: " + drawn);
    
        // Dopo aver pescato, passa il turno
        game.getTurnManager().advance();
        handleBotTurns();
    
        updateUI();
    }

    private void handleBotTurns() {
        Player current = game.getTurnManager().getCurrentPlayer();
    
        while (current instanceof BotPlayer) {
            BotPlayer bot = (BotPlayer) current;
    
            bot.playTurn(game.getTopCard(), game.getCurrentColor()); // Qui il bot gioca la sua carta o pesca
            gameStatusLabel.setText(bot.getName() + " ha giocato.");
    
            game.getTurnManager().advance();
            current = game.getTurnManager().getCurrentPlayer();
        }
    
        currentPlayer = current;
        updateUI();
    }
}
