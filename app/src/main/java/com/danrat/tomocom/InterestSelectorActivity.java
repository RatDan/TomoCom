package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.danrat.tomocom.Model.Interests;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterestSelectorActivity extends AppCompatActivity {

    private int checkedCount = 0;
    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;
    private String userID;

    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selector);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

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
        CheckBox onlineCB = findViewById(R.id.onlineGamesCB);
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

        List<CheckBox> interests = new ArrayList<>();
        interests.add(0, dancingCB);
        interests.add(1, fashionCB);
        interests.add(2, showsCB);
        interests.add(3, travellingCB);
        interests.add(4, artCB);
        interests.add(5, gamesCB);
        interests.add(6, sportCB);
        interests.add(7, animalsCB);
        interests.add(8, cookingCB);
        interests.add(9, moviesCB);
        interests.add(10, animeCB);
        interests.add(11, mangaCB);
        interests.add(12, musicCB);
        interests.add(13, natureCB);
        interests.add(14, programmingCB);
        interests.add(15, onlineCB);
        interests.add(16, gymCB);
        interests.add(17, writingCB);
        interests.add(18, fitnessCB);
        interests.add(19, outdoorsCB);
        interests.add(20, concertsCB);
        interests.add(21, scienceCB);
        interests.add(22, booksCB);
        interests.add(23, campingCB);
        interests.add(24, boardGamesCB);
        interests.add(25, fishingCB);
        interests.add(26, hikingCB);

        if (!CollectionUtils.isEmpty(interests)) {
            for (final CheckBox checkbox : interests) {
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkedCount++;
                            if (checkedCount > 5) {
                                checkbox.setChecked(false);
                                checkedCount--;
                                Toast.makeText(InterestSelectorActivity.this, "Wybierz maksymalnie 5 zainteresowań.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            checkedCount--;
                        }
                    }
                });
            }
        }

        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Interests> selectedInterests = new ArrayList<>();
                userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                DocumentReference documentReference = fireStore.collection("users").document(userID);

                if (dancingCB.isChecked()) {
                    selectedInterests.add(Interests.Dancing);
                }
                if (fashionCB.isChecked()) {
                    selectedInterests.add(Interests.Fashion);
                }
                if (showsCB.isChecked()) {
                    selectedInterests.add(Interests.Shows);
                }
                if (travellingCB.isChecked()) {
                    selectedInterests.add(Interests.Traveling);
                }
                if (artCB.isChecked()) {
                    selectedInterests.add(Interests.Art);
                }
                if (gamesCB.isChecked()) {
                    selectedInterests.add(Interests.Games);
                }
                if (sportCB.isChecked()) {
                    selectedInterests.add(Interests.Sport);
                }
                if (animalsCB.isChecked()) {
                    selectedInterests.add(Interests.Animals);
                }
                if (cookingCB.isChecked()) {
                    selectedInterests.add(Interests.Cooking);
                }
                if (moviesCB.isChecked()) {
                    selectedInterests.add(Interests.Movies);
                }
                if (animeCB.isChecked()) {
                    selectedInterests.add(Interests.Anime);
                }
                if (mangaCB.isChecked()) {
                    selectedInterests.add(Interests.Manga);
                }
                if (musicCB.isChecked()) {
                    selectedInterests.add(Interests.Music);
                }
                if (programmingCB.isChecked()) {
                    selectedInterests.add(Interests.Programming);
                }
                if (natureCB.isChecked()) {
                    selectedInterests.add(Interests.Nature);
                }
                if (onlineCB.isChecked()) {
                    selectedInterests.add(Interests.Online_Games);
                }
                if (gymCB.isChecked()) {
                    selectedInterests.add(Interests.Gym);
                }
                if (writingCB.isChecked()) {
                    selectedInterests.add(Interests.Writing);
                }
                if (fitnessCB.isChecked()) {
                    selectedInterests.add(Interests.Fitness);
                }
                if (outdoorsCB.isChecked()) {
                    selectedInterests.add(Interests.Outside_Activities);
                }
                if (concertsCB.isChecked()) {
                    selectedInterests.add(Interests.Concerts);
                }
                if (scienceCB.isChecked()) {
                    selectedInterests.add(Interests.Science);
                }
                if (booksCB.isChecked()) {
                    selectedInterests.add(Interests.Books);
                }
                if (campingCB.isChecked()) {
                    selectedInterests.add(Interests.Camping);
                }
                if (boardGamesCB.isChecked()) {
                    selectedInterests.add(Interests.Board_Games);
                }
                if (fishingCB.isChecked()) {
                    selectedInterests.add(Interests.Fishing);
                }
                if (hikingCB.isChecked()) {
                    selectedInterests.add(Interests.Hiking);
                }

                documentReference
                        .update("interests", selectedInterests)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"onSuccess: Document "+ userID +" updated succesfully");
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "onFailure: Error updating "+ userID +" document", e);
                            }
                        });

            }
        });



    }
}