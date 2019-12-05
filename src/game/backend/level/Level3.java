package game.backend.level;

import game.backend.GameState;
import game.backend.cell.TimedBombCandyGeneratorCell;
import game.backend.element.Candy;
import game.backend.element.NumberedCandy;
import game.backend.element.TimedBombCandy;
import game.backend.move.Move;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level3 extends Level {

    private static int REQUIRED_SCORE = 5000;
    private static int MIN_MOVEMENTS = 15;
    private boolean exploded = false;
    private boolean started = false;
    private boolean firstPass = true;
    private int minMovementsLeft = MIN_MOVEMENTS;
    protected List<NumberedCandy> timedBombCandies = new ArrayList<>();

    public Level3() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super();
        setGenerator(TimedBombCandyGeneratorCell.class);
    }

    public void add(Candy candy) {
        timedBombCandies.add((NumberedCandy) candy);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    private void checkCandies() {
        Iterator<NumberedCandy> it = timedBombCandies.iterator();

        while (it.hasNext()) {
            NumberedCandy candy = it.next();
            if (!candy.stillUp()) {
                it.remove();
                if (((TimedBombCandy) candy).getMovementsLeft() == minMovementsLeft) {
                    minMovementsLeft = MIN_MOVEMENTS;
                }
            } else {
                if (started) {
                    if (((TimedBombCandy) candy).countDownMovements() == 0)
                        exploded = true;
                }
            }
        }
    }

    private void setMinMovementsLeft() {
        Iterator<NumberedCandy> it = timedBombCandies.iterator();
        int movementsLeft;

        while (it.hasNext()) {
            NumberedCandy candy = it.next();
            movementsLeft = ((TimedBombCandy) candy).getMovementsLeft();
            if (movementsLeft < minMovementsLeft)
                minMovementsLeft = movementsLeft;
        }
    }

    public void update() {
        System.out.println("VECTOR DE CANDIES BEFORE" + timedBombCandies);

        checkCandies();
        if (firstPass) {
            firstPass = false;
            ((Level3State) state()).updateState();
            if (timedBombCandies.isEmpty())
                ((TimedBombCandyGeneratorCell) generator).reset();
        }
        setMinMovementsLeft();

        System.out.println("VECTOR DE CANDIES AFTER" + timedBombCandies);
    }

    @Override
    public boolean tryMove(int i1, int j1, int i2, int j2) {
        Move move = moveMaker.getMove(i1, j1, i2, j2);
        swapContent(i1, j1, i2, j2);
        if (move.isValid()) {
            if (!started) {
                started = true;
            }
            move.removeElements();
            fallElements();
            update();
            return true;
        } else {
            swapContent(i1, j1, i2, j2);
            return false;
        }
    }

    @Override
    protected GameState newState() {
        return new Level3State(REQUIRED_SCORE);
    }

    public int minMovementsLeft() {
        return minMovementsLeft;
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
            generalInfo.put("condition", String.valueOf(minMovementLeft));
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
