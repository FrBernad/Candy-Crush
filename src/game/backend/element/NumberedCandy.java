package game.backend.element;

public abstract class NumberedCandy extends Candy{

    protected boolean stillUp=true;

    public NumberedCandy(CandyColor color) {
        super(color);
    }

    public abstract String getNumber();

    public boolean stillUp()
    {
        return stillUp;
    }

     public void killCandy()
    {
        stillUp=false;
    }

}
