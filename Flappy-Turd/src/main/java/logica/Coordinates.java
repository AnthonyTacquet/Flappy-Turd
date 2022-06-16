package logica;

public class Coordinates {
    private int x = 0;
    private int y = 0;
    private int length = 0;
    private int width = 0;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(int x, int y, int length, int width){
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getLength(){
        return this.length;
    }

    public int getWidth(){
        return this.width;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setLength(int length){
        this.length = length;
    }

    public void setWidth(int width){
        this.width = width;
    }
}
