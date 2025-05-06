package com.example.mytheaterapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
        } else {
            // Load MyTicketsFragment if the activity is started after payment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new MyTicketsFragment())
                    .commit();

        }

        // button functionality to open ChatBot
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.chat_icon) {
                    startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
                } else if (item.getItemId() == R.id.home_icon) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new HomeFragment())
                            .commit();
                } else if (item.getItemId() == R.id.myTickets_icon) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new MyTicketsFragment())
                            .commit();
                }
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
    private String loadJsonFromAssets(Context context, String fileName){
        String json = null;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


}