package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        Button findPeopleButton = (Button)findViewById(R.id.findPeopleButton);
        findPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PanelActivity.this,MatchedPeopleActivity.class));
            }
        });

        Button chatsButton = (Button)findViewById(R.id.chatsButton);

        Button changeInterestsButton = (Button)findViewById(R.id.changeInterestsButton);
        Intent intentFromPanel = new Intent(PanelActivity.this, InterestSelectorActivity.class);
        intentFromPanel.putExtra("panelData", "1");
        changeInterestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentFromPanel);
            }
        });

        Button logoutButton = (Button)findViewById(R.id.logoutButton);

    }


}