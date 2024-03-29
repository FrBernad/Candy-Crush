package game.backend.move;

import game.backend.Grid;
import game.backend.element.Element;

public abstract class Move {

    private Grid grid;
    protected int i1, j1, i2, j2;//POSICIONES DE LOS DOS CANDIES

    public Move(Grid grid) {
        this.grid = grid;
    }//RECIBE EL JUEGO ACTUAL

    public void setCoords(int i1, int j1, int i2, int j2) {
        this.i1 = i1;
        this.j1 = j1;
        this.i2 = i2;
        this.j2 = j2;
    }

    public boolean isValid() {//PREGUNTA SI EL MOVIMIENTO ES VALIDO
        if ((i1 == i2 && Math.abs(j1 - j2) == 1) || (j1 == j2 && Math.abs(i1 - i2) == 1)) {
            //SI ESTAN EN LA MISMA LINEA Y A DISTANCIA DE UNO EN COLUMNA
            //O MISMA COLUMNA Y DISTANCIA DE UNO EN FILA
            return internalValidation();
        }
        return false;
    }

    protected boolean internalValidation() {
        return true;
    }

    protected Element get(int i, int j) {
        return grid.get(i, j);
    }

    protected void clearContent(int i, int j) {
        grid.clearContent(i, j);
    }

    protected void setContent(int i, int j, Element e) {
        grid.setContent(i, j, e);
    }

    protected void wasUpdated() {
        grid.wasUpdated();
    }

    public abstract void removeElements();

}
