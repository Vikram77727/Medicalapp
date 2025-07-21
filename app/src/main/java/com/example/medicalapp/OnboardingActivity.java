package com.example.medicalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private Button btnNext, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        // Create onboarding fragments list
        List<OnboardingFragment> fragments = new ArrayList<>();
        fragments.add(OnboardingFragment.newInstance(R.drawable.logo, "Welcome to MediCare", "Get instant medical consultation and medicine recommendations."));
        fragments.add(OnboardingFragment.newInstance(R.drawable.healthsurvey, "Describe Your Health Issue", "Enter your symptoms, age, gender, and duration of illness."));
        fragments.add(OnboardingFragment.newInstance(R.drawable.checkbox_selection, "Select Symptoms & Get Suggestions", "Choose your symptoms from a list, and we’ll suggest suitable medicines."));
        fragments.add(OnboardingFragment.newInstance(R.drawable.shoppingcart, "Easy & Secure Purchase", "Buy your recommended medicines easily and securely."));
        fragments.add(OnboardingFragment.newInstance(R.drawable.reorder, "Quick Reorder", "Save your medicine history and reorder them instantly when needed."));
        fragments.add(OnboardingFragment.newInstance(R.drawable.getstarted, "Get Started!", "Begin your health journey today!"));

        // Set adapter
        adapter = new OnboardingAdapter(this, fragments);
        viewPager.setAdapter(adapter);

        // Next button logic
        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < fragments.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(OnboardingActivity.this, login.class));
                finish();
            }
        });

        // Skip button logic
        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(OnboardingActivity.this, login.class));
            finish();
        });
    }
}
