package com.uno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    // Punto di ingresso dell'applicazione JavaFX
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML per l'interfaccia di avvio del gioco
        Parent root = FXMLLoader.load(getClass().getResource("/com/uno/view/start.fxml"));
       
        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // Metodo main per avviare l'applicazione JavaFX
    public static void main(String[] args) {
        launch(args);
    }
    
}
