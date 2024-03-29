package game.backend.cell;

import game.backend.Grid;
import game.backend.element.Candy;
import game.backend.element.CandyColor;
import game.backend.element.Element;
import game.backend.element.TimeCandy;
import game.backend.level.Level2;

public class TimedCandyGeneratorCell extends CandyGeneratorCell {

    private final double THRESHOLD = 0.1;
    private final int MAX_CANDIES = 8;
    private final int MAX_RANGE = 10;
    private final int MIN_RANGE = 5;
    private int candiesToThrow = MAX_CANDIES;


    public TimedCandyGeneratorCell(Grid grid) {
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
                int extraTime = (int) (Math.random() * MAX_RANGE) + MIN_RANGE;
                Candy aux = new TimeCandy(CandyColor.values()[colour], extraTime);
                ((Level2) grid).add(aux);
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
