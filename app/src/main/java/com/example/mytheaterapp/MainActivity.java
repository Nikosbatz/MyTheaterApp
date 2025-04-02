package com.example.mytheaterapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;
    MenuItem button_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For DEBUGGING purposes
        deleteInternalFiles(this);
        //-------------

        // Find Buttons Ids
        bottom_nav = findViewById(R.id.bottom_nav);
        button_chat = bottom_nav.getMenu().findItem(R.id.button_chat);;



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

    private void deleteInternalFiles(Context context) {
        File dir = context.getFilesDir(); // Get internal storage directory
        for (File file: Objects.requireNonNull(dir.listFiles())){
            file.delete();
        }
    }
}