package com.example.medicalapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OnboardingFragment extends Fragment {

    public static OnboardingFragment newInstance(int imageResId, String title, String description) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt("image", imageResId);
        args.putString("title", title);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragmentonboarding, container, false);

        // Bind views and set values
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView titleView = view.findViewById(R.id.title);
        TextView descriptionView = view.findViewById(R.id.description);

        if (getArguments() != null) {
            imageView.setImageResource(getArguments().getInt("image"));
            titleView.setText(getArguments().getString("title"));
            descriptionView.setText(getArguments().getString("description"));
        }

        return view;
    }
}
