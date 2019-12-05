package game.backend.level;

import game.backend.element.NumberedCandy;
import game.backend.move.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class BonusLevel extends Level {

    protected boolean started = false;
    protected boolean firstPass = true;

    protected List<NumberedCandy> specialCandies = new ArrayList<>();

    public abstract void update();

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
    public boolean canUpdate() {
        return true;
    }
}
