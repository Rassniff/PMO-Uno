package com.uno.controller;

import com.uno.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class GameController {

    //@FXML private HBox playerHandBox;
    @FXML private FlowPane playerHandBox;
    @FXML private ImageView topCardImage;
    @FXML private Button drawButton;
    @FXML private Label statusText;
    @FXML private Label colorLabel;

    private Game game;
    private Player humanPlayer;
    private boolean gameEnded = false;
    private boolean inputLocked = false; // Per evitare input multipli


    public void initializeGame(List<Player> players) {
        game = new Game(players);
        colorLabel.setText("Colore attuale: " + game.getCurrentColor().name());
        humanPlayer = players.get(0); // supponiamo che il primo sia il giocatore umano        
        
        game.addListener(new GameListener() {
            @Override
            public void onGameOver(Player winner) {
                Platform.runLater(() -> {
                    if(winner.equals(humanPlayer)){
                        statusText.setText("Hai vinto!");
                    } else {
                        statusText.setText(winner.getName() + " ha vinto!");
                    }
                    drawButton.setDisable(true);
                    gameEnded = true;
                });
            }
            @Override
            public void onTurnChanged(Player currentPlayer) {
                Platform.runLater(() -> {
                    if (currentPlayer.isBot()) {
                        statusText.setText("Turno di " + currentPlayer.getName() + "...");
                    } else {
                        statusText.setText("Tocca a te...");
                    }
                    updateUI();
                });
            }
            @Override
            public void onColorChanged(Color newColor) {
                Platform.runLater(() -> { 
                    colorLabel.setText("Colore attuale: " + newColor.name());
                    updateUI();
                });
            }
            @Override
            public void onDrawPatta() {
                Platform.runLater(() -> {
                    statusText.setText("Non ci sono più carte da pescare! La partita è patta.");
                    drawButton.setDisable(true);
                    gameEnded = true;
                });
            }
        });
        
        updateUI();
        startTurnLoop();
    }

    private void startTurnLoop() {
        if (gameEnded) return;
        Player currentPlayer = game.getCurrentPlayer();
        
        if (currentPlayer.isBot()) {
            handleBotTurn((BotPlayer) currentPlayer);
        } else {
        // Aggiungi un piccolo delay per dare feedback visivo
        drawButton.setDisable(true);
        //statusText.setText("Tocca a te...");
        new Thread(() -> {
            try {
                Thread.sleep(800); // 0.8 secondi di pausa
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Platform.runLater(() -> {
                //drawButton.setDisable(false);
                // Abilita solo se è ancora il turno dell'umano e la partita non è finita
                if (game.getCurrentPlayer().equals(humanPlayer) && !gameEnded) {
                    inputLocked = false; // Sblocca input
                    drawButton.setDisable(false);
                    updateUI();
                }
            });
        }).start();
    }
    }

    private void handleBotTurn(BotPlayer bot) {
        drawButton.setDisable(true);
        //statusText.setText("Turno di " + bot.getName() + "...");

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

                /*if (playedCard != null) {
                    game.playCard(playedCard);
                    if (playedCard instanceof SpecialCard specialCard) {
                        game.handleSpecialCardExternally(specialCard, bot);

                        // Mostra il colore scelto se è una WILD o DRAW_4
                        if (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR) {
                            Color chosenColor = game.getCurrentColor();
                            statusText.setText(bot.getName() + " ha scelto il colore " + chosenColor.name());
                        }

                    }
                }*/ 
                if (playedCard != null) {
                    Color chosenColor = null;
                    if (playedCard instanceof SpecialCard specialCard &&
                        (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
                        chosenColor = bot.chooseColor(); // O un metodo simile per il bot
                    }
                    game.playTurn(bot, playedCard, chosenColor);
                    if (game.isGameOver()) {
                        updateUI();
                        return;
                    }
                    /* 
                    if (playedCard instanceof SpecialCard specialCard &&
                        (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR)) {
                        Color color = game.getCurrentColor();
                        colorLabel.setText(bot.getName() + " ha scelto il colore " + color.name());
                    }*/
                } /*else {
                    Card drawn = game.drawCardFor(bot);
                    if (TurnManager.isPlayable(drawn, topCard, game.getCurrentColor())) {
                        game.playCard(drawn);
                        if (drawn instanceof SpecialCard specialCard) {
                            game.handleSpecialCardExternally(specialCard, bot);
                        }
                    }
                }*/
                // ...existing code...
                else {
                    Card drawn = game.drawCardFor(bot);
                    Card topCardAfterDraw = game.getTopCard();

                    if (TurnManager.isPlayable(drawn, topCardAfterDraw, game.getCurrentColor())) {
                        Color chosenColor = null;
                        if (drawn instanceof SpecialCard specialCard &&
                            (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
                            chosenColor = bot.chooseColor();
                        }
                        game.playTurn(bot, drawn, chosenColor);
                        if (game.isGameOver()) {
                            updateUI();
                            return;
                        }
                    }
                }
                // ...existing code...

                /*if (game.isGameOver()) {
                    Player winner = game.getWinner();
                    statusText.setText(winner.getName() + " ha vinto!");
                    drawButton.setDisable(true);
                    return;
                }*/

                //game.getTurnManager().advance();
                game.advanceTurn();
                updateUI();
                startTurnLoop(); // Prossimo turno
            });
        }).start();
    }

    @FXML
    private void onDrawCardClicked() {
        if(gameEnded) return;
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(humanPlayer)) return;

         //Blocco di sicurezza aggiuntivo
        if (game.canCurrentPlayerPlay()) {
            // Opzionale: mostra messaggio che non può pescare se ha carte valide
            statusText.setText("Hai carte giocabili! Non puoi pescare.");
            return;
        }

        Card drawn = game.drawCardFor(humanPlayer); 
        Card topCard = game.getTopCard();

        if (TurnManager.isPlayable(drawn, topCard, game.getCurrentColor())) {
            //playCard(drawn);
            statusText.setText("Hai pescato una carta giocabile");
            updateUI();
            
        } else {
            updateUI();
            //game.getTurnManager().advance();
            game.advanceTurn();
            if(game.isGameOver()){
                return;
            }
            startTurnLoop();
        }
    }

    /*private void playCard(Card card) {

        humanPlayer.removeCard(card);
        game.playCard(card);
        statusText.setText("Hai giocato: " + card.toString());


        if (card instanceof SpecialCard specialCard) {
            if(specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR){
                promptColorSelection();
            }
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
    */

    private void playCard(Card card) {
        if (gameEnded || inputLocked) return;
        inputLocked = true; // Blocca ulteriori input fino a che non finisce il turno

        //boolean hasWon;
        if (card instanceof SpecialCard specialCard &&
            (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
            Color chosenColor = promptColorSelectionAndWait();
            game.playTurn(humanPlayer, card, chosenColor);
        } else {
            game.playTurn(humanPlayer, card, null);
        }
        statusText.setText("Hai giocato: " + card.toString());
        updateUI();
        /*if (game.isGameOver()) {
            Player winner = game.getWinner();
            statusText.setText(winner.getName() + " ha vinto!");
            drawButton.setDisable(true);
            gameEnded = true;
            return;
        }*/
        //game.getTurnManager().advance();
        if(game.isGameOver()){
            //inputLocked = false; // Sblocca input
            return; // Il gioco è finito, non avanzare il turno
        }
        game.advanceTurn();
        startTurnLoop();
        //inputLocked = false; // Sblocca input dopo che il turno è finito
    }
   
    private void updateUI() {
        updateTopCardImage();
        updateHand();

        // Disabilita il bottone se il giocatore ha carte giocabili
        Player currentPlayer = game.getCurrentPlayer();
        boolean canPlay = game.canCurrentPlayerPlay();
        
        colorLabel.setText("Colore attuale: " + game.getCurrentColor().name());
        
        //drawButton.setDisable(canPlay || gameEnded);
        
        // Disabilita il bottone se:
        // - non è il turno dell'umano
        // - il giocatore ha carte giocabili
        // - la partita è finita
        drawButton.setDisable(
            !currentPlayer.equals(humanPlayer) ||
            canPlay ||
            gameEnded
        );
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

            if (playable && !gameEnded && !inputLocked) {
                cardView.setOnMouseClicked(e -> playCard(card));
            } else {
                cardView.setOpacity(0.4); // visivamente disattivata
                Tooltip.install(cardView, new Tooltip("Non puoi giocare questa carta"));
            }

            playerHandBox.getChildren().add(cardView);
        }
    }

   /* private void promptColorSelection() {
    Platform.runLater(() -> {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Scegli un colore");

        Button redButton = new Button("Rosso");
        Button yellowButton = new Button("Giallo");
        Button greenButton = new Button("Verde");
        Button blueButton = new Button("Blu");

        redButton.setOnAction(e -> {
            game.setCurrentColor(Color.RED);
            dialog.close();
            continueGame();
        });

        yellowButton.setOnAction(e -> {
            game.setCurrentColor(Color.YELLOW);
            dialog.close();
            continueGame();
        });

        greenButton.setOnAction(e -> {
            game.setCurrentColor(Color.GREEN);
            dialog.close();
            continueGame();
        });

        blueButton.setOnAction(e -> {
            game.setCurrentColor(Color.BLUE);
            dialog.close();
            continueGame();
        });

        HBox hbox = new HBox(10, redButton, yellowButton, greenButton, blueButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));

        Scene scene = new Scene(hbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    });
}
    */
    
    private Color promptColorSelectionAndWait() {
    final Color[] selectedColor = new Color[1];

    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Scegli un colore");

    // Disabilita la chiusura tramite la X
    dialog.setOnCloseRequest(event -> {
        event.consume(); // Previene la chiusura del dialogo
    });

    // Disabilita la chiusura tramite ESC
    dialog.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
        if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
            event.consume();
        }
    });

    Button redButton = new Button("Rosso");
    Button yellowButton = new Button("Giallo");
    Button greenButton = new Button("Verde");
    Button blueButton = new Button("Blu");

    redButton.setOnAction(e -> { selectedColor[0] = Color.RED; dialog.close(); });
    yellowButton.setOnAction(e -> { selectedColor[0] = Color.YELLOW; dialog.close(); });
    greenButton.setOnAction(e -> { selectedColor[0] = Color.GREEN; dialog.close(); });
    blueButton.setOnAction(e -> { selectedColor[0] = Color.BLUE; dialog.close(); });

    HBox hbox = new HBox(10, redButton, yellowButton, greenButton, blueButton);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(20));

    Scene scene = new Scene(hbox);
    dialog.setScene(scene);
    dialog.showAndWait();

    return selectedColor[0];
}
    
    /*private void continueGame() {
    updateUI();
    if (humanPlayer.isHandEmpty()) {
        statusText.setText("Hai vinto!");
        drawButton.setDisable(true);
        return;
    }

    game.getTurnManager().advance();
    startTurnLoop();
}
    */
}


