package com.example.mytheaterapp;

import android.animation.LayoutTransition;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private ImageView lastClickedImage;

    // Empty constructor
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView playInfoText = view.findViewById(R.id.play_info);
        ImageView hamletImg = view.findViewById(R.id.hamlet_img);
        ImageView deathImg = view.findViewById(R.id.death_img);
        Boolean hamletZoomed = false;
        Boolean deathZoomed = false;



        LinearLayout linearContainer = view.findViewById(R.id.playing_now_layout);
        // Enable layout animation
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        linearContainer.setLayoutTransition(layoutTransition);


        hamletImg.setOnClickListener(v -> {
            v.animate()
                    .scaleX(1.11f)
                    .scaleY(1.11f)
                    .setDuration(300)
                    .start();

            v.postDelayed(() -> {
                v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .start();
            }, 300);

            if (lastClickedImage == hamletImg){
                playInfoText.setText("");
                lastClickedImage = null;
            }
            else {
                lastClickedImage = hamletImg;
                playInfoText.setText("\uD83C\uDFAD Hamlet: A Modern Theater Remake\n\n" +
                        "This contemporary remake of Hamlet reimagines Shakespeare’s classic in a stark, modern world ruled by " +
                        "surveillance and suspicion. With minimalist staging, digital projections, and a haunting score, the play " +
                        "explores themes of madness, revenge, and identity in a fractured society. Familiar lines are delivered with " +
                        "fresh urgency, making the tragedy feel strikingly relevant today. ");
            }



        });

        deathImg.setOnClickListener(v -> {
            v.animate()
                    .scaleX(1.11f)
                    .scaleY(1.11f)
                    .setDuration(300)
                    .start();

            v.postDelayed(() -> {
                v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(300)
                        .start();
            }, 300);

            if (lastClickedImage == deathImg){
                playInfoText.setText("");
                lastClickedImage = null;
            }
            else {
                lastClickedImage = deathImg;
                playInfoText.setText("\uD83C\uDFAD Death of a Salesman: A Contemporary Remake\n\n" +
                        "In this powerful reimagining of Arthur Miller’s classic, Death of a Salesman is set" +
                        " in today’s world of economic precarity and fading dreams. With stripped-down staging and modern visuals, Willy " +
                        "Loman’s struggle against failure and illusion becomes a haunting reflection of" +
                        " the pressures of modern success and self-worth.");
            }



        });


        return view;
    }

}
