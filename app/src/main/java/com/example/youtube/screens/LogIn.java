package com.example.youtube.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.youtube.AppDatabase;
import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.UserSession;
import com.example.youtube.entities.user;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class LogIn extends AppCompatActivity {

    private TextInputLayout emailEditText, passwordEditText;
    private ArrayList<user> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_login_button);
        Button signUpButton = findViewById(R.id.login_to_signup_button);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userDb").allowMainThreadQueries().build();

        users = new ArrayList<>(db.userDao().getAllUsers());

        Objects.requireNonNull(emailEditText.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEditText.setError(null); // Clear error when user starts typing
                emailEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Objects.requireNonNull(passwordEditText.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setError(null); // Clear error when user starts typing
                passwordEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginButton.setOnClickListener(v -> login());
        signUpButton.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        Intent intent = new Intent(LogIn.this, SignUpActivity.class);
        resetFields();
        startActivity(intent);
    }

    private void login() {
        String email = Objects.requireNonNull(emailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getEditText()).getText().toString().trim();

        if (password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
        }
        if (email.isEmpty()) {
            emailEditText.setError("Please enter an email");
        }
        if (password.isEmpty()||email.isEmpty()){
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            return;
        }

        // Check if the email and password match any user in the users list
        if (users != null) {
            for (user u : users) {
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    UserSession.getInstance().setUserId(u.getId());
                    resetFields();
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
        // If no match is found, show an error message
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {
        Objects.requireNonNull(emailEditText.getEditText()).setText("");
        Objects.requireNonNull(passwordEditText.getEditText()).setText("");
    }
}
