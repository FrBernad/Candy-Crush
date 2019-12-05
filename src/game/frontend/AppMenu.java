package game.frontend;

import game.backend.level.Level;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.Optional;
import game.backend.CandyGame;
import game.backend.level.Level1;
import game.backend.level.Level2;
import game.backend.level.Level3;
import javafx.stage.Stage;

public class AppMenu extends MenuBar {

    protected Stage level;

    private EventHandler<ActionEvent> provider(Class<? extends Level> levelClass)
    {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CandyGame game = new CandyGame(levelClass);
                CandyFrame frame = new CandyFrame(game);
                Scene scene = new Scene(frame);
                level.setResizable(false);
                level.setScene(scene);
                level.show();
            }
        };
    }

    public AppMenu() {

        // ARCHIVO ////////////////////////////////////////////////////////////////
        Menu file = new Menu("Archivo");
        // CHANGE LEVEL
        Menu changeLevel = new Menu("Change level");
        MenuItem lvl1 = new MenuItem("New level 1");
        MenuItem lvl2 = new MenuItem("New level 2");
        MenuItem lvl3 = new MenuItem("New level 3");

        lvl1.setOnAction(provider(Level1.class));
        lvl2.setOnAction(provider(Level2.class));
        lvl3.setOnAction(provider(Level3.class));

        changeLevel.getItems().addAll(lvl1, lvl2, lvl3);

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
