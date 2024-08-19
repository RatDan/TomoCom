package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Arrays;
import java.util.List;

public class InterestSelectorActivity extends AppCompatActivity {

    private int checkedCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selector);

        Intent intent = getIntent();
        ImageButton returnButton = (ImageButton)findViewById(R.id.returnButton);
        if (intent.hasExtra("panelData"))
        {
            returnButton.setVisibility(View.VISIBLE);
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        CheckBox[] checkboxy = new CheckBox[27];
        checkboxy[0]=findViewById(R.id.dancingCB);
        checkboxy[1]=findViewById(R.id.fashionCB);
        checkboxy[2]=findViewById(R.id.showsCB);
        checkboxy[3]=findViewById(R.id.travellingCB);
        checkboxy[4]=findViewById(R.id.artCB);
        checkboxy[5]=findViewById(R.id.gamesCB);
        checkboxy[6]=findViewById(R.id.sportCB);
        checkboxy[7]=findViewById(R.id.animalsCB);
        checkboxy[8]=findViewById(R.id.cookingCB);
        checkboxy[9]=findViewById(R.id.moviesCB);
        checkboxy[10]=findViewById(R.id.animeCB);
        checkboxy[11]=findViewById(R.id.mangaCB);
        checkboxy[12]=findViewById(R.id.musicCB);
        checkboxy[13]=findViewById(R.id.natureCB);
        checkboxy[14]=findViewById(R.id.programmingCB);
        checkboxy[15]=findViewById(R.id.onlineGamesCB);
        checkboxy[16]=findViewById(R.id.gymCB);
        checkboxy[17]=findViewById(R.id.writingCB);
        checkboxy[18]=findViewById(R.id.fitnessCB);
        checkboxy[19]=findViewById(R.id.outdoorsCB);
        checkboxy[20]=findViewById(R.id.concertsCB);
        checkboxy[21]=findViewById(R.id.scienceCB);
        checkboxy[22]=findViewById(R.id.booksCB);
        checkboxy[23]=findViewById(R.id.campingCB);
        checkboxy[24]=findViewById(R.id.boardGamesCB);
        checkboxy[25]=findViewById(R.id.fishingCB);
        checkboxy[26]=findViewById(R.id.hikingCB);

        for (final CheckBox checkbox : checkboxy) {
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedCount++;
                        if (checkedCount > 5) {
                            checkbox.setChecked(false);
                            checkedCount--;
                            Toast.makeText(InterestSelectorActivity.this, "Możesz zaznaczyć maksymalnie 5 zainteresowań.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        checkedCount--;
                    }
                }
            });
        }

        Button dalejPrzycisk = (Button)findViewById(R.id.nextButton);

        dalejPrzycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<5;i++)
                {

                }
            }
        });



    }
}