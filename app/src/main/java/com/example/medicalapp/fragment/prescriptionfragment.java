package com.example.medicalapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.medicalapp.MedicineAdapter;
import com.example.medicalapp.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class prescriptionfragment extends Fragment {

    private Spinner spinnerSymptoms;
    private EditText etTimeDuration, etMedicationDetails;
    private RadioGroup rgDailyRoutine, rgExerciseFrequency, rgSleepPattern;
    private Button btnGeneratePrescription;
    private ListView lvMedicines;
    private TextView tvRecommendedMedicines;

    private SharedPreferences preferences;
    private int age;
    private String gender;

    public prescriptionfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescriptionfragment, container, false);

        // Initialize Views
        spinnerSymptoms = view.findViewById(R.id.spinnerSymptoms);
        etTimeDuration = view.findViewById(R.id.etTimeDuration);
        etMedicationDetails = view.findViewById(R.id.etMedicationDetails);
        rgDailyRoutine = view.findViewById(R.id.rgDailyRoutine);
        rgExerciseFrequency = view.findViewById(R.id.rgExerciseFrequency);
        rgSleepPattern = view.findViewById(R.id.rgSleepPattern);
        btnGeneratePrescription = view.findViewById(R.id.btnGeneratePrescription);
        lvMedicines = view.findViewById(R.id.lvMedicines);
        tvRecommendedMedicines = view.findViewById(R.id.tvRecommendedMedicines);

        // Get user details from SharedPreferences (same as account fragment)
        preferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        age = preferences.getInt("UserAge", 0);
        gender = preferences.getString("UserGender", "");

        // Set up the symptoms spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.symptom_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSymptoms.setAdapter(adapter);

        // Set the listener for the "Generate Prescription" button
        btnGeneratePrescription.setOnClickListener(v -> generatePrescription());

        return view;
    }

    // Method to generate prescription based on the selected symptoms and time duration
    private void generatePrescription() {
        // Capture the user inputs
        String selectedSymptom = spinnerSymptoms.getSelectedItem().toString();
        String timeDuration = etTimeDuration.getText().toString();

        // Validate the input
        if (timeDuration.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Logic for recommending medicines based on symptoms
        ArrayList<String> recommendedMedicines = new ArrayList<>();

        // Add medicines based on the selected symptom
        if (selectedSymptom.equals("Fever")) {
            recommendedMedicines.add("Paracetamol - 500mg");
        } else if (selectedSymptom.equals("Cough")) {
            recommendedMedicines.add("Cough Syrup - 5ml twice a day");
        } else if (selectedSymptom.equals("Headache")) {
            recommendedMedicines.add("Ibuprofen - 200mg");
        } else if (selectedSymptom.equals("Sore Throat")) {
            recommendedMedicines.add("Throat Lozenges - 2 tablets a day");
        }

        // Handle additional questions (Daily Routine, Exercise, Sleep, Medication)
        int dailyRoutineSelectedId = rgDailyRoutine.getCheckedRadioButtonId();
        RadioButton selectedDailyRoutine = getView().findViewById(dailyRoutineSelectedId);

        int exerciseFrequencySelectedId = rgExerciseFrequency.getCheckedRadioButtonId();
        RadioButton selectedExerciseFrequency = getView().findViewById(exerciseFrequencySelectedId);

        int sleepPatternSelectedId = rgSleepPattern.getCheckedRadioButtonId();
        RadioButton selectedSleepPattern = getView().findViewById(sleepPatternSelectedId);

        String medicationDetails = etMedicationDetails.getText().toString();

        // Optional logic based on routine and sleep pattern (add more logic if needed)
        if (selectedDailyRoutine != null) {
            String dailyRoutine = selectedDailyRoutine.getText().toString();
            recommendedMedicines.add("Suggested Daily Routine: " + dailyRoutine);
        }

        if (selectedExerciseFrequency != null) {
            String exerciseFrequency = selectedExerciseFrequency.getText().toString();
            recommendedMedicines.add("Exercise Frequency: " + exerciseFrequency);
        }

        if (selectedSleepPattern != null) {
            String sleepPattern = selectedSleepPattern.getText().toString();
            recommendedMedicines.add("Sleep Pattern: " + sleepPattern);
        }

        if (!medicationDetails.isEmpty()) {
            recommendedMedicines.add("Medication Details: " + medicationDetails);
        }

        // Display the recommended medicines
        if (recommendedMedicines.size() > 0) {
            // Set the adapter for ListView to show medicines
            MedicineAdapter adapter = new MedicineAdapter(getContext(), recommendedMedicines);
            lvMedicines.setAdapter(adapter);
            tvRecommendedMedicines.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), "No symptoms selected", Toast.LENGTH_SHORT).show();
        }
    }
}