package com.example.medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etAge, etBlood, etHeight, etWeight;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etAge = findViewById(R.id.etAge);
        etBlood = findViewById(R.id.etBlood);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        btnSave = findViewById(R.id.btnSave);

        // Get the current data passed from the account fragment
        Intent intent = getIntent();
        etAge.setText(intent.getStringExtra("age"));
        etBlood.setText(intent.getStringExtra("blood"));
        etHeight.setText(intent.getStringExtra("height"));
        etWeight.setText(intent.getStringExtra("weight"));

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String updatedAge = etAge.getText().toString();
        String updatedBlood = etBlood.getText().toString();
        String updatedHeight = etHeight.getText().toString();
        String updatedWeight = etWeight.getText().toString();

        // Send back the updated data
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedAge", updatedAge);
        resultIntent.putExtra("updatedBlood", updatedBlood);
        resultIntent.putExtra("updatedHeight", updatedHeight);
        resultIntent.putExtra("updatedWeight", updatedWeight);

        setResult(RESULT_OK, resultIntent);
        finish(); // Close the activity and return to the previous one
    }
}
