package logic;

public class Turd extends Coordinates{

    public Turd(){
        super(0,0,0,0);
    }

    public Turd(Coordinates coordinates){
        super(coordinates.getX(), coordinates.getY(), coordinates.getLength(), coordinates.getWidth());
    }
}
