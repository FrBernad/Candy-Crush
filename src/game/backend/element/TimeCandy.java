package game.backend.element;

public class TimeCandy extends NumberedCandy {
    protected int extraTime;

    public TimeCandy(CandyColor color, int extraTime) {
        super(color);
        this.extraTime = extraTime;
    }

    @Override
    public String getNumber() {
        return "+" + String.valueOf(extraTime);
    }

    public int getExtraTime() {
        return extraTime;
    }

}
