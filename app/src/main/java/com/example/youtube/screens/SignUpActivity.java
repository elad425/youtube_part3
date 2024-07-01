package com.example.youtube.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Window;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputLayout usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Uri imageUri;
    private ArrayList<video> videos;
    private ArrayList<user> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        usernameEditText = findViewById(R.id.sign_up_username);

        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);
        confirmPasswordEditText = findViewById(R.id.sign_up_confirm_password);
        Button uploadButton = findViewById(R.id.sign_up_upload_button);
        Button signUpButton = findViewById(R.id.sign_up_button);
        Button loginButton = findViewById(R.id.sign_up_login_button);

        Intent intent = getIntent();
        videos = intent.getParcelableArrayListExtra("video_list");
        users = intent.getParcelableArrayListExtra("users");

        uploadButton.setOnClickListener(v -> openFileChooser());
        signUpButton.setOnClickListener(v -> signUp());
        loginButton.setOnClickListener(v->login());
        clearErrorOnTyping(usernameEditText);
        clearErrorOnTyping(emailEditText);
        clearErrorOnTyping(passwordEditText);
        clearErrorOnTyping(confirmPasswordEditText);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void login(){
        resetFields();
        Intent intent = new Intent(SignUpActivity.this, LogIn.class);
        intent.putParcelableArrayListExtra("video_list", videos);
        intent.putParcelableArrayListExtra("users", users);
        startActivity(intent);
    }

    private void resetFields() {
        usernameEditText.getEditText().setText("");
        emailEditText.getEditText().setText("");
        passwordEditText.getEditText().setText("");
        confirmPasswordEditText.getEditText().setText("");
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Toast.makeText(this, "profile picture added", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        String username = usernameEditText.getEditText().getText().toString().trim();
        String email = emailEditText.getEditText().getText().toString().trim();
        String password = passwordEditText.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getEditText().getText().toString().trim();
        if (username.isEmpty()){
            usernameEditText.setError("You need to enter a username");
        }
        if (email.isEmpty()){
            emailEditText.setError("Please enter an email");
        }
        if (password.isEmpty()){
            passwordEditText.setError("Please enter a password");

        }
        if(confirmPassword.isEmpty()){
            confirmPasswordEditText.setError("You need to enter a password confirmation");
        }

        if(username.length()>=20){
            Toast.makeText(this, "username too long, must be under 20 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(GeneralUtils.isUserExist(users,email)){
            Toast.makeText(this, "this email is already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            passwordEditText.setError("Password must be at least 8 characters long and contain at least one number");
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        user new_user = new user(username,email,password,imageUri.toString());
        if (users==null){
            users=new ArrayList<>();
        }
        users.add(new_user);

        Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show();
        login();
    }

    private void clearErrorOnTyping(TextInputLayout textInputLayout) {
        TextInputEditText editText = (TextInputEditText) textInputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError(null); // Clear error when user starts typing
                textInputLayout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
