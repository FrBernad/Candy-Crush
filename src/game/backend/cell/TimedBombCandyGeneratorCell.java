package game.backend.cell;

import game.backend.Grid;
import game.backend.element.*;
import game.backend.level.Level3;

public class TimedBombCandyGeneratorCell extends CandyGeneratorCell {

    private final double THRESHOLD = 0.06;
    private final int MAX_CANDIES = 10;
    private final int MAX_RANGE = 10;
    private final int MIN_RANGE = 5;
    private int candiesToThrow = MAX_CANDIES;

    public TimedBombCandyGeneratorCell(Grid grid) {
        super(grid);
    }

    private boolean canThrow() {
        return candiesToThrow > 0;
    }

    @Override
    public Element getContent() {
        int colour = (int) (Math.random() * CandyColor.values().length);

        if (canThrow()) {
            double specialCandyChance = Math.random();
            if (specialCandyChance < THRESHOLD) {
                int movements = (int) (Math.random() * MAX_RANGE) + MIN_RANGE;
                Candy aux = new TimedBombCandy(CandyColor.values()[colour], movements);
                ((Level3) grid).add(aux);
                candiesToThrow--;
                return aux;
            }
        }
        return new Candy(CandyColor.values()[colour]);
    }

    public void reset() {
        candiesToThrow = MAX_CANDIES;
    }
}