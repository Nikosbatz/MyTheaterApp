package com.example.mytheaterapp;

import java.util.regex.*;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChatBot {

    private ArrayList<Intent> intents = new ArrayList<>();
    private HashMap<String,String> frames = new HashMap<>();
    private HashMap<String, String> performanceDetails = new HashMap<>();
    private Intent currentIntent;

    public ChatBot(Context context) throws JSONException {

        performanceDetails.put("hamlet", "Hamlet: A Modern Theater Remake\n" +
                "This contemporary remake of Hamlet reimagines Shakespeare’s classic in a stark, modern world ruled by surveillance and " +
                "suspicion. With minimalist staging, digital projections, and a haunting score, the play explores themes of madness," +
                " revenge, and identity in a fractured society. Familiar lines are delivered with fresh urgency, making the tragedy " +
                "feel strikingly relevant today.");

        performanceDetails.put("death", "Death of a Salesman: A Contemporary Remake\n" +
                "In this powerful reimagining of Arthur Miller’s classic, Death of a Salesman is set in today’s world of economic" +
                " precarity and fading dreams. With stripped-down staging and modern visuals, Willy Loman’s struggle against" +
                " failure and illusion becomes a haunting reflection of the pressures of modern success and self-worth.");

        Intent book_ticket = new Intent("book_ticket");
        Intent cancel_ticket = new Intent("cancel_ticket");
        Intent request_info = new Intent("request_info");
        Intent request_support = new Intent("request_support");

        intents.add(book_ticket);
        intents.add(cancel_ticket);
        intents.add(request_info);
        intents.add(request_support);

        // Load .json file from the "/app/src/main/assets/" dir
        String jsonString = loadJsonFromAssets(context, "intents.json");

        // Parsing the JSON content
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray intentsJSON = jsonObject.getJSONArray("intents");

        // iterate through the intents of the .json
        for (int i = 0; i < intentsJSON.length(); i++) {
            JSONObject intent = intentsJSON.getJSONObject(i);
            String intentName = intent.getString("name");
            // add the wordlist of the current intent to the corresponding Intent object
            if (intent.has("wordlist")){
                JSONArray wordlist = intent.getJSONArray("wordlist");
                for (Intent in: intents){
                    if (in.getName().equals(intentName)){
                        Log.d("DEBUG", wordlist.toString());
                        in.setIntentWordlist(wordlist);
                        break;
                    }
                }
            }
            //---------------------------------

            // iterate through entities of the intent and add them to the Intent object
            if (intent.has("entities")){
                JSONArray entities = intent.getJSONArray("entities");
                for (int j = 0; j < entities.length(); j++) {
                    JSONObject entity = entities.getJSONObject(j);
                    String entityName = entity.getString("name");
                    JSONArray wordlist = entity.getJSONArray("wordlist");

                    // Ensures that this entity belongs to the current intent (In case of duplicate entities between intents)
                    for (Intent in: intents){
                        if (in.getName().equals(intentName)){
                            Log.d("DEBUG", wordlist.toString());
                            in.addEntity(entityName, wordlist);
                            break;
                        }
                    }
                }
            }
            //---------------------------------

        }
    }

    public String getCurrentIntentName() {
        if (currentIntent != null){
            return currentIntent.getName();
        }
        else {return null;}
    }

    public HashMap<String, String> getFrames() {
        return frames;
    }

    public String analyzeUserInput(String input, Context context) throws JSONException {

        String response = null ;
        input = input.toLowerCase();

        ArrayList<String> wordlist = new ArrayList<>();

        /* Analyze the input and find the Intent of the user */
        for (Intent intent: intents){
            for (String word: intent.getWordlist()){
                // if new intent is detected
                if (input.contains(word) & this.currentIntent != intent){
                    this.currentIntent = intent;
                    if (currentIntent.getName().equals("request_support")){
                        response = "Forwarding our conversation to a human. Please wait...";
                        break;
                    }
                    // Initialize new frames if intent changed
                    for (String key: currentIntent.getEntities().keySet() ){
                        frames.put(key, null);
                    }
                    break;
                }
            }
        }
        if (currentIntent == null){
            return "I don't understand would you like details about a performance or book a ticket?";
        }

        //TODO Request support doesn't have "entities" key
        for (String key : currentIntent.getEntities().keySet()){
            for (String word:currentIntent.getEntities().get(key)){
                if (input.matches(".*\\b" + word + "\\b.*")){
                    Log.d("DEBUG", key+word);
                    frames.put(key, word);
                }
            }
            // Check if the input contains a booking date (dd/mm/yy/)
            if (key.equals("date")){
                String regex = "\\b\\d{2}/\\d{2}/\\d{2}\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input);
                String dateString = "";
                if(matcher.find()) {

                    dateString = matcher.group();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                    LocalDate date = null;
                    try {
                        date = LocalDate.parse(dateString, formatter);
                    }
                    catch (java.time.format.DateTimeParseException e){
                        return "Invalid Date!\nPlease try a different one (dd/mm/yy)";
                    }

                    LocalDate currentDate = LocalDate.now();

                    if(date.isAfter(currentDate) || date.equals(currentDate)){
                        frames.put(key,dateString);
                    }
                    else {
                        return "The date for the ticket must be equal to "+currentDate.toString()+" or later...";
                    }
                }
            }
        }

        // If the intention is to cancel ticket and the input is 5 characters long then check if there
        // is an existing ticket with this ID and delete it
        if (currentIntent.getName().equals("cancel_ticket") && input.length() == 5){
            ArrayList<Ticket> tickets = MyTicketsFragment.loadTickets(context);
            for (Ticket ticket: tickets){
                if (ticket.getId().equals(input) ){
                    File dir = context.getFilesDir(); // Get internal storage directory
                    for (File file: Objects.requireNonNull(dir.listFiles())){
                        if (file.getName().equals("ticket"+input+".json")){
                            frames.put("ticket_id", input);
                            file.delete();
                        }
                    }
                }
            }
        }

        for (String entity : frames.keySet()){
            if (frames.get(entity) == null && entity.equals("performance") && currentIntent.getName().equals("book_ticket")){
                response = "For which performance would you like to make the booking?\n1. Hamlet\n2. Death of a Salesman";
            }
            else if(frames.get(entity) == null && entity.equals("performance") && currentIntent.getName().equals("request_info")){
                response = "Currently there are 2 performances playing in our Theater:\n1. Hamlet by William Shakespeare\n" +
                "2. Death of a Salesman by Arthur Miller\nFor which one would you like to learn more?";
            }
            else if(frames.get(entity) != null && entity.equals("performance") && currentIntent.getName().equals("request_info")){
                if (frames.get(entity).contains("hamlet")){
                    response = performanceDetails.get("hamlet");
                }
                else if(frames.get(entity).contains("death") || frames.get(entity).contains("salesman")) {
                    response = performanceDetails.get("death");
                }

            }
            else if(frames.get(entity) == null && entity.equals("date")){
                response = "Please specify the date (dd/mm/yy) and time (afternoon or night) of performance you would like";
            }
            else if(frames.get(entity) == null && entity.equals("number_of_tickets")){
                response = "Please specify the number of tickets you would like to book";
            }
            else if(frames.get(entity) == null && entity.equals("ticket_id")){
                response = "Please specify the ticket ID of the ticket you would like to cancel\n(The ticket id is a 5 character word on your ticket)";
            }
            else if (frames.get(entity) != null && entity.equals("ticket_id")){
                response = "Cancelling your reservation with id "+ input +" ... Hope to see you next time!";
            }
        }

        //TODO frames implementation and corresponding answers based on the frames missing




        return response;
    }




    class Intent {
        private HashMap<String, ArrayList<String>> entities = new HashMap<>();
        private ArrayList<String> wordlist = new ArrayList<>();
        private String name;

        public Intent(String name){
            this.name = name;
        }

        public void addEntity(String entityName, JSONArray wordlistJSON) throws JSONException {

            ArrayList<String> wordlist = new ArrayList<>();
            for (int k=0;k<wordlistJSON.length();k++){
                wordlist.add(wordlistJSON.getString(k));
            }
            entities.put(entityName, wordlist);
        }


        public String getEntity(String entityName){
            return entities.get(entityName).toString(); // maybe useless
        }

        public HashMap<String, ArrayList<String>> getEntities(){
            return this.entities;
        }

        public void setIntentWordlist(JSONArray wordlistJSON) throws JSONException {
            for (int k=0;k<wordlistJSON.length();k++){
                //Log.d("DEBUG", wordlistJSON.getString(k));
                this.wordlist.add(wordlistJSON.getString(k));
            }
        }

        public String getName(){
            return this.name;
        }

        public ArrayList<String> getWordlist(){
            return this.wordlist;
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
