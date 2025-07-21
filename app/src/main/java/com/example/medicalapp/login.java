package com.example.medicalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {

    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    TextView tvRegister, tvForgotPassword;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    etLoginEmail.setError("Email can't be empty");
                    etLoginEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etLoginPassword.setError("Password can't be empty");
                    etLoginPassword.requestFocus();
                    return;
                }

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor emailCursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

                if (emailCursor.moveToFirst()) {
                    // Email exists, now check password
                    Cursor loginCursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
                    if (loginCursor.moveToFirst()) {
                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Get user details
                        String firstName = loginCursor.getString(loginCursor.getColumnIndexOrThrow("first_name"));
                        String lastName = loginCursor.getString(loginCursor.getColumnIndexOrThrow("last_name"));

                        // Save user data in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("LoggedInEmail", email);
                        editor.putString("LoggedInFirstName", firstName);
                        editor.putString("LoggedInLastName", lastName);
                        editor.apply();

                        // Pass details to MainActivity if needed
                        Intent intent = new Intent(login.this, MainActivity.class);
                        intent.putExtra("first_name", firstName);
                        intent.putExtra("last_name", lastName);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                    }
                    loginCursor.close();
                } else {
                    Toast.makeText(login.this, "Email not registered!", Toast.LENGTH_SHORT).show();
                }
                emailCursor.close();
                db.close();
            }
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, forgotactivity.class);
            startActivity(intent);
        });


    }
}

