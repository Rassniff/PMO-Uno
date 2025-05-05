package com.uno.controller;

import com.uno.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.HBox;

import java.util.List;

public class GameController {

    //@FXML private HBox playerHandBox;
    @FXML private FlowPane playerHandBox;
    @FXML private ImageView topCardImage;
    @FXML private Button drawButton;
    @FXML private Label statusText;

    private Game game;
    private Player humanPlayer;

    public void initializeGame(List<Player> players) {
        game = new Game(players);
        humanPlayer = players.get(0); // supponiamo che il primo sia il giocatore umano        
        updateUI();
        startTurnLoop();
    }

    private void startTurnLoop() {
        Player currentPlayer = game.getTurnManager().getCurrentPlayer();
        
        if (currentPlayer.isBot()) {
            handleBotTurn((BotPlayer) currentPlayer);
        } else {
            statusText.setText("È il tuo turno!");
            //drawButton.setDisable(false);
        }
    }

    private void handleBotTurn(BotPlayer bot) {
        drawButton.setDisable(true);
        statusText.setText("Turno di " + bot.getName() + "...");

        // Delay per far sembrare che il bot "pensasse"
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                Card topCard = game.getTopCard();
                Card playedCard = bot.playTurn(topCard, game.getCurrentColor());

                if (playedCard != null) {
                    game.playCard(playedCard);
                    if (playedCard instanceof SpecialCard specialCard) {
                        game.handleSpecialCardExternally(specialCard, bot);

                        // Mostra il colore scelto se è una WILD o DRAW_4
                        if (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR) {
                            Color chosenColor = game.getCurrentColor();
                            statusText.setText(bot.getName() + " ha scelto il colore " + chosenColor.name());
                        }

                    }
                } else {
                    Card drawn = game.drawCardFor(bot);
                    if (TurnManager.isPlayable(drawn, topCard, game.getCurrentColor())) {
                        game.playCard(drawn);
                        if (drawn instanceof SpecialCard specialCard) {
                            game.handleSpecialCardExternally(specialCard, bot);
                        }
                    }
                }

                if (bot.isHandEmpty()) {
                    statusText.setText(bot.getName() + " ha vinto!");
                    drawButton.setDisable(true);
                    return;
                }

                game.getTurnManager().advance();
                updateUI();
                startTurnLoop(); // Prossimo turno
            });
        }).start();
    }

    @FXML
    private void onDrawCardClicked() {
        Player currentPlayer = game.getTurnManager().getCurrentPlayer();
        if (!currentPlayer.equals(humanPlayer)) return;

         // 🔒 Blocco di sicurezza aggiuntivo
        if (game.canCurrentPlayerPlay()) {
            // Opzionale: mostra messaggio che non può pescare se ha carte valide
            statusText.setText("Hai carte giocabili! Non puoi pescare.");
            return;
        }

        Card drawn = game.drawCardFor(humanPlayer);
        Card topCard = game.getTopCard();

        if (TurnManager.isPlayable(drawn, topCard, game.getCurrentColor())) {
            playCard(drawn);
        } else {
            //updateUI();
            game.getTurnManager().advance();
            startTurnLoop();
        }
    }

    private void playCard(Card card) {
        humanPlayer.removeCard(card);
        game.playCard(card);
        statusText.setText("Hai giocato: " + card.toString());


        if (card instanceof SpecialCard specialCard) {
            game.handleSpecialCardExternally(specialCard, humanPlayer);
        }

        updateUI();

        if (humanPlayer.isHandEmpty()) {
            statusText.setText("Hai vinto!");
            drawButton.setDisable(true);
            return;
        }

        game.getTurnManager().advance();
        startTurnLoop();
    }

    private void updateUI() {
        updateTopCardImage();
        updateHand();

        // Disabilita il bottone se il giocatore ha carte giocabili
        boolean canPlay = game.canCurrentPlayerPlay();
        drawButton.setDisable(canPlay);
    }

    private void updateTopCardImage() {
        Card topCard = game.getTopCard();
        String imageName = topCard.getImageName(); // assicurati che questo metodo esista
        Image image = new Image(getClass().getResourceAsStream("/com/uno/images/cards/" + imageName));
        topCardImage.setImage(image);
    }

    private void updateHand() {
        playerHandBox.getChildren().clear();
        Card topCard = game.getTopCard();
        Color currentColor = game.getCurrentColor();

        int handSize = humanPlayer.getHand().size();
        int maxCardsPerRow = Math.max(7, handSize); // o fissa 7 se preferisci
        double spacingCompensation = 1.1; // Per compensare gli hgap/vgap

        for (Card card : humanPlayer.getHand()) {
            ImageView cardView = new ImageView();
            //cardView.setFitHeight(120);
            //cardView.setPreserveRatio(true);

            cardView.setPreserveRatio(true);

            // Responsive fitWidth legato al contenitore
            cardView.fitWidthProperty().bind(
                playerHandBox.widthProperty().divide(maxCardsPerRow * spacingCompensation)
            );


            String imageName = card.getImageName();
            Image image = new Image(getClass().getResourceAsStream("/com/uno/images/cards/" + imageName));
            cardView.setImage(image);

            boolean playable = TurnManager.isPlayable(card, topCard, currentColor);

            if (playable) {
                cardView.setOnMouseClicked(e -> playCard(card));
            } else {
                cardView.setOpacity(0.4); // visivamente disattivata
                Tooltip.install(cardView, new Tooltip("Non puoi giocare questa carta"));
            }

            playerHandBox.getChildren().add(cardView);
        }
    }
}
