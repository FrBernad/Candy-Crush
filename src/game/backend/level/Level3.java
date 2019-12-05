package game.backend.level;

import game.backend.GameState;
import game.backend.cell.TimedBombCandyGeneratorCell;
import game.backend.element.Candy;
import game.backend.element.NumberedCandy;
import game.backend.element.TimedBombCandy;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level3 extends BonusLevel {

    private static int REQUIRED_SCORE = 5000;
    private static int MIN_MOVEMENTS = 15;
    private boolean exploded = false;
    private int minMovementsLeft = MIN_MOVEMENTS;
    private String movementsMessage;

    public Level3() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super();
        setGenerator(TimedBombCandyGeneratorCell.class);
        condition = "Movements Left: ";
    }

    public void add(Candy candy) {
        specialCandies.add((TimedBombCandy) candy);
    }

    @Override
    public void update() {
        checkCandies();
        if (firstPass) {
            firstPass = false;
            ((Level3State) state()).updateState();
            if (specialCandies.isEmpty())
                ((TimedBombCandyGeneratorCell) generator).reset();
        }
        setMinMovementsLeft();

        if (specialCandies.isEmpty()) {
            movementsMessage = "-";
        }
    }

    private void checkCandies() {
        Iterator<NumberedCandy> it = specialCandies.iterator();
        while (it.hasNext()) {
            NumberedCandy candy = it.next();
            if (!candy.stillUp()) {
                it.remove();
                if (((TimedBombCandy) candy).getMovementsLeft() == minMovementsLeft) {
                    minMovementsLeft = MIN_MOVEMENTS;
                    movementsMessage = String.valueOf(minMovementsLeft);
                }
            } else {
                if (started) {
                    if (((TimedBombCandy) candy).countDownMovements() == 0) {
                        exploded = true;
                    }
                }
            }
        }
    }

    private void setMinMovementsLeft() {
        Iterator<NumberedCandy> it = specialCandies.iterator();
        int movementsLeft;

        while (it.hasNext()) {
            NumberedCandy candy = it.next();
            movementsLeft = ((TimedBombCandy) candy).getMovementsLeft();
            if (movementsLeft < minMovementsLeft) {
                minMovementsLeft = movementsLeft;
                movementsMessage = String.valueOf(minMovementsLeft);
            }
        }
    }

    public int minMovementsLeft() {
        return minMovementsLeft;
    }

    @Override
    protected GameState newState() {
        return new Level3State(REQUIRED_SCORE);
    }

    private class Level3State extends GameState {

        private long requiredScore;
        private int minMovementLeft;
        private Timer timer = new Timer();

        public Level3State(long requiredScore) {
            this.requiredScore = requiredScore;
        }

        public void updateState() {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new TimerTask() {
                        @Override
                        public void run() {
                            updateMovementsLeft();
                            gameOver();
                        }
                    });
                }
            }, 0, 1000);
        }

        public boolean gameOver() {
            return playerWon() || bombExploded();
        }

        public void updateMovementsLeft() {
            minMovementLeft = minMovementsLeft();
        }

        //lo llama el reloj del front
        @Override
        public Map<String, String> getInfo() {
            Map<String, String> generalInfo = new HashMap<>();
            generalInfo.put("score", String.valueOf(getScore()));
            generalInfo.put("condition", movementsMessage);
            return generalInfo;
        }

        public boolean playerWon() {
            return getScore() > requiredScore;
        }

        public boolean bombExploded() {
            return exploded;
        }

    }
}
