package com.example.mytheaterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Ticket implements Serializable {
    private String performance;
    private String id;
    private String date;
    private ArrayList<Integer> seats;
    private boolean isActive;
    private int quantity;
    private String base64QRcode;



    public Ticket(String performance, String date, String quantity) throws WriterException {
        this.performance = performance;
        this.date = date;
        this.quantity = Integer.parseInt(quantity);
        isActive = false;
        seats = new ArrayList<>();

        // Generate random id for the ticket
        generateRandomID();

        // Generate QR code
        generateQRcode();

        // Generate random seats equal to the quantity of tickets
        Random random = new Random();
        for (int i = 0; i < this.quantity; i++) {
            seats.add(random.nextInt(100) + 1);
        }
    }

    public Ticket(JSONObject ticketJSON) throws JSONException, WriterException {
        this.id = ticketJSON.getString("id");
        this.date = ticketJSON.getString("date");
        this.performance = ticketJSON.getString("performance");
        this.base64QRcode = ticketJSON.getString("qrcode");


        // Retrieving JSONArray and converting it back to ArrayList<Integer>
        JSONArray jsonSeats = new JSONArray(ticketJSON.getString("seats"));

        ArrayList<Integer> seats = new ArrayList<>();
        for (int i = 0; i < jsonSeats.length(); i++) {
            seats.add(Integer.parseInt(jsonSeats.getString(i))); // Convert JSON string back to ArrayList
        }

        this.seats = seats;
        Log.d("DEBUG", "seats:" + seats);
    }

    private void generateRandomID() {
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

    private void generateQRcode() throws WriterException {

        BitMatrix matrix = new MultiFormatWriter().encode(this.id, BarcodeFormat.QR_CODE, 600, 300);
        Bitmap bitmap = Bitmap.createBitmap(600, 300, Bitmap.Config.RGB_565);

        for (int x = 0; x < 600; x++) {
            for (int y = 0; y < 300; y++) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Log.d("DEBUG", Arrays.toString(stream.toByteArray()));
        byte[] QRdata = stream.toByteArray();
        this.base64QRcode = Base64.encodeToString(QRdata, Base64.NO_WRAP);
    }

    public Bitmap getQRcodeBitMap(){
        byte[] decodedBytes = Base64.decode(this.base64QRcode, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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

    public String getBase64QRcode() {
        return base64QRcode;
    }
}
