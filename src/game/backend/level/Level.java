package game.backend.level;
import game.backend.Grid;
import game.backend.cell.Cell;
import game.backend.element.Wall;
import java.lang.reflect.InvocationTargetException;

// Se movió la mayor cantidad de comportamientos compartidos
// a esta clase abstracta, con el contenido que previamente
// se hallaba en Level1.

public abstract class Level extends Grid {

	protected int maxScore;
	protected String condition;
    protected Cell wallCell;
    protected Cell generator;
    
    public void setGenerator(Class<? extends Cell> generator) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.generator = generator.getConstructor(Grid.class).newInstance(this);
    }

    public boolean canUpdate() {
        return false;
    }

    public abstract void update();

    public String getCondition() {
        return condition;
    }

    public String getMaxScore() {
    	return String.valueOf(maxScore);
    }
    
    @Override
    protected void fillCells() {

        wallCell = new Cell(this);
        wallCell.setContent(new Wall());

        //corners
        g()[0][0].setAround(generator, g()[1][0], wallCell, g()[0][1]); //TOPLEFT
        g()[0][SIZE - 1].setAround(generator, g()[1][SIZE - 1], g()[0][SIZE - 2], wallCell);//TOPRIGHT
        g()[SIZE - 1][0].setAround(g()[SIZE - 2][0], wallCell, wallCell, g()[SIZE - 1][1]);//DOWNLEFT
        g()[SIZE - 1][SIZE - 1].setAround(g()[SIZE - 2][SIZE - 1], wallCell, g()[SIZE - 1][SIZE - 2], wallCell);//DOWN RIGHT

        //upper line cells
        for (int j = 1; j < SIZE - 1; j++) {
            g()[0][j].setAround(generator, g()[1][j], g()[0][j - 1], g()[0][j + 1]);
        }
        //bottom line cells
        for (int j = 1; j < SIZE - 1; j++) {
            g()[SIZE - 1][j].setAround(g()[SIZE - 2][j], wallCell, g()[SIZE - 1][j - 1], g()[SIZE - 1][j + 1]);
        }
        //left line cells
        for (int i = 1; i < SIZE - 1; i++) {
            g()[i][0].setAround(g()[i - 1][0], g()[i + 1][0], wallCell, g()[i][1]);
        }
        //right line cells
        for (int i = 1; i < SIZE - 1; i++) {
            g()[i][SIZE - 1].setAround(g()[i - 1][SIZE - 1], g()[i + 1][SIZE - 1], g()[i][SIZE - 2], wallCell);
        }
        //central cells
        for (int i = 1; i < SIZE - 1; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                g()[i][j].setAround(g()[i - 1][j], g()[i + 1][j], g()[i][j - 1], g()[i][j + 1]);
            }
        }
    }

}
