package game.backend;

import game.backend.element.Candy;
import game.backend.element.CandyColor;
import game.backend.element.Element;

import java.awt.Point;

public class FigureDetector {
	
	private Grid grid;
	
	public FigureDetector(Grid grid) {
		this.grid = grid;
	}
	
	public Figure checkFigure(int i, int j) {
		int acum = readCheckpoints(i, j);
		if (acum > 0) {
			for(Figure f: Figure.values()) {
				if (f.matches(acum)) {//CHECKEA Q SE FORME ALGUN COMBO
					return f;
				}
			}
		}
		return null;
	}
	
	private int readCheckpoints(int i, int j) {
		Element curr = grid.get(i,j);
		int acum = 0;
		for (Checkpoint cp: Checkpoint.values()) { //POSICIONES POSIBLES
			int newI = i + cp.getI();//GUARDA EL VALOR DE EL NUEVO I
			int newJ = j + cp.getJ();//GUARDA EL VALOR DE EL NUEVO J
			if (newI >= 0 && newI < Grid.SIZE && newJ >= 0 && newJ < Grid.SIZE) {//CHECKEA QUE NO SE PASE DE LOS MARGENES
				if (curr.equals(grid.get(newI, newJ))) {//CHECKEA Q TENGA EL MISMO CARAMELO ALREDEDOR
					acum += cp.getValue();//SUMA PARA SABER LUEGO EN EL TOTAL DE ACUM Q COMBO SE FORMA
				}
			}
		}
		return acum;
	}
	
	public void removeFigure(int i, int j, Figure f) {
		CandyColor color = ((Candy)grid.get(i, j)).getColor();
		grid.clearContent(i, j);
		if (f.hasReplacement()) {
			grid.setContent(i, j, f.generateReplacement(color));//EN CASO DE TENER REEMPLAZO LO PONE
		}
		for (Point p: f.getPoints()) {
			grid.clearContent(i + p.x, j + p.y);//ELIMINA LOS QUE ESTAN EN CONTACTO CON EL CARAMELO QUE REACCIONA
		}
	}
	
}
