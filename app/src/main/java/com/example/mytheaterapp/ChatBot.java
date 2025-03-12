package com.example.mytheaterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatBot extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText userInput;
    private Button sendButton;
    private ScrollView scrollView;

    private int misunderstandingCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatContainer = findViewById(R.id.chat_container);
        userInput = findViewById(R.id.user_input);
        sendButton = findViewById(R.id.send_button);
        scrollView = findViewById(R.id.scroll_view);

        // greeting
        addMessage("🎭 Καλώς ήρθατε στο θέατρο! Πώς μπορώ να σας βοηθήσω;", false);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = userInput.getText().toString().trim();
                if (!input.isEmpty()) {
                    handleUserInput(input);
                    userInput.setText("");
                }
            }
        });
    }
    private void handleUserInput(String input) {
        addMessage("🧑‍💬 " + input, true);

        String response;

        if (input.toLowerCase().contains("πληροφορίες") || input.toLowerCase().contains("παράσταση")) {
            response = "🎭 Παίζουμε 'skibidi' και 'toilet'. Ποια παράσταση σας ενδιαφέρει;";
            misunderstandingCount = 0;
        } else if (input.toLowerCase().contains("οθέλλος")) {
            response = "🕒 Θέλετε απογευματινή (18:00) ή βραδινή (21:00) παράσταση;";
            misunderstandingCount = 0;
        } else if (input.toLowerCase().contains("βραδινή")) {
            response = "🎟️ Πόσα εισιτήρια θέλετε;";
            misunderstandingCount = 0;
        } else if (input.matches("\\d+")) {
            response = "✅ Επιβεβαιώνετε την κράτηση για " + input + " εισιτήρια για τη βραδινή παράσταση 'skibidi';";
            misunderstandingCount = 0;
        } else if (input.toLowerCase().contains("ναι")) {
            response = "🎉 Η κράτησή σας ολοκληρώθηκε! Καλή διασκέδαση!";
            misunderstandingCount = 0;
        } else {
            misunderstandingCount++;
            if (misunderstandingCount >= 3) {
                response = "❗ Φαίνεται ότι δεν καταλαβαίνω. Θέλετε να μιλήσετε με έναν εκπρόσωπο;";
                misunderstandingCount = 0;
            } else {
                response = "❓ Συγγνώμη, δεν καταλαβαίνω. Μπορείτε να το διατυπώσετε διαφορετικά;";
            }
        }

        addMessage(response, false);
    }

    private void addMessage(String message, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setPadding(16, 8, 16, 8);

        if (isUser) {
            textView.setBackgroundColor(0xFFB3E5FC);
        } else {
            textView.setBackgroundColor(0xFFE0E0E0);
        }

        chatContainer.addView(textView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}


