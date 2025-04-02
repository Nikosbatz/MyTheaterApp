package com.example.mytheaterapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Ticket implements Serializable {
    private String performance;
    private String id;
    private String date;
    private ArrayList<Integer> seats;
    private boolean isActive;
    private int quantity;


    public Ticket(String performance,  String date, String quantity){
        this.performance = performance;
        this.date = date;
        this.quantity = Integer.parseInt(quantity);
        isActive = false;
        seats = new ArrayList<>();

        // generate random id for the ticket
        generateRandomID();

        // Generate random seats equal to the quantity of tickets
        Random random = new Random();
        for (int i = 0 ; i<this.quantity; i++){
            seats.add(random.nextInt(100)+1);
        }
    }

    public Ticket(JSONObject ticketJSON) throws JSONException {
        this.id = ticketJSON.getString("id");
        this.date = ticketJSON.getString("date");
        this.performance = ticketJSON.getString("performance");


        // Retrieving JSONArray and converting it back to ArrayList<Integer>
        JSONArray jsonSeats = new JSONArray(ticketJSON.getString("seats"));

        ArrayList<Integer> seats = new ArrayList<>();
        for (int i = 0; i < jsonSeats.length(); i++) {
            seats.add(Integer.parseInt(jsonSeats.getString(i))); // Convert JSON string back to ArrayList
        }

        this.seats = seats;
        Log.d("DEBUG","seats:" + seats);
    }

    private void generateRandomID(){
        String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        Log.d("DEBUG", sb.toString());
        this.id = sb.toString();
    }

    public ArrayList<Integer> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Integer> seats) {
        this.seats = seats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
