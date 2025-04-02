package com.example.mytheaterapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyTickets extends AppCompatActivity {


    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private ArrayList<Ticket> tickets;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            tickets = loadTickets(this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Render Tickets on the RecyclerView through the TicketAdapter class
        ticketAdapter = new TicketAdapter(tickets);
        recyclerView.setAdapter(ticketAdapter);




    }








    private ArrayList<Ticket> loadTickets(Context context) throws JSONException {
        File directory = context.getFilesDir(); // Get internal storage directory

        // Filter files that contain "ticket" in the name
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().contains("ticket"); // Case-insensitive match
            }
        });

        // If there are no tickets return null
        if (files == null){
            return null;
        }

        // Parse Files into JSONObjects
        ArrayList<JSONObject> ticketJSONs = new ArrayList<>();
        for (File file: files) {
            StringBuilder content = new StringBuilder();
            try (FileInputStream fis = new FileInputStream(file)) {
                int ch;
                while ((ch = fis.read()) != -1) {
                    content.append((char) ch);
                }
                ticketJSONs.add(new JSONObject(content.toString())); // Convert to JSON
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Instantiate Ticket objects from the jsonObjects
        ArrayList<Ticket> tickets = new ArrayList<>();
        for (JSONObject ticketJSON: ticketJSONs){
            tickets.add(new Ticket(ticketJSON));

        }


        Toast.makeText(this, "tickets size:" + tickets.size(), Toast.LENGTH_SHORT).show();
        return tickets;
    }
}
