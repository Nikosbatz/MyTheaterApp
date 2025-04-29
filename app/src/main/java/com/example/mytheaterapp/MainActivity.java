package com.example.mytheaterapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

    MenuItem homeIcon;
    MenuItem myTicketsIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean startedByActivity = getIntent().getBooleanExtra("flag", false);
        Log.d("DEBUG", ""+startedByActivity);


        // For DEBUGGING purposes
        //deleteInternalFiles(this);
        //-------------

        // Find Buttons Ids
        bottom_nav = findViewById(R.id.bottom_nav);
        button_chat = bottom_nav.getMenu().findItem(R.id.chat_icon);
        homeIcon = bottom_nav.getMenu().findItem(R.id.home_icon);
        myTicketsIcon = bottom_nav.getMenu().findItem(R.id.myTickets_icon);


        if (!startedByActivity) {
            // Load HomeFragment by default
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
        }
        else {
            // Load MyTicketsFragment if the activity is started after payment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new MyTicketsFragment())
                    .commit();

        }

        // OPENAI API TEST ------------
        OpenAi.chatGPT("Hello which model are you", new OpenAi.Callback(){
            @Override
            public void onResponse(String result) {
                runOnUiThread(() -> Log.d("api", result));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Log.d("api", "ERROR"));
            }
        } );
        // OPENAI API TEST ------------



        // button functionality to open ChatBot
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.chat_icon){
                    startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
                }
                else if(item.getItemId()==R.id.home_icon){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new HomeFragment())
                            .commit();
                }
                else if(item.getItemId()==R.id.myTickets_icon){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new MyTicketsFragment())
                            .commit();
                }
                return true;
            }
        });
    }

    /*// Action to be performed when MenuItem is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("DEBUG", "MENU ITEM SELELCTED");
        int id = item.getItemId();
        // if Home is Selected in the Menu
        if (id == R.id.home_icon){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
        }
        // if MyTickets is Selected in the Menu
        else if (id == R.id.myTickets_icon) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new MyTicketsFragment())
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get menu Items references
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);

        // Get menu item by ID
        homeIcon = menu.findItem(R.id.home_icon);
        myTicketsIcon = menu.findItem(R.id.myTickets_icon);

        return true;
    }*/
    private void deleteInternalFiles(Context context) {
        File dir = context.getFilesDir(); // Get internal storage directory
        for (File file: Objects.requireNonNull(dir.listFiles())){
            file.delete();
        }
    }
}