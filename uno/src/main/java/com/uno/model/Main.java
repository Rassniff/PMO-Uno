package com.uno.model;

import java.util.List;
//creazione dei player
public class Main {
    public static void main(String[] args) {
        Player p1 = new HumanPlayer("Tu");
        Player p2 = new BotPlayer("Diego");
        Player p3 = new BotPlayer("Andri");
        Player p4 = new BotPlayer("Edo");

        //creazione game
        Game game = new Game(List.of(p1, p2, p3, p4));
        game.playGame();
    }
}
