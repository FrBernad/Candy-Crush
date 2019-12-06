package game.frontend;

import game.backend.element.NumberedCandy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BoardPanel extends TilePane {

    private StackPane[][] cells;

    public BoardPanel(final int rows, final int columns, final int cellSize) {
        setPrefRows(rows);
        setPrefColumns(columns);
        setPrefTileHeight(cellSize);
        setPrefTileWidth(cellSize);
        this.cells = new StackPane[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new StackPane();
                getChildren().add(cells[i][j]);
            }
        }
    }

    public void setImage(int row, int column, Image image) {
        cells[row][column].getChildren().add(new ImageView(image));
    }

    public void setNumber(int row, int column, NumberedCandy candy) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(3.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.ORANGERED);
        Text text = new Text(candy.getNumber());
        text.setFont(Font.font("Impact", FontWeight.BOLD, 28));
        text.setFill(Color.BLACK);
        text.setEffect(dropShadow);
        cells[row][column].getChildren().add(text);
    }
}
