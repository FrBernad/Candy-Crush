package game.frontend;

import game.backend.CandyGame;
import game.backend.GameListener;
import game.backend.cell.Cell;
import game.backend.element.Element;

import game.backend.element.NumberedCandy;
import game.backend.element.TimeCandy;
import game.backend.level.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CandyFrame extends VBox {

    private static final int CELL_SIZE = 65;

    private BoardPanel boardPanel;
    private InfoPanel scorePanel;
    private InfoPanel timePanel;
    private ImageManager images;
    private Point2D lastPoint;
    private CandyGame game;
    private Level gameLevel;

    public CandyFrame(CandyGame game) {
        this.game = game;
        getChildren().add(new AppMenu());
        images = new ImageManager();
        boardPanel = new BoardPanel(game.getSize(), game.getSize(), CELL_SIZE);
        getChildren().add(boardPanel);
        scorePanel = new InfoPanel();
        getChildren().add(scorePanel);
        timePanel = new InfoPanel();
        getChildren().add(timePanel);
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

    private void updatePanels(Timer timer) {
        Map<String, String> info = game.getInformation();
        scorePanel.updateMessage("Score: " + info.get("score"));
        timePanel.updateMessage(Level.condition + info.get("condition"));

        if (game().isFinished()) {
            if (game().playerWon()) {
                finishGame(String.valueOf(game.getScore()), "Player Won!", timer);
            } else {
                finishGame(String.valueOf(game.getScore()), "Player Lost!", timer);
            }
        }

    }

    public void finishGame(String score, String message, Timer timer) {
        timer.cancel();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Candy Crush 2.0");
        alert.setHeaderText(message);
        alert.setContentText("Your score was: " + score);
        ButtonType buttonTypeCancel = new ButtonType("Exit");
        alert.getButtonTypes().setAll(buttonTypeCancel);
        alert.showAndWait();
        Platform.exit();
    }

}
