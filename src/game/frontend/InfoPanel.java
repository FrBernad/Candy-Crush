package game.frontend;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.Label;

public class InfoPanel extends BorderPane {

	private Label label;

	public InfoPanel() {
		setStyle("-fx-background-color: #5490ff");
		label = new Label("");
		label.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-size: 24");
		setCenter(label);
	}

	public void updateMessage(String text) {
		label.setText(text);
	}

}