package com.uno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uno/view/game_view.fxml"));
        //Parent root = loader.load();
        Parent root = FXMLLoader.load(getClass().getResource("/com/uno/view/start.fxml"));
        // Ottengo il controller
        //GameController controller = loader.getController();

        //Player human = new HumanPlayer("Tu");
        //Player bot1 = new BotPlayer("Bot1");
        //Player bot2 = new BotPlayer("Bot2");
        //Player bot3 = new BotPlayer("Bot3");
        
        //controller.initializeGame(List.of(human, bot1));

        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
