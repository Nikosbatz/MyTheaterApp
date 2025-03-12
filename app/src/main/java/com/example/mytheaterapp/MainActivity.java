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
        button_chat = findViewById(R.id.button_chat);


        ArrayList<String> cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play1 = new Play("1o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );

        cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play2 = new Play("2o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );


        // button functionality to open ChatBot
        button_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatBot.class);
                startActivity(intent);
            }
        });


    }
}