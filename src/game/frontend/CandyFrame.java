package game.frontend;

import game.backend.CandyGame;
import game.backend.GameListener;
import game.backend.cell.Cell;
import game.backend.element.Element;
import game.backend.element.NumberedCandy;
import game.backend.level.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class CandyFrame extends VBox {

    private static final int CELL_SIZE = 65;

    private BoardPanel boardPanel;
    private InfoPanel scorePanel;
    private InfoPanel timePanel;
    private ImageManager images;
    private Point2D lastPoint;
    private CandyGame game;
    private Level gameLevel;
    private Stage primaryStage;

    // Se añadieron paneles para mostrar la información de la jugada actual.
    public CandyFrame(CandyGame game, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.game = game;
        getChildren().add(new AppMenu(primaryStage));
        images = new ImageManager();
        boardPanel = new BoardPanel(game.getSize(), game.getSize(), CELL_SIZE);
        scorePanel = new InfoPanel("/images/Up.png");
        timePanel = new InfoPanel("/images/Down.png");
        getChildren().addAll(boardPanel, scorePanel, timePanel);
        game.initGame();
        gameLevel = (Level) game.getGrid();
        if (gameLevel.canUpdate()) {
            gameLevel.update();
        }
        GameListener listener;
        game.addGameListener(listener = new GameListener() {
            @Override
            public void gridUpdated() {
                Timeline timeLine = new Timeline();
                Duration frameGap = Duration.millis(100);
                Duration frameTime = Duration.ZERO;
                for (int i = game().getSize() - 1; i >= 0; i--) {
                    for (int j = game().getSize() - 1; j >= 0; j--) {
                        int finalI = i;
                        int finalJ = j;

                        Cell cell = CandyFrame.this.game.get(i, j);
                        Element element = cell.getContent();
                        Image image = images.getImage(element);
                        timeLine.getKeyFrames().add(new KeyFrame(frameTime, e -> boardPanel.setImage(finalI, finalJ, null)));
                        timeLine.getKeyFrames().add(new KeyFrame(frameTime, e -> boardPanel.setImage(finalI, finalJ, image)));
                        if (element instanceof NumberedCandy) {
                            timeLine.getKeyFrames().add(new KeyFrame(frameTime, e -> boardPanel.setNumber(finalI, finalJ, (NumberedCandy) element)));
                        }

                    }
                    frameTime = frameTime.add(frameGap);
                }

                timeLine.play();
            }

            @Override
            public void cellExplosion(Element e) {
                //
            }
            
        });
     
        listener.gridUpdated();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updatePanels(timer);
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (lastPoint == null) {
                lastPoint = translateCoords(event.getX(), event.getY());
                System.out.println("Get first = " + lastPoint);//"Get first = " + lastPoint
            } else {
                Point2D newPoint = translateCoords(event.getX(), event.getY());

                if (newPoint != null) {
                    System.out.println("Get second = " + newPoint);//"Get second = " + newPoint

                    game().tryMove((int) lastPoint.getX(), (int) lastPoint.getY(), (int) newPoint.getX(), (int) newPoint.getY());

                    lastPoint = null;
                }
            }
        });

    }

    private CandyGame game() {
        return game;
    }

    private Point2D translateCoords(double x, double y) {
        double i = x / CELL_SIZE;
        double j = y / CELL_SIZE;
        return (i >= 0 && i < game.getSize() && j >= 0 && j < game.getSize()) ? new Point2D(j, i) : null;
    }
    
    // Método encargado de actualizar la información que obtiene del backend.
    private void updatePanels(Timer timer) {
        Map<String, String> info = game.getInformation();
        scorePanel.updateMessage("Score: " + info.get("score") + "/" + gameLevel.getMaxScore());
        timePanel.updateMessage(gameLevel.getCondition() + info.get("condition"));

        if (game().isFinished()) {
            if (game().playerWon()) {
                finishGame(String.valueOf(game.getScore()), "Player Won!", timer);
            } else {
                finishGame(String.valueOf(game.getScore()), "Player Lost!", timer);
            }
        }

    }

    // Ventana que se abre cuando la jugada termina, ganándola o perdiéndola.
    // Permite volver al menú de selección de niveles o salir del juego.
    public void finishGame(String score, String message, Timer timer) {
        timer.cancel();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game ended");
        alert.setHeaderText(message + "\n" + "Your score was: " + score);
        alert.setContentText("Choose one of the following options:");
        ImageView graphic=new ImageView("images/graphic.png");
        alert.setGraphic(graphic);
        ButtonType buttonTypeOne = new ButtonType("Back to menu");
        ButtonType buttonTypeTwo = new ButtonType("Exit game");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            primaryStage.close();
            Platform.runLater(() -> new GameApp().start(new Stage()));
        } else if (result.get() == buttonTypeTwo) {
            Platform.exit();
        }
    }
}
