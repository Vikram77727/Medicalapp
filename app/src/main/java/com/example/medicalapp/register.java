package com.example.medicalapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class register extends AppCompatActivity {
    EditText etFirstName, etLastName, etUsername, etEmail, etPhone, etPassword, etPassword2;
    CheckBox chkTerms;
    Button btnCreateAccount;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        chkTerms = findViewById(R.id.chkTerms);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        dbHelper = new DatabaseHelper(this);

        btnCreateAccount.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        // Input Validations
        if (firstName.isEmpty()) {
            etFirstName.setError("First Name can't be empty");
            etFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            etLastName.setError("Last Name can't be empty");
            etLastName.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            etUsername.setError("Username can't be empty");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty() || !email.endsWith("@gmail.com")) {
            etEmail.setError("Enter a valid Gmail address");
            etEmail.requestFocus();
            return;
        }
        if (phone.isEmpty() || !phone.matches("\\d{10}")) {
            etPhone.setError("Phone number must be 10 digits");
            etPhone.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(password2)) {
            etPassword2.setError("Passwords do not match");
            etPassword2.requestFocus();
            return;
        }
        if (!chkTerms.isChecked()) {
            Toast.makeText(register.this, "You must agree to the Terms & Privacy Policy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email or phone already exists
        if (dbHelper.emailExists(email)) {
            etEmail.setError("Email already registered");
            etEmail.requestFocus();
            return;
        }
        if (dbHelper.phoneExists(phone)) {
            etPhone.setError("Phone number already registered");
            etPhone.requestFocus();
            return;
        }
        if (dbHelper.usernameExists(username)) {
            etUsername.setError("Username already taken");
            etUsername.requestFocus();
            return;
        }
        else {
            // Save to SQLite
            boolean inserted = dbHelper.insertUser(firstName, lastName, username, email, phone, password);
            if (inserted) {
                Toast.makeText(register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(register.this, login.class));
                finish();
            } else {
                Toast.makeText(register.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}