package com.example.mytheaterapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.WriterException;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatBotActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText userEditText;
    private ImageButton sendButton;
    private ScrollView scrollView;
    private Map<String, String> frames = new HashMap<>();
    private ArrayList<Button> initialOptionsButtons = new ArrayList<>();
    private LinearLayout greetingOptionsLayout;
    private ChatBot chat_bot;

    private int misunderstandingCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            this.chat_bot = new ChatBot(this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        frames.put("Performance", null);
        frames.put("Date", null);
        frames.put("time", null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        greetingOptionsLayout = findViewById(R.id.greetingOptionsLayout);
        chatContainer = findViewById(R.id.chat_container);
        userEditText = findViewById(R.id.userEditText);
        sendButton = findViewById(R.id.send_button);
        scrollView = findViewById(R.id.scroll_view);

        //Finding Initial Chat Buttons by Id (Greeting phase)
        Button infoButtonOption = findViewById(R.id.infoButtonOption);
        Button bookButtonOption = findViewById(R.id.bookButtonOption);
        Button supportButtonOption = findViewById(R.id.supportButtonOption);
        initialOptionsButtons.add(infoButtonOption);
        initialOptionsButtons.add(bookButtonOption);
        initialOptionsButtons.add(supportButtonOption);


        // greeting phase
        addMessage("🎭 Welcome how may i assist you?", false);

        // disable user input edit text and force him to use one of the existing options
        userEditText.setEnabled(false);



        infoButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleInitialOptionSelection("info", (String) infoButtonOption.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        bookButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleInitialOptionSelection("booking", (String) bookButtonOption.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        supportButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleInitialOptionSelection("support", (String) supportButtonOption.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = userEditText.getText().toString().trim();
                if (!input.isEmpty()) {
                    try {
                        handleUserInput(input);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    userEditText.setText("");
                }
            }
        });

    }
    private void handleUserInput(String input) throws JSONException {
        addMessage("🧑‍💬 " + input, true);

        // Get response from the ChatBot based on the user input
        String response = chat_bot.analyzeUserInput(input, this);

        HashMap <String, String> frames = chat_bot.getFrames();
        String currentIntentName = chat_bot.getCurrentIntentName();
        boolean isBookingFrameComplete = true;

        // Check if all the frames are completed
        if (currentIntentName != null && currentIntentName.equals("book_ticket")){
            for (String value : frames.values()){
                if (value == null){
                    isBookingFrameComplete = false;
                    break;
                }
            }
            // if all frames are complete and the booking frame is completed transfer client to payment environment
            if (isBookingFrameComplete){
                response = "You are being transfered to a safe enviroment to place your order based on the details you provided...";

                // Build Ticket object and pass it to the PaymentEnviroment activity

                try {
                    Ticket ticket = new Ticket(frames.get("performance"), frames.get("date"), frames.get("number_of_tickets"));
                    Intent intent = new Intent(ChatBotActivity.this, PaymentEnviroment.class);
                    intent.putExtra("ticket", ticket);
                    startActivity(intent);
                }
                catch (WriterException e){
                    Log.d("DEBUG", "Ticket Constructor WriterException!!");
                }
            }
        }

        addMessage(response, false);


    }


    private void handleInitialOptionSelection(String selection, String userText) throws JSONException {

        if (selection.equals("info")){
            handleUserInput(userText);
        } else if (selection.equals("booking")) {
            handleUserInput(userText);
        } else {
            addMessage(userText, true);
            String response = "I am dialing the theater's support desk for you...";
            addMessage(response,false);
        }

        userEditText.setEnabled(true);
        userEditText.setFocusable(true);
        greetingOptionsLayout.removeAllViews();

    }

    private void addMessage(String message, boolean isUser) {

        TextView textView = buildChatTextView(message, isUser);
        chatContainer.addView(textView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }



    private TextView buildChatTextView(String message, Boolean isUser){

        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setPadding(16, 8, 16, 8);


        // Set layout parameters with wrap_content
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,  // Width: wrap_content
                LinearLayout.LayoutParams.WRAP_CONTENT   // Height: wrap_content
        );

        params.setMargins(0,0,0,20);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(15);

        if (isUser) {
            // Set shape background from drawable
            Drawable shapeDrawable = ContextCompat.getDrawable(this, R.drawable.user_chat_shape);
            textView.setBackground(shapeDrawable);
            params.gravity= Gravity.END;
        } else {
            Drawable shapeDrawable = ContextCompat.getDrawable(this, R.drawable.bot_chat_shape);
            textView.setBackground(shapeDrawable);
            params.gravity= Gravity.START;
        }
        textView.setMaxWidth(700);
        textView.setLayoutParams(params);
        return textView;
    }

}




