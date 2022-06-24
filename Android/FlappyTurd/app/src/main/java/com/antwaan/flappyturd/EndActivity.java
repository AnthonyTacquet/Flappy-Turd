package com.antwaan.flappyturd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import logic.Sound;

public class EndActivity extends AppCompatActivity implements View.OnClickListener {

    Button BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        BackButton = (Button) findViewById(R.id.BackButton);
        BackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BackButton){
            playSound(Sound.SELECT);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void playSound(Sound sound){
        if (sound == Sound.SELECT){
            final MediaPlayer SELECT = MediaPlayer.create(EndActivity.this, R.raw.select);
            SELECT.start();
        }

    }
}