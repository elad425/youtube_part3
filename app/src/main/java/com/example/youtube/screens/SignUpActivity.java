package com.example.youtube.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtube.R;
import com.example.youtube.viewmodels.SignUpViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputLayout usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Uri imageUri;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        usernameEditText = findViewById(R.id.sign_up_username);
        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);
        confirmPasswordEditText = findViewById(R.id.sign_up_confirm_password);
        Button uploadButton = findViewById(R.id.sign_up_upload_button);
        Button signUpButton = findViewById(R.id.sign_up_button);
        Button loginButton = findViewById(R.id.sign_up_login_button);

        uploadButton.setOnClickListener(v -> openFileChooser());
        signUpButton.setOnClickListener(v -> signUp());
        loginButton.setOnClickListener(v -> login());

        clearErrorOnTyping(usernameEditText);
        clearErrorOnTyping(emailEditText);
        clearErrorOnTyping(passwordEditText);
        clearErrorOnTyping(confirmPasswordEditText);
    }

    private void observeViewModel() {
        viewModel.getSignUpSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                login();
            }
        });

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void login() {
        resetFields();
        Intent intent = new Intent(SignUpActivity.this, LogIn.class);
        startActivity(intent);
    }

    private void resetFields() {
        Objects.requireNonNull(usernameEditText.getEditText()).setText("");
        Objects.requireNonNull(emailEditText.getEditText()).setText("");
        Objects.requireNonNull(passwordEditText.getEditText()).setText("");
        Objects.requireNonNull(confirmPasswordEditText.getEditText()).setText("");
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Toast.makeText(this, "Profile picture added", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        String username = Objects.requireNonNull(usernameEditText.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(emailEditText.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getEditText()).getText().toString().trim();

        viewModel.signUp(username, email, password, confirmPassword, imageUri);
    }

    private void clearErrorOnTyping(TextInputLayout textInputLayout) {
        TextInputEditText editText = (TextInputEditText) textInputLayout.getEditText();
        assert editText != null;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}