package com.uno.controller;

import com.uno.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class StartController {
    @FXML private TextField nameField;         // Campo di testo per il nome del giocatore
    @FXML private Spinner<Integer> botSpinner; // Spinner per il numero di bot
    @FXML private RadioButton normalModeRadio; // RadioButton per la modalità normale
    @FXML private RadioButton pointsModeRadio; // RadioButton per la modalità a punti
    @FXML private ToggleGroup modeToggleGroup; // Gruppo di toggle per i RadioButton
    
    // Inizializza il controller, associa i RadioButton al ToggleGroup
    @FXML
    public void initialize() {
        modeToggleGroup = new ToggleGroup();
        normalModeRadio.setToggleGroup(modeToggleGroup);
        pointsModeRadio.setToggleGroup(modeToggleGroup);
        normalModeRadio.setSelected(true); // Imposta la modalità normale come predefinita
    }
    
    // Metodo che permette di avviare il gioco quando l'utente clicca sul pulsante "Inizia Partita"
    @FXML
    private void onStartClicked() {
        
        boolean partitaAPunti = pointsModeRadio.isSelected();
        String nome = nameField.getText().trim();
        if (nome.isEmpty()) nome = "Te";
        int numBot = botSpinner.getValue();

        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(nome));
        String[] botNames = {"Diego", "Andri", "Edo"};
        for (int i = 0; i < numBot; i++) {
            players.add(new BotPlayer(botNames[i]));
        }

        try {
            // Carica il file FXML per l'interfaccia di gioco 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uno/view/game_view.fxml"));
            Parent root = loader.load();

            // Inizializza il controller del gioco con i giocatori e la modalità di gioco
            GameController gameController = loader.getController();
            gameController.initializeGame(players, partitaAPunti);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}