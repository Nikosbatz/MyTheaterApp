package com.example.mytheaterapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MyTicketsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private ArrayList<Ticket> tickets;
    private Context context;

    // Empty constructor
    MyTicketsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);
        context = getActivity();


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        try {
            tickets = loadTickets(context);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Render Tickets on the RecyclerView through the TicketAdapter class
        ticketAdapter = new TicketAdapter(tickets);
        recyclerView.setAdapter(ticketAdapter);


        return view;
    }

    public static ArrayList<Ticket> loadTickets(Context context) throws JSONException {
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
            try {
                tickets.add(new Ticket(ticketJSON));
            }
            catch (WriterException e){
            }


        }


        // DEBUG
        Toast.makeText(context, "tickets size:" + tickets.size(), Toast.LENGTH_SHORT).show();
        return tickets;
    }


}
