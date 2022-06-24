package com.antwaan.flappyturd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import logic.Coordinates;
import logic.Mathematics;
import logic.Pipe;
import logic.Sound;
import logic.Timer;
import logic.Tube;
import logic.Turd;

public class GameActivity extends AppCompatActivity{

    Turd bird;
    boolean jump = false;
    boolean gameover = false;

    int score = 0;

    Thread jumpThread = new Thread();
    Thread fallingThread = new Thread();
    Thread pipesMoveThread = new Thread();
    Thread pipesCreateThread = new Thread();
    Thread collisionThread = new Thread();
    Thread repaintThread = new Thread();
    Thread themeThread = new Thread();

    ArrayList<Pipe> pipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);
        setContentView(new MyView(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);   // this super call is important !!!
        jump = true;
        return true;
    }

    public void stop(){
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }



    public class MyView extends View
    {
        Paint paint = null;
        public MyView(Context context)
        {
            super(context);
            paint = new Paint();
            startGame();
        }

        public void startGame(){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            bird = new Turd(new Coordinates(100,height / 2,25,25));

            pipes.clear();
            Pipe startPipe = new Pipe();
            startPipe.createRandomPipe(height, width);
            pipes.add(startPipe);
            createPipes();
            movePipes();

            falling();
            collision();
            repaint();
            themeSound();
        }

        public void gameOver(){
            gameover = true;
            playSound(Sound.HIT);
            System.out.println("bird: " + bird.getX() + " : " + bird.getY());
            System.out.println("Tube 1 : " + pipes.get(0).getTube1().getX() + " : " + pipes.get(0).getTube1().getY());
            System.out.println("Tube 2 : " + pipes.get(0).getTube2().getX() + " : " + pipes.get(0).getTube2().getY());

            collisionThread.interrupt();
            while (pipesCreateThread.isAlive() || pipesMoveThread.isAlive()){
                System.out.println("test");
            }
            pipes.clear();
            stop();
        }

        public void jump(){
            if (jumpThread.isAlive())
                jumpThread.interrupt();
            jumpThread = new Thread(() -> {
                Timer timer = new Timer(2);
                timer.startTimer();
                int distance = 1;
                for (int i = 0; i < 30; i++) {
                    switch (i){
                        case 5: distance = 2; break;
                        case 10: distance = 4; break;
                        case 15: distance = 6; break;
                        case 20: distance = 8; break;
                        case 25: distance = 10; break;

                    }
                    if (!timer.getThread().isAlive()) {
                        timer.resetTimer();
                        bird.setY(bird.getY() - distance);
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
                Timer fallingTimer = new Timer(2);
                fallingTimer.startTimer();
                int i = 0;
                int distance = 0;

                while (!gameover){
                    if (jump){
                        i = 0;
                        distance = 0;
                        jump = false;
                        jump();
                        playSound(Sound.JUMP);
                    } else if (!fallingTimer.getThread().isAlive() && !jumpThread.isAlive()){
                        switch (i){
                            case 0: distance = 1; break;
                            case 5: distance = 2; break;
                            case 10: distance = 3; break;
                            case 20: distance = 4; break;
                            case 25: distance = 5; break;
                        }
                        bird.setY(distance + bird.getY());
                        fallingTimer.resetTimer();
                        i++;
                    }
                }
            });
            fallingThread.start();
        }

        public void movePipes(){
            pipesMoveThread = new Thread(() -> {
                while (!gameover){
                    try {
                        Thread.sleep(50);
                        for (int i = 0; i < pipes.size(); i++){
                            pipes.get(i).moveX(10);
                            checkScore(pipes.get(i));
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                }
            });
            pipesMoveThread.start();
        }

        public void createPipes(){
            pipesCreateThread = new Thread(() -> {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                while (!gameover){
                    for (int i = 0; i < pipes.size(); i++) {
                        if (pipes.get(i).getTube1().getX() < -pipes.get(i).getTube1().getWidth()){
                            pipes.remove(i);
                        }
                    }

                    if (pipes.size() < 10 && pipes.get(pipes.size() - 1).getTube1().getX() < 100){
                        Pipe pipe = new Pipe();
                        pipe.createRandomPipe(height, width);
                        pipes.add(pipe);
                    }

                }
            });
            pipesCreateThread.start();
        }

        public void checkScore(Pipe pipe){
            if (pipe.getTube1().getX() == bird.getX())
                score++;
        }

        public void repaint(){
            repaintThread = new Thread(() -> {
                while (!gameover){
                    //System.out.print("");
                    invalidate();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            repaintThread.start();
        }

        public void collision(){
            collisionThread = new Thread(() -> {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                while (!gameover){
                    for (int i = 0; i < pipes.size(); i++){
                        if (gameover)
                            return;
                        //if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY(), tubeBitUpside.getWidth(), tubeBitUpside.getHeight(), bird.getX(), bird.getY(), bird.getLength()))
                        //    gameOver();
                        //else if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), tubeBit.getWidth(), tubeBit.getHeight(), bird.getX(), bird.getY(), bird.getLength()))
                        //    gameOver();

                        try {
                            if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY(), pipes.get(i).getTube1().getWidth(), pipes.get(i).getTube1().getLength(), bird.getX(), bird.getY(), bird.getLength()))
                                gameOver();
                            else if (Mathematics.cirkelOverlaptMetRechthoek(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), pipes.get(i).getTube2().getWidth(), pipes.get(i).getTube2().getLength(), bird.getX(), bird.getY(), bird.getLength()))
                                gameOver();
                            else if (bird.getY() > height || bird.getY() < 0)
                                gameOver();
                        } catch (Exception exception){
                            System.out.println(exception.getMessage());
                        }
                    }
                }
            });
            collisionThread.start();
        }

        public void playSound(Sound sound){
            switch (sound){
                case SELECT: final MediaPlayer SELECT = MediaPlayer.create(GameActivity.this, R.raw.select); SELECT.start(); break;
                case HIT: final MediaPlayer HIT = MediaPlayer.create(GameActivity.this, R.raw.hit); HIT.start(); break;
                case JUMP: final MediaPlayer JUMP = MediaPlayer.create(GameActivity.this, R.raw.jump); JUMP.start(); break;
            }
        }

        public void themeSound(){
            if (themeThread.isAlive())
                themeThread.interrupt();
            themeThread = new Thread(() -> {
                final MediaPlayer THEME = MediaPlayer.create(GameActivity.this, R.raw.theme);
                THEME.start();
                while (!gameover){
                    if (!THEME.isPlaying()){
                        THEME.start();
                    }
                }
                THEME.stop();
            });
            themeThread.start();
        }

        Bitmap birdBit = BitmapFactory.decodeResource(getResources(), R.raw.bird);
        Bitmap tubeBit = BitmapFactory.decodeResource(getResources(), R.raw.tube);
        Bitmap tubeBitUpside = BitmapFactory.decodeResource(getResources(), R.raw.tubelopside);

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawBitmap(birdBit, bird.getX() - bird.getLength() / 2,bird.getY() - bird.getLength() / 2, paint);

            for (int i = 0; i < pipes.size(); i++){
                canvas.drawBitmap(tubeBit, pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), paint);
                canvas.drawBitmap(tubeBitUpside, pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY() + pipes.get(i).getTube1().getLength() - tubeBitUpside.getHeight(), paint);
                //canvas.drawRect(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), width - pipes.get(i).getTube2().getX() + pipes.get(i).getTube2().getWidth(), height - pipes.get(i).getTube2().getY() + pipes.get(i).getTube2().getLength(), paint);
                //canvas.drawRect(pipes.get(i).getTube2().getX(), pipes.get(i).getTube2().getY(), pipes.get(i).getTube2().getX() + pipes.get(i).getTube2().getWidth(), pipes.get(i).getTube2().getY() + pipes.get(i).getTube2().getLength(), paint);
                //canvas.drawRect(pipes.get(i).getTube1().getX(), pipes.get(i).getTube1().getY(), pipes.get(i).getTube1().getX() + pipes.get(i).getTube1().getWidth(), pipes.get(i).getTube1().getY() + pipes.get(i).getTube1().getLength(), paint);

            }
            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            canvas.drawText("Score: " + score, 10, 100, paint);
        }
    }

}