package game.backend.level;

import game.backend.GameState;
import game.backend.cell.TimedCandyGeneratorCell;
import game.backend.element.*;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level2 extends BonusLevel {

    private static long TIME_LEFT = 50;
    private static int REQUIRED_SCORE = 5000;

    // El constructor guarda el score necesario para ganar en la clase ancestro "Level"
    // para el posterior acceso del front a dicha información, así como la condición
    // que estipula el comportamiento del nivel (en este caso, límite de tiempo).
    public Level2() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        maxScore=REQUIRED_SCORE;
        setGenerator(TimedCandyGeneratorCell.class);
        condition = "Time Left: ";
    }

    // La clase padre "BonusLevel" tiene el método specialCandies(...) que guarda
    // en una lista dichos caramelos encontrados.
    public void add(Candy candy) {
        specialCandies.add((TimeCandy) candy);
    }

    // Gracias a este método puede llenarse la grilla, corroborando que no inicie sin
    // caramelos especiales. Además, setea los mensajes que corresponden a dicho estado.
    @Override
    public void update() {
        checkCandies();
        if (firstPass) {
            firstPass = false;
            ((Level2State) state()).updateState();
            if (specialCandies.isEmpty())
                ((TimedCandyGeneratorCell) generator).reset();
        }
    }

    // Itera sobre la lista de TimedBombCandies para verificar si se rompieron
    // y si es así, sumar su valor al tiempo restante.
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

    // Actualiza el estado del nivel actual mientras se ejecuta el juego.
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

        // Lo llama el reloj del frontend para actualizar los paneles de información.
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
