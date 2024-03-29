package game.backend;

import java.util.*;

import static java.lang.String.valueOf;

public abstract class GameState {

    private long score = 0;
    private int moves = 0;

    public void addScore(long value) {
        this.score = this.score + value;
    }

    public Map<String, String> getInfo() {
        Map<String, String> generalInfo = new HashMap<>();
        generalInfo.put("score", valueOf(getScore()));
        generalInfo.put("condition", "");
        return generalInfo;
    }

    public long getScore() {
        return score;
    }

    public void addMove() {
        moves++;
    }

    public int getMoves() {
        return moves;
    }

    public abstract boolean gameOver();

    public abstract boolean playerWon();

}
