package game.backend.level;

import game.backend.GameState;
import game.backend.cell.TimedCandyGeneratorCell;
import game.backend.element.*;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level2 extends BonusLevel {

    private static long TIME_LEFT = 50;
    private int REQUIRED_SCORE = 5000;

    public Level2() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super();
        setGenerator(TimedCandyGeneratorCell.class);
        condition = "Time Left: ";
    }

    public void add(Candy candy) {
        specialCandies.add((TimeCandy) candy);
    }


    @Override
    public void update() { //cada vez q clickeo
        System.out.println("VECTOR DE CANDIES BEFORE" + specialCandies);
        checkCandies();
        if (firstPass) {
            firstPass = false;
            ((Level2State) state()).updateState();
            if (specialCandies.isEmpty())
                ((TimedCandyGeneratorCell) generator).reset();
        }
        System.out.println("VECTOR DE CANDIES AFTER" + specialCandies);
    }

    private void checkCandies() {
        Iterator<NumberedCandy> it = specialCandies.iterator();
        while (it.hasNext()) {
            NumberedCandy candy = it.next();
            if (!candy.stillUp()) {
                if (started) {
                    ((Level2State) state()).addTime(((TimeCandy) candy).getExtraTime());
                }
                it.remove();
            }
        }
    }

    @Override
    protected GameState newState() {
        return new Level2State(TIME_LEFT, REQUIRED_SCORE);
    }


    private class Level2State extends GameState {

        private long timeLeft;
        private int requiredScore;
        private final int MINUS_SECS = 1;
        private Timer timer = new Timer();

        public void updateState() {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new TimerTask() {
                        @Override
                        public void run() {
                            updateTime();
                            gameOver();
                        }
                    });
                }
            }, 0, 1000);
        }

        public Level2State(long timeLeft, int requiredScore) {
            this.timeLeft = timeLeft;
            this.requiredScore = requiredScore;
        }

        public boolean gameOver() {
            return playerWon() || timeIsUp();
        }

        public boolean playerWon() {
            return getScore() > requiredScore;
        }

        public boolean timeIsUp() {
            if (timeLeft > 0)
                return false;
            timer.cancel();
            return true;
        }

        public void updateTime() {
            timeLeft -= MINUS_SECS;
        }

        @Override
        public Map<String, String> getInfo() {
            Map<String, String> generalInfo = new HashMap<>();
            generalInfo.put("score", String.valueOf(getScore()));
            generalInfo.put("condition", String.valueOf(timeLeft));
            return generalInfo;
        }

        public void addTime(long extraTime) {
            timeLeft += extraTime;
        }

    }

}

