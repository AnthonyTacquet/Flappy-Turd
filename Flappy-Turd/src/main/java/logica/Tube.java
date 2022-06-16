package logica;

import javax.swing.*;
import java.awt.*;

public class Tube extends Coordinates{
    Color color = Color.WHITE;

    public Tube(){
        super(0,0,0,0);
    }

    public Tube(Coordinates coordinates){
        super(coordinates.getX(), coordinates.getY(), coordinates.getLength(), coordinates.getWidth());
    }

    public Tube(Coordinates coordinates, Color color){
        super(coordinates.getX(), coordinates.getY(), coordinates.getLength(), coordinates.getWidth());
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color color){
        this.color = color;
    }
}
