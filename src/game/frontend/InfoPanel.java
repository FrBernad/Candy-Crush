package game.frontend;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.Label;


public class InfoPanel extends BorderPane {

    private Label label;

    public InfoPanel(String path) {
        setStyle("-fx-background-image:url("+path+")");
        label = new Label("");
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 24");
        setCenter(label);
    }

    public void updateMessage(String text) {
        label.setText(text);
    }

}