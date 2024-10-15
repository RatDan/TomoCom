package com.danrat.tomocom.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.DescriptionViewModel;

public class DescriptionActivity extends AppCompatActivity {

    DescriptionViewModel descriptionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        descriptionViewModel = new ViewModelProvider(this).get(DescriptionViewModel.class);
        Button nextButton = findViewById(R.id.nextButton);
        EditText descriptionEditText = findViewById(R.id.descriptionET);

        nextButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString().trim();
            descriptionViewModel.updateDescription(description);
            handleActivity();
        });
    }

    public void handleActivity () {
        if (getIntent() != null)
            finish();
        else
            startActivity(new Intent(getApplicationContext(), InterestSelectorActivity.class));
    }
}