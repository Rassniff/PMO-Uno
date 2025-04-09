package com.uno.model;

import java.util.List;

public class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean isClockwise = true;

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void reverseDirection() {
        isClockwise = !isClockwise;
    }

    public void skipNextPlayer() {
        advance(); // salta uno
    }

    public void advance() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    public Player peekNextPlayer() {
        int nextIndex = isClockwise
                ? (currentPlayerIndex + 1) % players.size()
                : (currentPlayerIndex - 1 + players.size()) % players.size();
        return players.get(nextIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }
    
}
