package com.danrat.tomocom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.danrat.tomocom.Model.Interests;
import com.danrat.tomocom.ViewModel.InterestsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class InterestSelectorActivity extends AppCompatActivity {

    private InterestsViewModel interestsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selector);

        interestsViewModel = new ViewModelProvider(this).get(InterestsViewModel.class);
        Button nextButton = findViewById(R.id.nextButton);

        CheckBox dancingCB = findViewById(R.id.dancingCB);
        CheckBox fashionCB = findViewById(R.id.fashionCB);
        CheckBox showsCB = findViewById(R.id.showsCB);
        CheckBox travellingCB = findViewById(R.id.travellingCB);
        CheckBox artCB = findViewById(R.id.artCB);
        CheckBox gamesCB = findViewById(R.id.gamesCB);
        CheckBox sportCB = findViewById(R.id.sportCB);
        CheckBox animalsCB = findViewById(R.id.animalsCB);
        CheckBox cookingCB = findViewById(R.id.cookingCB);
        CheckBox moviesCB = findViewById(R.id.moviesCB);
        CheckBox animeCB = findViewById(R.id.animeCB);
        CheckBox mangaCB = findViewById(R.id.mangaCB);
        CheckBox musicCB = findViewById(R.id.musicCB);
        CheckBox programmingCB = findViewById(R.id.programmingCB);
        CheckBox natureCB = findViewById(R.id.natureCB);
        CheckBox onlineGamesCB = findViewById(R.id.onlineGamesCB);
        CheckBox gymCB = findViewById(R.id.gymCB);
        CheckBox writingCB = findViewById(R.id.writingCB);
        CheckBox fitnessCB = findViewById(R.id.fitnessCB);
        CheckBox outdoorsCB = findViewById(R.id.outdoorsCB);
        CheckBox concertsCB = findViewById(R.id.concertsCB);
        CheckBox scienceCB = findViewById(R.id.scienceCB);
        CheckBox booksCB = findViewById(R.id.booksCB);
        CheckBox campingCB = findViewById(R.id.campingCB);
        CheckBox boardGamesCB = findViewById(R.id.boardGamesCB);
        CheckBox fishingCB = findViewById(R.id.fishingCB);
        CheckBox hikingCB = findViewById(R.id.hikingCB);

        List<CheckBox> checkBoxes = Arrays.asList(
                dancingCB, fashionCB, showsCB, travellingCB, artCB, gamesCB, sportCB,
                animalsCB, cookingCB, moviesCB, animeCB, mangaCB, musicCB, programmingCB,
                natureCB, onlineGamesCB, gymCB, writingCB, fitnessCB, outdoorsCB,
                concertsCB, scienceCB, booksCB, campingCB, boardGamesCB, fishingCB, hikingCB
        );

        List<Interests> interestsList = Arrays.asList(
                Interests.Dancing, Interests.Fashion, Interests.Shows, Interests.Traveling, Interests.Art,
                Interests.Games, Interests.Sport, Interests.Animals, Interests.Cooking, Interests.Movies,
                Interests.Anime, Interests.Manga, Interests.Music, Interests.Programming, Interests.Nature,
                Interests.Online_Games, Interests.Gym, Interests.Writing, Interests.Fitness, Interests.Outside_Activities,
                Interests.Concerts, Interests.Science, Interests.Books, Interests.Camping, Interests.Board_Games,
                Interests.Fishing, Interests.Hiking
        );

        interestsViewModel.canSelectMoreInterests().observe(this, canSelectMore -> {
            for (CheckBox checkBox : checkBoxes) {
                if (!checkBox.isChecked()) {
                    checkBox.setEnabled(canSelectMore);
                }
            }
        });

        interestsViewModel.isButtonEnabled().observe(this, nextButton::setEnabled);


        for (int i = 0; i < checkBoxes.size(); i++) {
            CheckBox checkBox = checkBoxes.get(i);
            Interests interest = interestsList.get(i);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> interestsViewModel.toggleInterest(interest, isChecked));
        }

        nextButton.setOnClickListener(v -> {
            List<Interests> selectedInterests = interestsViewModel.getSelectedInterests().getValue();

            if (selectedInterests != null && selectedInterests.size() < 3) {
                Toast.makeText(InterestSelectorActivity.this, "Musisz wybrać minimum 3 zainteresowania", Toast.LENGTH_SHORT).show();
            } else {
                interestsViewModel.updateInterests(unused -> {
                    Log.d("InterestSelector", "Zaktualizowano pomyślnie");
                    handleActivity();
                }, e -> Log.w("InterestSelector", "Błąd przy aktualizacji", e));
            }
        });
    }

    public void handleActivity () {
        if (getIntent() != null)
            finish();
        else
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}