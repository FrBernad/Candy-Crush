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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApp extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		primaryStage.setTitle("CandyCrush");
        Button level1 = new Button();
        level1.setText("Level 1");
        level1.setMinSize(70, 30);
        level1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	CandyGame game = new CandyGame(Level1.class);
                CandyFrame frame = new CandyFrame(game);
                Scene scene = new Scene(frame);
                primaryStage.setResizable(false);
                primaryStage.setScene(scene);
                primaryStage.show();
            
            }
        });
        
        Button level2 = new Button();
        level2.setText("Level 2");
        level2.setMinSize(70, 30);
        level2.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	CandyGame game = new CandyGame(Level2.class);
                CandyFrame frame = new CandyFrame(game);
                Scene scene = new Scene(frame);
                primaryStage.setResizable(false);
                primaryStage.setScene(scene);
                primaryStage.show();
            
            }
        });
        
 
        Button level3 = new Button();
        level3.setText("Level 3");
        level3.setMinSize(70, 30);       
        level3.setOnAction(new EventHandler<ActionEvent>() {
          
        	@Override
            public void handle(ActionEvent event) {
              

        		CandyGame game = new CandyGame(Level3.class);
                CandyFrame frame = new CandyFrame(game);
                Scene scene = new Scene(frame);
                primaryStage.setResizable(false);
                primaryStage.setScene(scene);
                primaryStage.show();

                
            }           
        });
        
        StackPane root = new StackPane();
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(70,70,70,70));
        Pane spacer1 = new Pane();
        spacer1.setMinSize(50, 1);
        Pane spacer2 = new Pane();
        spacer2.setMinSize(50, 1);
        buttons.getChildren().addAll(level1,spacer1,level2,spacer2,level3);
        root.getChildren().addAll(buttons);
        primaryStage.setScene(new Scene(root, 400, 100));
        primaryStage.show();
			 
	}

}
