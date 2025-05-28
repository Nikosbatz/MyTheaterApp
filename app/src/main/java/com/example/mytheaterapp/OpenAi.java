package com.example.mytheaterapp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class OpenAi{

    static private String secretKey = "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\n";

    public static Future<String> chatGPT(String prompt) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        return executor.submit(() -> {
            String url = "https://api.openai.com/v1/chat/completions";
            String apiKey = secretKey;
            String model = "gpt-3.5-turbo";

            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");

                String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                return extractMessageFromJSONResponse(response.toString());

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to call ChatGPT API", e);
            }
        });
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }

}