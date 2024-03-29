package game.backend.level;

import game.backend.GameState;
import game.backend.cell.CandyGeneratorCell;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Level1 extends Level {

    private static int REQUIRED_SCORE = 5000;
    private static int MAX_MOVES = 20;

    public Level1() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        maxScore = REQUIRED_SCORE;
        setGenerator(CandyGeneratorCell.class);
        condition = "Moves Left: ";
    }

    @Override
    public void update() {
    }


    @Override
    protected GameState newState() {
        return new Level1State(REQUIRED_SCORE, MAX_MOVES);
    }

        private class Level1State extends GameState {
            private long requiredScore;
            private long maxMoves;

            public Level1State(long requiredScore, int maxMoves) {
                this.requiredScore = requiredScore;
                this.maxMoves = maxMoves;
            }

            public boolean gameOver() {
                return playerWon() || getMoves() >= maxMoves;
            }

            public boolean playerWon() {
                return getScore() > requiredScore;
            }

            @Override
            public Map<String, String> getInfo() {
                Map<String, String> generalInfo = new HashMap<>();
                generalInfo.put("score", String.valueOf(getScore()));
                generalInfo.put("condition", String.valueOf(MAX_MOVES-getMoves()));
                return generalInfo;
            }
        }

    @Override
    public boolean tryMove(int i1, int j1, int i2, int j2) {
        boolean ret;
        if (ret = super.tryMove(i1, j1, i2, j2)) {
            state().addMove();
        }
        return ret;
    }

}
