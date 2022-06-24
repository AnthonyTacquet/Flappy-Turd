package logic;

import android.view.View;

public class Pipe{
    private final int GAP = 650;
    private Tube tube1;
    private Tube tube2;
    private final int WIDTH = 60;

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

    public void createRandomPipe(int height, int width){
        int lenghtTube1 = (int) (Math.random() * 800 + 20);
        int lengthTube2 = height - lenghtTube1 - GAP;
        tube1 = new Tube(new Coordinates(width,0,lenghtTube1, WIDTH));
        tube2 = new Tube(new Coordinates(width,height - lengthTube2,lengthTube2, WIDTH));
    }

    public void moveX(int x){
        tube1.setX(tube1.getX() - x);
        tube2.setX(tube2.getX() - x);
    }
}
