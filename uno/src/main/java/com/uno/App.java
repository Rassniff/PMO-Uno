package com.uno;

import java.util.List;

import com.uno.controller.GameController;
import com.uno.model.BotPlayer;
import com.uno.model.HumanPlayer;
import com.uno.model.Player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uno/view/game_view.fxml"));
        Parent root = loader.load();

        // Ottengo il controller
        GameController controller = loader.getController();

        Player human = new HumanPlayer("Tu");
        Player bot1 = new BotPlayer("Bot1");
        
        controller.initializeGame(List.of(human, bot1));

        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
