package com.antwaan.flappyturd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import data.ReadWrite;
import logic.Sound;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button GameButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameButton = (Button) findViewById(R.id.GameButton);
        GameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.GameButton){
            playSound(Sound.SELECT);
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }

    public void playSound(Sound sound){
        if (sound == Sound.SELECT){
            final MediaPlayer SELECT = MediaPlayer.create(MainActivity.this, R.raw.select);
            SELECT.start();
        }

    }
}