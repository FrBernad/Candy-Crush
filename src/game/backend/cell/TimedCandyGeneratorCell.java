package game.backend.cell;

import game.backend.Grid;
import game.backend.element.Candy;
import game.backend.element.CandyColor;
import game.backend.element.Element;
import game.backend.element.TimeCandy;
import game.backend.level.Level2;

public class TimedCandyGeneratorCell extends CandyGeneratorCell {

    private static final double THRESHOLD=0.1;

    private boolean throwTimeCandies=true;
    private boolean startCounting=false;

    public TimedCandyGeneratorCell(Grid grid) {
		super(grid);
	}

    public void startCounting(){
        startCounting=true;
    }

	public void throwTimeCandies(boolean value){
        throwTimeCandies=value;
    }


    @Override
    public Element getContent() {
        int i = (int)(Math.random() * CandyColor.values().length);

        if(throwTimeCandies)
        {
            int extraTime = (int)(Math.random() * 10)+5;
            double j = Math.random();

            if(j<THRESHOLD) {
                Candy aux = new TimeCandy(CandyColor.values()[i],extraTime);;
                ((Level2)grid).add(aux);

                if(startCounting)
                    ((Level2)grid).countDown();

                return aux;
            }
        }

       return new Candy(CandyColor.values()[i]);
    }
}
