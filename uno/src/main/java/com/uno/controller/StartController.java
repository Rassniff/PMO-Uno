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

import java.util.ArrayList;
import java.util.List;

public class StartController {
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> botSpinner;
    @FXML private RadioButton normalModeRadio;
    @FXML private RadioButton pointsModeRadio;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uno/view/game_view.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.initializeGame(players, partitaAPunti);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}