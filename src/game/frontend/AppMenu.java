package game.frontend;

import javafx.application.Platform;
import javafx.scene.control.*;
import java.util.Optional;
import javafx.stage.Stage;

public class AppMenu extends MenuBar {

    private Stage primaryStage;

    public AppMenu(Stage primaryStage) {
        // ARCHIVO ////////////////////////////////////////////////////////////////
        Menu file = new Menu("Archivo");
        // CHANGE LEVEL
        MenuItem changeLevel = new MenuItem("Cambiar nivel");
        changeLevel.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cambiar de nivel");
            alert.setHeaderText("Cambiar de nivel");
            alert.setContentText("¿Está seguro que desea salir del nivel?");
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
        MenuItem exitMenuItem = new MenuItem("Salir");
        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salir");
            alert.setHeaderText("Salir de la aplicación");
            alert.setContentText("¿Está seguro que desea salir de la aplicación?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                }
            }
        });
        file.getItems().addAll(changeLevel, exitMenuItem);

        // AYUDA //////////////////////////////////////////////////////////////////
        Menu help = new Menu("Ayuda");

        MenuItem aboutMenuItem = new MenuItem("Acerca De");
        aboutMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Acerca De");
            alert.setHeaderText("Candy TPE");
            alert.setContentText("Cátedra POO 2019.\n" +
                    "Implementación Original: Laura Zabaleta (POO 2013).\n" + "Actualización: Francisco Bernad, Camila Ponce y Luciana Diaz.");
            alert.showAndWait();
        });

        help.getItems().add(aboutMenuItem);

        // GENERAL ////////////////////////////////////////////////////////////////
        getMenus().addAll(file, help);
    }

}
