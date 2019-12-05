package game.backend.level;

import game.backend.GameState;
import game.backend.cell.CandyGeneratorCell;

import java.lang.reflect.InvocationTargetException;

public class Level1 extends Level {
	
	private static int REQUIRED_SCORE = 5000; 
	private static int MAX_MOVES = 20;

	public Level1() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		super();
		setGenerator(CandyGeneratorCell.class);
	}

	@Override
	public void update() {
		//
	}

	@Override
	protected GameState newState() {
		return new Level1State(REQUIRED_SCORE, MAX_MOVES);
	}

	private class Level1State extends GameState {
		private long requiredScore;
		private long maxMoves;
		
		public Level1State(long requiredScore, int maxMoves) {
			this.requiredScore = requiredScore;
			this.maxMoves = maxMoves;
		}
		
		public boolean gameOver() {
			return playerWon() || getMoves() >= maxMoves;
		}
		
		public boolean playerWon() {
			return getScore() > requiredScore;
		}
	}

}
