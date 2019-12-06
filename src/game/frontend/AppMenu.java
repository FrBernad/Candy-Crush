package game.frontend;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.Optional;
import javafx.stage.Stage;

public class AppMenu extends MenuBar {

    public AppMenu(Stage primaryStage) {
    	
        // ARCHIVO ////////////////////////////////////////////////////////////////
     
    	Menu file = new Menu("File");
   
    	// CHANGE LEVEL
        MenuItem changeLevel = new MenuItem("Change level");
        changeLevel.setGraphic(new ImageView("images/changeLevelIcon.png"));
        changeLevel.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Change level");
            alert.setHeaderText("Change level");
            alert.setContentText("Are you sure you want to change the level?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Stage stage = new Stage();
                    primaryStage.close();
                    Platform.runLater(() -> new GameApp().start(stage));
                }
            }
        });

        // EXIT
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setGraphic(new ImageView("images/exitIcon.png"));
        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Exit application");
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                }
            }
        });
   
        file.getItems().addAll(changeLevel, exitMenuItem);

        // AYUDA //////////////////////////////////////////////////////////////////
    
        Menu help = new Menu("Help");
        
        // Game guide
        MenuItem gameGuide = new MenuItem("Game guide");
        gameGuide.setGraphic(new ImageView("images/helpIcon.png"));
        gameGuide.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game guide");
            alert.setHeaderText("Candy Crush 2.0: TPE Guide");
            alert.setContentText("Level 1: Basic Mode\nThe very popular version of Candy Crush we all know and love. Combine sweets until you get the final score!\n\nLevel 2: Time's Up\nTime is running! Get the maximum score before it's too late. Combine the special candies to gain more time!\n\nLevel 3: Time Bombs\nThink wisely before you play, your movements are numbered! Try to get the final score and crush the bomb candies for more opportunities.");
            alert.showAndWait();
        });

        // About
        MenuItem aboutMenuItem = new MenuItem("About CandyCrush 2.0");
        aboutMenuItem.setGraphic(new ImageView("images/aboutIcon.png"));
        aboutMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Candy Crush 2.0: TPE Credits");
            alert.setContentText("Cátedra POO 2019.\n" +
                    "Implementación Original: Laura Zabaleta (POO 2013).\n" + "Actualización: Francisco Bernad, Camila Ponce y Luciana Diaz K.");
            ImageView graphic=new ImageView("images/graphic.png");
            alert.setGraphic(graphic);
            alert.showAndWait();
        });

        help.getItems().addAll(gameGuide,aboutMenuItem);

        // GENERAL ////////////////////////////////////////////////////////////////
        getMenus().addAll(file, help);
    }

}
