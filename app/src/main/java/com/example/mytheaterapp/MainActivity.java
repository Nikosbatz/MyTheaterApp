package com.example.mytheaterapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ImageView imgPlay1;
    ImageView imgPlay2;
    FloatingActionButton button_chat;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find Buttons Ids
        imgPlay1 = findViewById(R.id.imgPlay1);
        imgPlay2 = findViewById(R.id.imgPlay2);
        button_chat = findViewById(R.id.button_chat);

        ArrayList<String> cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play1 = new Play("1o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );

        cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play2 = new Play("2o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );


        imgPlay1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator.ofFloat(imgPlay1, "scaleX", 1.1f).setDuration(90).start();
                        ObjectAnimator.ofFloat(imgPlay1, "scaleY", 1.1f).setDuration(90).start();
                        imgPlay1.setAlpha(0.7f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ObjectAnimator.ofFloat(imgPlay1, "scaleX", 1.0f).setDuration(100).start();
                        ObjectAnimator.ofFloat(imgPlay1, "scaleY", 1.0f).setDuration(100).start();
                        imgPlay1.setAlpha(1f);
                        break;
                }
                return false;
            }
        });

        imgPlay2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator.ofFloat(imgPlay2, "scaleX", 1.1f).setDuration(90).start();
                        ObjectAnimator.ofFloat(imgPlay2, "scaleY", 1.1f).setDuration(90).start();
                        imgPlay2.setAlpha(0.7f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ObjectAnimator.ofFloat(imgPlay2, "scaleX", 1.0f).setDuration(100).start();
                        ObjectAnimator.ofFloat(imgPlay2, "scaleY", 1.0f).setDuration(100).start();
                        imgPlay2.setAlpha(1f);
                        break;
                }
                return false;
            }
        });

        // Start the Play1 Activity
        imgPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayInfo.class);
                intent.putExtra("play", play1);
                startActivity(intent);
            }
        });

        // Start the Play2 Activity
        imgPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayInfo.class);
                intent.putExtra("play", play2);
                startActivity(intent);
            }
        });

    }
}