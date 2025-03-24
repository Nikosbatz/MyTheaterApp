package com.example.mytheaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;
    MenuItem button_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("error", "PRIN TO LISTENER");

        // Find Buttons Ids
        bottom_nav = findViewById(R.id.bottom_nav);
        button_chat = bottom_nav.getMenu().findItem(R.id.button_chat);;



        /*ArrayList<String> cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play1 = new Play("1o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );

        cast = new ArrayList<>(Arrays.asList("ena", "duo", "tria"));
        Play play2 = new Play("2o_ergo", cast, "comedy", "ewqeq qweqwe sadasfe qeewqewq asfas asdq weq", "gladiator" );
        */

        Log.d("error", "PRIN TO LISTENER");
        // button functionality to open ChatBot
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.button_chat){
                    //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,chatFragment).commit();
                    startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
                }
                /*if(item.getItemId()==R.id.menu_profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,profileFragment).commit();
                }*/
                return true;
            }
        });






    }
}