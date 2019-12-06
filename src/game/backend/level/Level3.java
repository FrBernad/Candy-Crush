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

    private final String CONDITION_TAG = "Movements Left: ";
    private static int REQUIRED_SCORE = 5000;
    private static int MIN_MOVEMENTS = 15;
    private int minMovementsLeft = MIN_MOVEMENTS;
    private String movementsMessage;
    private boolean exploded = false;

    
    // El constructor guarda el score necesario para ganar en la clase ancestro "Level"
    // para el posterior acceso del front a dicha información, así como la condición
    // que estipula el comportamiento del nivel (en este caso, límite de movimientos).
    public Level3() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        maxScore=REQUIRED_SCORE;
        setGenerator(TimedBombCandyGeneratorCell.class);
        condition = CONDITION_TAG;
    }

    // La clase padre "BonusLevel" tiene el método specialCandies(...) que guarda
    // en una lista dichos caramelos encontrados.
    public void add(Candy candy) {
        specialCandies.add((TimedBombCandy) candy);
    }

    // Gracias a este método puede llenarse la grilla, corroborando que no inicie sin
    // caramelos especiales. Además, setea los mensajes que corresponden a dicho estado.
    @Override
    public void update() {
        checkCandies();
        if (firstPass) {
            firstPass = false;
            ((Level3State) state()).updateState();
            if (specialCandies.isEmpty())
                ((TimedBombCandyGeneratorCell)generator).reset();
        }
        setMinMovementsLeft();
        if (specialCandies.isEmpty()) {
            movementsMessage = "";
            condition = "No more time bombs in sight";
        } else {
            condition = CONDITION_TAG;
        }
    }
    
    // Itera sobre la lista de TimedBombCandies para verificar si se rompieron
    // o si el tiempo de los mismos llego a cero (es decir, explotaron).
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

    // Actualiza el mínimo de movimientos que le quedan al jugador antes de que explote
    // el TimedBombCandy con menor "tiempo" restante.
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

    // Actualiza el estado del nivel actual mientras se ejecuta el juego.
    private class Level3State extends GameState {

        private long requiredScore;
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
            minMovementsLeft = minMovementsLeft();
        }

        // Lo llama el reloj del frontend para actualizar los paneles de información.  
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
