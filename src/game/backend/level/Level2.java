package game.backend.level;

import game.backend.Figure;
import game.backend.GameState;
import game.backend.cell.Cell;
import game.backend.cell.TimedCandyGeneratorCell;
import game.backend.element.*;
import game.backend.move.Move;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level2 extends Level {

    private static long TIME_LEFT = 50;
    private int REQUIRED_SCORE = 5000;
    private static int MAX_CANDIES = 4;
    private int candiesThrown = MAX_CANDIES;
    private boolean started = false;
    private boolean flag = false;
    protected List<NumberedCandy> timeCandies = new ArrayList<>();


    public Level2() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super();
        setGenerator(TimedCandyGeneratorCell.class);
    }


    public void add(Candy candy)
    {
        timeCandies.add((NumberedCandy)candy);
    }


    @Override
    public Figure tryRemove(Cell cell) {
        return super.tryRemove(cell);
    }


    @Override
    public boolean canUpdate() {
        return true;
    }


    public void countDown()
    {
        candiesThrown--;
        if(candiesThrown<=0)
            ((TimedCandyGeneratorCell)generator).throwTimeCandies(false);
    }

//cada vez q clickeo

    public void update() {

        if (!flag) {
            flag=true;
             ((Level2State)state()).updateState();
        }

                Iterator<NumberedCandy> it=timeCandies.iterator();

                    NumberedCandy candy=it.next();
                    if (!candy.stillUp())
                    {
                        if (started)
                            ((Level2State)state()).addTime(((TimeCandy)candy).getExtraTime());

                        it.remove();
                    }


    }

    @Override
    public boolean tryMove(int i1, int j1, int i2, int j2) {
		Move move = moveMaker.getMove(i1, j1, i2, j2);
		swapContent(i1, j1, i2, j2);
		if (move.isValid()) {
		    if(!started)
            {
                ((TimedCandyGeneratorCell)generator).startCounting();
                started=true;
            }
		    update();
			move.removeElements();
			fallElements();
			return true;
		} else {
			swapContent(i1, j1, i2, j2);
			return false;
		}
	}

    @Override
    protected GameState newState() {
        return new Level2State(TIME_LEFT, REQUIRED_SCORE);
    }




    private class Level2State extends GameState {

        private long timeLeft;
        private int requiredScore;

        private Timer timer=new Timer();

        public void updateState()
        {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new TimerTask() {
                        @Override
                        public void run() {
                            updateTime();
                            gameOver();
                        }
                    });
                }
             },0,1000);
        }

        public Level2State(long timeLeft, int requiredScore) {
            this.timeLeft = timeLeft;
            this.requiredScore = requiredScore;
        }

        public boolean gameOver() {
            return playerWon()  || timeIsUp();
        }

        public boolean playerWon() {
            return getScore() > requiredScore;
        }

        public boolean timeIsUp() {
            if(timeLeft>0)
                return false;

            timer.cancel();
            return true;
        }

        public void updateTime() {
            timeLeft -= 1;
        }

        @Override
        public Map<String,String> getInfo() {
		Map<String,String> generalInfo= new HashMap<>();
		generalInfo.put("score",String.valueOf(getScore()));
		generalInfo.put("condition",String.valueOf(timeLeft));
		return generalInfo;
	    }

        public void addTime(long extraTime) {
            timeLeft += extraTime;
        }

    }

}

