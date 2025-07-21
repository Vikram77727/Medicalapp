package com.example.medicalapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalapp.EditProfileActivity;
import com.example.medicalapp.R;
import com.example.medicalapp.DatabaseHelper;

import static android.content.Context.MODE_PRIVATE;

public class accountfragment extends Fragment {

    private TextView tvProfileName, tvPremiumStatus, tvAge, tvBlood, tvHeight, tvWeight, tvLastUpdate;
    private Button btnDownload, btnEdit;

    private DatabaseHelper dbHelper;
    private String loggedInEmail;

    public accountfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountfragment, container, false);

        // Initialize Views
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvPremiumStatus = view.findViewById(R.id.tvPremiumStatus);
        tvAge = view.findViewById(R.id.tvAge);
        tvBlood = view.findViewById(R.id.tvBlood);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnEdit = view.findViewById(R.id.btnEdit);

        // Get logged-in email from SharedPreferences
        SharedPreferences preferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInEmail = preferences.getString("LoggedInEmail", null);

        dbHelper = new DatabaseHelper(getContext());

        if (loggedInEmail != null && !loggedInEmail.isEmpty()) {
            loadUserData(loggedInEmail);
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        // Edit Button Logic
        btnEdit.setOnClickListener(v -> openEditActivity());

        return view;
    }

    private void loadUserData(String email) {
        Cursor cursor = dbHelper.getUserDetails(email); // Uses your existing method

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String age = cursor.getString(cursor.getColumnIndexOrThrow("age"));
            String blood = cursor.getString(cursor.getColumnIndexOrThrow("blood_group"));
            String height = cursor.getString(cursor.getColumnIndexOrThrow("height"));
            String weight = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
            String lastUpdate = cursor.getString(cursor.getColumnIndexOrThrow("last_update"));

            // Set views
            tvProfileName.setText(firstName + " " + lastName);
            tvAge.setText(age != null ? age : "");
            tvBlood.setText(blood != null ? blood : "");
            tvHeight.setText(height != null ? height : "");
            tvWeight.setText(weight != null ? weight : "");
            tvLastUpdate.setText("Last update: " + (lastUpdate != null ? lastUpdate : "N/A"));

            cursor.close();
        } else {
            Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEditActivity() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        // Passing current data to EditProfileActivity
        intent.putExtra("age", tvAge.getText().toString());
        intent.putExtra("blood", tvBlood.getText().toString());
        intent.putExtra("height", tvHeight.getText().toString());
        intent.putExtra("weight", tvWeight.getText().toString());
        startActivityForResult(intent, 1); // Request code = 1 for identifying the result
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            // Get the updated data from EditProfileActivity
            String updatedAge = data.getStringExtra("updatedAge");
            String updatedBlood = data.getStringExtra("updatedBlood");
            String updatedHeight = data.getStringExtra("updatedHeight");
            String updatedWeight = data.getStringExtra("updatedWeight");

            // Update the UI with the new data
            tvAge.setText(updatedAge);
            tvBlood.setText(updatedBlood);
            tvHeight.setText(updatedHeight);
            tvWeight.setText(updatedWeight);

            // Optionally update the database with the new information
            updateProfileInDatabase(updatedAge, updatedBlood, updatedHeight, updatedWeight);
        }
    }

    private void updateProfileInDatabase(String age, String blood, String height, String weight) {
        boolean isUpdated = dbHelper.updateUserProfile(loggedInEmail, age, blood, height, weight, "Updated on " + System.currentTimeMillis());

        if (isUpdated) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}