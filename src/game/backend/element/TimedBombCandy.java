package game.backend.element;

public class TimedBombCandy extends NumberedCandy{
    protected int movementsLeft;

    public TimedBombCandy(CandyColor color,int movementsLeft) {
        super(color);
        this.movementsLeft = movementsLeft;
    }

    @Override
    public String getNumber() {
        return String.valueOf(movementsLeft);
    }

    public int getMovementsLeft() {
        return movementsLeft;
    }

    public int countDownMovements()
    {
       return --movementsLeft;
    }

    @Override
    public String toString() {
        return "TimedBombCandy";
    }
}
