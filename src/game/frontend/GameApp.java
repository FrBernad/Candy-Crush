package game.frontend;

import game.backend.CandyGame;
import game.backend.level.Level1;
import game.backend.level.Level2;
import game.backend.level.Level3;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApp extends Application {

    protected Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CandyCrush");

        Button level1 = new Button();
        level1.setText("Level 1");
        level1.setMinSize(70, 30);
        level1.setOnAction(provider(Level1.class));

        Button level2 = new Button();
        level2.setText("Level 2");
        level2.setMinSize(70, 30);
        level2.setOnAction(provider(Level2.class));

        Button level3 = new Button();
        level3.setText("Level 3");
        level3.setMinSize(70, 30);
        level3.setOnAction(provider(Level3.class));

        StackPane root = new StackPane();
        BackgroundImage myBI= new BackgroundImage(new Image("images/wallpaper.png",400,200,false,true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(70, 70, 70, 70));
        Pane spacer1 = new Pane();
        spacer1.setMinSize(50, 1);
        Pane spacer2 = new Pane();
        spacer2.setMinSize(50, 1);
        buttons.getChildren().addAll(level1, spacer1, level2, spacer2, level3);
        root.getChildren().addAll(buttons);
        primaryStage.setScene(new Scene(root, 400, 100));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    EventHandler<ActionEvent> provider(Class<? extends Level> levelClass) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CandyGame game = new CandyGame(levelClass);
                CandyFrame frame = new CandyFrame(game);
                Scene scene = new Scene(frame);
                primaryStage.setResizable(false);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        };
    }

}
