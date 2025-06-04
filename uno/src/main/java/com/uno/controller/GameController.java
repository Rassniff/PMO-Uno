package com.uno.controller;

import com.uno.model.*;
import com.uno.model.interfaces.IGameListener;
import com.uno.controller.interfaces.IGameController;

import javafx.animation.PauseTransition;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.List;

public class GameController implements IGameController {

    @FXML private FlowPane playerHandBox; // FlowPane per le carte del giocatore
    @FXML private HBox opponentsBox;      // HBox per le carte degli avversari
    @FXML private ImageView topCardImage; // ImageView per la carta in cima al mazzo
    @FXML private Button drawButton;      // Bottone per pescare una carta
    @FXML private Label statusText;       // Label per lo stato del gioco
    @FXML private Label colorLabel;       // Label per il colore attuale del mazzo
    @FXML private Label unoLabel;         // Label per notificare la chiamata di UNO
    @FXML private Button unoButton;       // Bottone per chiamare UNO

    private Game game;                     // La partita corrente
    private Player humanPlayer;            // Il giocatore umano
    private boolean gameEnded = false;     // Flag per indicare se la partita è finita
    private boolean inputLocked = false;   // Flag per evitare input multipli
    private boolean partitaAPunti = false; // Flag per modalità partita a punti


    // Metodo per inizializzare il controller e la partita
    public void initializeGame(List<Player> players, boolean partitaAPunti) {
        this.partitaAPunti = partitaAPunti;
        statusText.setText("");
        game = new Game(players);
        colorLabel.setText("Colore attuale: " + game.getCurrentColor().name());
        humanPlayer = players.get(0); // Assumiamo che il primo giocatore sia l'umano       
        
        // Aggiungi i listener per gli eventi del gioco
        game.addListener(new IGameListener() {
            // Metodo chiamato quando il gioco è finito
            @Override
            public void onGameOver(Player winner) {
                Platform.runLater(() -> {
                    if(partitaAPunti){
                        showScorePopup();
                    } else {
                        if(winner.equals(humanPlayer)){
                            statusText.setText("Hai vinto!");
                        } else {
                            statusText.setText(winner.getName() + " ha vinto!");
                        }
                    drawButton.setDisable(true);
                    gameEnded = true;
                    }
                });
            }
            // Metodo chiamato quando il turno cambia
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
            // Metodo chiamato quando una carta viene giocata
            @Override
            public void onColorChanged(Color newColor) {
                Platform.runLater(() -> { 
                    colorLabel.setText("Colore attuale: " + newColor.name());
                    updateUI();
                });
            }
            // Metodo chiamato quando il gioco è patta
            @Override
            public void onDrawPatta() {
                Platform.runLater(() -> {
                    statusText.setText("Non ci sono più carte da pescare! La partita è patta.");
                    drawButton.setDisable(true);
                    gameEnded = true;
                });
            }
            // Metodo chiamato quando un giocatore chiama UNO
            @Override
            public void onUnoCalled(Player player) {
                Platform.runLater(() -> {
                    unoLabel.setText(player.getName() + " ha chiamato UNO!");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> unoLabel.setText(""));
                    pause.play();
                });
            }
        });
        
        updateUI();
        startTurnLoop();
    }

    // Metodo per iniziare il loop dei turni
    private void startTurnLoop() {
        if (gameEnded) return;
        Player currentPlayer = game.getCurrentPlayer();
        
        if (currentPlayer.isBot()) {
            handleBotTurn((BotPlayer) currentPlayer);
        } else {
        drawButton.setDisable(true);
        new Thread(() -> {
            try {
                Thread.sleep(800); // 0.8 secondi di pausa
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Platform.runLater(() -> {
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

    // Metodo per gestire il turno del bot
    private void handleBotTurn(BotPlayer bot) {
        drawButton.setDisable(true);

        new Thread(() -> {
            try {
                Thread.sleep(1500); // 1.5 secondi di pausa per il bot
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                // 1. All'inizio del turno: il bot può chiamare UNO se ha 1 carta
                if (bot.getHand().size() == 1 && !bot.isUnoCalled()) {
                    bot.setUnoCalled(true);
                    game.notifyUnoCalled(bot);

                    unoLabel.setText(bot.getName() + " ha chiamato UNO!");
                    updateUI();
                    
                }

                // Prova a giocare una carta
                Card topCard = game.getTopCard();
                Card playedCard = bot.playTurn(topCard, game.getCurrentColor());

                if (playedCard != null) {
                    Color chosenColor = null;
                    if (playedCard instanceof SpecialCard specialCard &&
                        (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
                        chosenColor = bot.chooseColor();
                    }
                    game.playTurn(bot, playedCard, chosenColor);
                    updateUI();
                    if (game.isGameOver()) {
                        updateUI();
                        return;
                    }
        
                } else {
                    // Se non può giocare, pesca una carta
                    Card drawn = game.drawCardFor(bot);
                    updateUI();
                    if (game.isGameOver()) {
                        updateUI();
                        return;
                    }
                    Card topCardAfterDraw = game.getTopCard();

                    if (TurnManager.isPlayable(drawn, topCardAfterDraw, game.getCurrentColor())) {
                        Color chosenColor = null;
                        if (drawn instanceof SpecialCard specialCard &&
                            (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
                            chosenColor = bot.chooseColor();
                        }
                        game.playTurn(bot, drawn, chosenColor);
                        updateUI();
                        if (game.isGameOver()) {
                            updateUI();
                            return;
                        }
                    }
                }

                // Controllo vittoria
                if (game.isGameOver()) {
                    updateUI();
                    return;
                }

                bot.setUnoCalled(false);
                game.advanceTurn();
                updateUI();
                startTurnLoop();
            });
        }).start();
    }

    // Metodo per gestire il click sul bottone "Pesca carta"
    @FXML
    public void onDrawCardClicked() {
        if(gameEnded) return;
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.equals(humanPlayer)) return;

         // Blocco di sicurezza aggiuntivo
        if (game.canCurrentPlayerPlay()) {
            // Opzionale: mostra messaggio che non può pescare se ha carte valide
            statusText.setText("Hai carte giocabili! Non puoi pescare.");
            return;
        }

        Card drawn = game.drawCardFor(humanPlayer); 
        Card topCard = game.getTopCard();

        if (TurnManager.isPlayable(drawn, topCard, game.getCurrentColor())) {
            statusText.setText("Hai pescato una carta giocabile");
            updateUI();
            
        } else {
            updateUI();
            game.advanceTurn();
            if(game.isGameOver()){
                return;
            }
            startTurnLoop();
        }
    }

    // Metodo per giocare una carta
    public void playCard(Card card) {
        if (gameEnded || inputLocked) return;
        inputLocked = true; // Blocca ulteriori input fino a che non finisce il turno
        
        
        if (humanPlayer.getHand().size() == 1 && !humanPlayer.isUnoCalled()) {
            game.drawCardFor(humanPlayer);
            unoLabel.setText("Non hai chiamato UNO! Pesca una carta.");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> unoLabel.setText(""));
            pause.play();
            updateUI();
            inputLocked = false;
            game.advanceTurn();
            startTurnLoop();
            return;
        }
        
        if (card instanceof SpecialCard specialCard &&
            (specialCard.getAction() == Action.WILD || specialCard.getAction() == Action.WILD_DRAW_FOUR || specialCard.getAction() == Action.SHUFFLE)) {
            Color chosenColor = promptColorSelectionAndWait();
            game.playTurn(humanPlayer, card, chosenColor);
        } else {
            game.playTurn(humanPlayer, card, null);
        }
        statusText.setText("Hai giocato: " + card.toString());
        updateUI();
        
        if(game.isGameOver()){
            return; // Il gioco è finito, non avanzare il turno
        }
        game.advanceTurn();
        startTurnLoop();
    }
   
    // Metodo per aggiornare l'interfaccia utente
    private void updateUI() {
        updateTopCardImage();
        updateHand();
        updateOpponents();

        // Disabilita il bottone se il giocatore ha carte giocabili
        Player currentPlayer = game.getCurrentPlayer();
        boolean canPlay = game.canCurrentPlayerPlay();
        
        colorLabel.setText("Colore attuale: " + game.getCurrentColor().name());
         
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

    // Metodo per aggiornare l'immagine della carta in cima al mazzo
    private void updateTopCardImage() {
        Card topCard = game.getTopCard();
        String imageName = topCard.getImageName();
        Image image = new Image(getClass().getResourceAsStream("/com/uno/images/cards/" + imageName));
        topCardImage.setImage(image);
    }

    // Metodo per aggiornare la mano del giocatore
    private void updateHand() {
        playerHandBox.getChildren().clear();
        Card topCard = game.getTopCard();
        Color currentColor = game.getCurrentColor();

        int handSize = humanPlayer.getHand().size();
        int maxCardsPerRow = Math.max(7, handSize);
        double spacingCompensation = 1.1;

        for (Card card : humanPlayer.getHand()) {
            ImageView cardView = new ImageView();
        
            cardView.setPreserveRatio(true);

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
                cardView.setOpacity(0.4); 
                Tooltip.install(cardView, new Tooltip("Non puoi giocare questa carta"));
            }
            
            playerHandBox.getChildren().add(cardView);
        }
        //Bottone UNO sempre visibile, abilitato solo se hai 1 carta
        if (handSize == 1 && !humanPlayer.isUnoCalled()) {
            unoButton.setDisable(false);
        } else {
            unoButton.setDisable(true);
            humanPlayer.setUnoCalled(false);
        }
    }

    // Metodo per aggiornare la visualizzazione degli avversari
    private void updateOpponents() {
        opponentsBox.getChildren().clear();
        List<Player> players = game.getPlayers();

        for (Player p : players) {
            if (p == humanPlayer) continue;
            VBox vbox = new VBox(2);
            vbox.setAlignment(Pos.CENTER);

            // Numero di carte
            Label numLabel = new Label(String.valueOf(p.getHand().size()));
            numLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222; -fx-background-color: rgba(255,255,255,0.7); -fx-padding: 2 8 2 8; -fx-background-radius: 10;");

            // Immagine carta coperta
            ImageView backView = new ImageView(new Image(getClass().getResourceAsStream("/com/uno/images/cards/card_back.png")));
            backView.setFitHeight(70);
            backView.setPreserveRatio(true);

            vbox.getChildren().addAll(numLabel, backView);

            // Nome bot sotto
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
            vbox.getChildren().add(nameLabel);

            opponentsBox.getChildren().add(vbox);
        }
    }
    
    // Metodo per selezionare un colore tramite un dialog
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
    
    // Metodo per uscire dal gioco e tornare alla schermata iniziale
    @FXML
    private void onExitClicked() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/uno/view/start.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) topCardImage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per chiamare UNO
    @FXML
    public void onUnoClicked() {
        humanPlayer.setUnoCalled(true);
        unoButton.setDisable(true);
        game.notifyUnoCalled(humanPlayer);
    }

    // Metodo per mostrare la classifica al termine della partita
    private void showScorePopup() {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Classifica");

    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(20));

    Label title = new Label("Classifica attuale:");
    title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    vbox.getChildren().add(title);

    final boolean[] partitaFinita = {false};
    final Player[] vincitore = {null};

    for (Player p : game.getPlayers()) {
        int score = game.getScoreForPlayer(p);
        Label scoreLabel = new Label(p.getName() + ": " + score + " punti");
        vbox.getChildren().add(scoreLabel);
        if (partitaAPunti && score >= 500) {
            partitaFinita[0] = true;
            vincitore[0] = p;
        }
    }

    Button continueButton = new Button(partitaFinita[0] ? "Fine partita" : "Continua");
    vbox.getChildren().add(continueButton);

    continueButton.setOnAction(e -> {
        dialog.close();
        if (partitaFinita[0]) {
            statusText.setText("Partita finita! " + vincitore[0].getName() + " ha vinto la partita!");
            drawButton.setDisable(true);
            gameEnded = true;
        } else {
            game.resetForNewRound();
            gameEnded = false;
            updateUI();
            startTurnLoop();
        }
    });

    Scene scene = new Scene(vbox);
    dialog.setScene(scene);
    dialog.showAndWait();
    }
}


