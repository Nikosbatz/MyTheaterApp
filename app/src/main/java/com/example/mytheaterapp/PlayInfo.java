package com.example.mytheaterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlayInfo extends AppCompatActivity {


    ImageView playImg;
    TextView playName;
    TextView playPlot;
    TextView playCast;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_play);

        Play play = (Play) getIntent().getSerializableExtra("play");

        playImg = findViewById(R.id.play_img);
        playName = findViewById(R.id.play_name);
        playPlot = findViewById(R.id.plot);
        playCast = findViewById(R.id.cast);

        int imgResId = getResources().getIdentifier(play.getImgPath(), "drawable", getPackageName());

        playImg.setImageResource(imgResId);
        playName.setText(play.getName());
        playPlot.setText(play.getPlot());
        playCast.setText(play.getCast().toString());
    }
}
