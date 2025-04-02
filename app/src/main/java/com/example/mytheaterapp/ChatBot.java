package com.example.mytheaterapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatBot {

    private ArrayList<Intent> intents = new ArrayList<>();
    private HashMap<String,String> frames = new HashMap<>();
    private HashMap<String, String> performanceDetails = new HashMap<>();
    private Intent currentIntent;

    public ChatBot(Context context) throws JSONException {

        performanceDetails.put("performance1", "Performance1 details following...");
        performanceDetails.put("performance2", "Performance2 details following...");
        String filePath = "intents.json"; // path to .json
        Intent book_ticket = new Intent("book_ticket");
        Intent cancel_ticket = new Intent("cancel_ticket");
        Intent request_info = new Intent("request_info");
        Intent request_support = new Intent("request_support");

        intents.add(book_ticket);
        intents.add(cancel_ticket);
        intents.add(request_info);
        intents.add(request_support);

        // Load .json file from the "/app/src/main/assets/" dir
        String jsonString = loadJsonFromAssets(context, filePath);

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

    public String analyzeUserInput(String input){

        String response = null ;
        input = input.toLowerCase();

        ArrayList<String> wordlist = new ArrayList<>();

        /* Analyze the input and find the Intent of the user */
        for (Intent intent: intents){
            for (String word: intent.getWordlist()){
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
        }

        for (String entity : frames.keySet()){
            if (frames.get(entity) == null && entity.equals("performance") && currentIntent.getName().equals("book_ticket")){
                response = "For which performance would you like to make the booking?";
            }
            else if(frames.get(entity) == null && entity.equals("performance") && currentIntent.getName().equals("request_info")){
                response = "Please mention the performance for which you would like to get informed...";
            }
            else if(frames.get(entity) != null && entity.equals("performance") && currentIntent.getName().equals("request_info")){
                response = performanceDetails.get(frames.get(entity));
            }
            else if(frames.get(entity) == null && entity.equals("date")){
                response = "Please mention the date and time of performance you would like";
            }
            else if(frames.get(entity) == null && entity.equals("number_of_tickets")){
                response = "Please specify the number of tickets you would like to book";
            }
            else if(frames.get(entity) == null && entity.equals("ticket_id")){
                response = "Please specify the ticket ID of the ticket you would like to cancel";
            }
            else if (frames.get(entity) != null && entity.equals("ticket_id")){
                response = "Cancelling your reservation... Hope to see you next time!";
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
