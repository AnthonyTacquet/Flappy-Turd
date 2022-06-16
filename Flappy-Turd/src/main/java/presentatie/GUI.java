package presentatie;

import logica.*;
import logica.Timer;
import org.xml.sax.helpers.AttributesImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;

public class GUI {
    private JPanel main;
    private JPanel card;
    private JPanel titleScreen;
    private JPanel gameScreen;
    private JPanel endScreen;

    Turd bird;
    boolean jump = false;
    boolean gameover = false;

    int score = 0;

    Thread jumpThread = new Thread();
    Thread fallingThread = new Thread();
    Thread pipesMoveThread = new Thread();
    Thread pipesCreateThread = new Thread();
    Thread collisionThread = new Thread();

    ArrayList<Pipe> pipes = new ArrayList<>();

    public GUI() {
        titleScreen.setFocusable(true);
        gameScreen.setVisible(false);
        titleScreen.setVisible(true);
        endScreen.setVisible(false);

        gameScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    jump = true;
                }
            }
        });
        titleScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    startGame();
                }
            }
        });
        endScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    titleScreen.setFocusable(true);
                    gameScreen.setVisible(false);
                    titleScreen.setVisible(true);
                    endScreen.setVisible(false);
                    startGame();
                }
            }
        });
    }

    public void startGame(){
        gameScreen.setFocusable(true);
        gameScreen.setVisible(true);
        titleScreen.setVisible(false);
        endScreen.setVisible(false);
        bird = new Turd(new Coordinates(50,50,16,16));

        Pipe startPipe = new Pipe();
        startPipe.createRandomPipe();
        pipes.add(startPipe);
        createPipes();
        movePipes();

        falling();
        collision();
    }

    public void gameOver(){
        gameover = true;
        pipes.clear();
        gameScreen.setVisible(false);
        endScreen.setVisible(true);
        titleScreen.setVisible(false);
        endScreen.repaint();
    }

    public void jump(){
        jumpThread = new Thread(() -> {
            Timer timer = new Timer(15);
            timer.startTimer();
            for (int i = 0; i < 10; i++) {
                if (!timer.getThread().isAlive()) {
                    timer.resetTimer();
                    bird.setY(bird.getY() - 8);
                    gameScreen.repaint();
                } else {
                    i--;
                }
                System.out.print("");
            }
        });
        jumpThread.start();
    }

    public void falling(){
        fallingThread = new Thread(() -> {
            Timer fallingTimer = new Timer(30);
            fallingTimer.startTimer();

            while (!gameover){
                //System.out.print("");
                if (jump){
                    jump = false;
                    //System.out.println("ok");
                    jump();
                } else if (!fallingTimer.getThread().isAlive() && !jumpThread.isAlive()){
                    bird.setY(bird.getY() + 8);
                    fallingTimer.resetTimer();
                    //System.out.println("ok");
                    gameScreen.repaint();
                }
            }
        });
        fallingThread.start();
    }

    public void movePipes(){
         pipesMoveThread = new Thread(() -> {
            while (!gameover){
                try {
                    Thread.sleep(100);
                    for (int i = 0; i < pipes.size(); i++){
                        pipes.get(i).moveX(10);
                        checkScore(pipes.get(i));
                        gameScreen.repaint();
                        //System.out.println(pipes.get(i).getTube1().getX());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
         });
         pipesMoveThread.start();
    }

    public void createPipes(){
        pipesCreateThread = new Thread(() -> {
            while (!gameover){
                for (int i = 0; i < pipes.size(); i++) {
                    if (pipes.get(i).getTube1().getX() < 0){
                        pipes.remove(i);
                    }
                }

                if (pipes.size() < 6 && pipes.get(pipes.size() - 1).getTube1().getX() < 350){
                    Pipe pipe = new Pipe();
                    pipe.createRandomPipe();
                    pipes.add(pipe);
                    gameScreen.repaint();
                }

            }
        });
        pipesCreateThread.start();
    }

    public void checkScore(Pipe pipe){
        if (pipe.getTube1().getX() == bird.getX())
            score++;
        System.out.println(score);
    }

    public void collision(){
        collisionThread = new Thread(() -> {
            while (!gameover){
                for (int i = 0; i < pipes.size(); i++){
                    if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY(), pipes.get(i).getTube1().getWidth(), pipes.get(i).getTube1().getLength(), bird.getX(), bird.getY(), bird.getLength()))
                        gameOver();
                    else if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), pipes.get(i).getTube2().getWidth(), pipes.get(i).getTube2().getLength(), bird.getX(), bird.getY(), bird.getLength()))
                        gameOver();
                    else if (bird.getY() > 600 || bird.getY() < 0)
                        gameOver();
                }
            }
        });
        collisionThread.start();
    }

    private void createUIComponents() {
        Image titlescreen = laadAfbeelding("Images/titlescreen");
        Image endscreen = laadAfbeelding("Images/gameover");
        Image turdImage = laadAfbeelding("Images/turd");

        gameScreen = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setBackground(Color.BLUE);
                g2.setColor(Color.DARK_GRAY);
                //g2.fillOval(bird.getX(), bird.getY(), bird.getLength(), bird.getWidth());
                g2.drawImage(turdImage, bird.getX() - bird.getLength() / 2, bird.getY() - bird.getLength() / 2, null);

                for (int i = 0; i < pipes.size(); i++){
                    g2.setColor(Color.GREEN);
                    g2.fillRect(pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY(), pipes.get(i).getTube1().getWidth(), pipes.get(i).getTube1().getLength());
                    g2.fillRect(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), pipes.get(i).getTube2().getWidth(), pipes.get(i).getTube2().getLength());
                }

            }
        };

        endScreen = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (gameover){
                    g2.drawImage(endscreen, 0,0,null);
                }
            }
        };

        titleScreen = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(titlescreen,0,0,null);
            }
        };
    }

    private static Image laadAfbeelding(String bestand) {
        try {
            URL resource = GUI.class.getResource("/" + bestand + ".png");
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ImageIcon laadIcoon(String bestand) {
        return new ImageIcon(laadAfbeelding(bestand));
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600,600);
        frame.setVisible(true);
    }
}
