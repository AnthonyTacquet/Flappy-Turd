package logica;

public class Pipe{
    private final int GAP = 200;
    private Tube tube1;
    private Tube tube2;

    public Pipe(){
        this.tube1 = new Tube();
        this.tube2 = new Tube();
    }

    public Pipe(Tube tube1, Tube tube2){
        this.tube1 = new Tube(tube1);
        this.tube2 = new Tube(tube2);
    }

    public Tube getTube1(){
        return this.tube1;
    }

    public Tube getTube2(){
        return this.tube2;
    }

    public void createRandomPipe(){
        int lenghtTube1 = (int) (Math.random() * 300 + 20);
        int lengthTube2 = 600 - lenghtTube1 - GAP;
        tube1 = new Tube(new Coordinates(500,0,lenghtTube1, 50));
        tube2 = new Tube(new Coordinates(500,600 - lengthTube2,lengthTube2, 50));
    }

    public void moveX(int x){
        tube1.setX(tube1.getX() - x);
        tube2.setX(tube2.getX() - x);
    }
}
