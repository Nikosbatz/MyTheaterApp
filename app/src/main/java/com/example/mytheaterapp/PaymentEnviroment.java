package com.example.mytheaterapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.vinaygaba.creditcardview.CreditCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class PaymentEnviroment extends AppCompatActivity {

    EditText card_number;
    EditText card_holder_name;
    EditText cvc;
    EditText exp_date;
    Button pay_button;
    ProgressBar progress_bar;
    CreditCardView card;
    Ticket ticket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_enviroment);

        // Get Ticket info from the ChatBotActivity
        ticket = (Ticket) getIntent().getSerializableExtra("ticket");
        Toast.makeText(this, ticket.getId(), Toast.LENGTH_SHORT).show();

        // Initialize Views
        card = findViewById(R.id.card);
        card_number = findViewById(R.id.cardNumber);
        card_holder_name = findViewById(R.id.cardHolderName);
        cvc = findViewById(R.id.cvc);
        exp_date = findViewById(R.id.expDate);
        pay_button = findViewById(R.id.payButton);
        progress_bar = findViewById(R.id.progressBar);


        // Change CARD NUMBER field Dynamically
        card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setCardNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//---------------------

        // Change EXPIRY DATE field Dynamically and format into MM/YY
        exp_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = exp_date.getText().toString();

                // Format the string to MM/YY

                if (input.length()>2) {
                    card.setExpiryDate(input.substring(0, 2) + "/" + input.substring(2,input.length()));
                } else {
                    card.setExpiryDate(input +"/");
                }


                // Move the cursor to the end
                exp_date.setSelection(input.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//---------------------


        // Change CARD HOLDER NAME field dynamically
        card_holder_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setCardName(card_holder_name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//---------------------

        // Complete Payment Action
        pay_button.setOnClickListener(v -> {
            if (card_number.getText().toString().length() != 16){
                card_number.setError("Card Number must consist of 16 numbers!");
            }
            else {
                progress_bar.setVisibility(View.VISIBLE);
                hideKeyboard(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveTicket();
                        startActivity(new Intent(PaymentEnviroment.this, MainActivity.class));
                        finish();
                    }
                },2500);

            }
        });//---------------------


    }


    public void saveTicket(){
        try (FileOutputStream fos = openFileOutput("ticket"+ticket.getId()+".json", Context.MODE_PRIVATE)) {
            JSONObject ticketJson = new JSONObject();
            ticketJson.put("id", ticket.getId());
            ticketJson.put("performance", ticket.getPerformance());
            ticketJson.put("date", ticket.getId());
            ticketJson.put("seats", ticket.getSeats());
            fos.write(ticketJson.toString().getBytes());
            Toast.makeText(this, "Ticket saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
