package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.instagram.R;
import com.parse.ParseUser;

/*
    LoginActivity.java allows the user to login to their account or click a button to register
    for an account.
 */

public class LoginActivity extends AppCompatActivity {

    public static String TAG = "LoginActivity";

    private EditText etUsername, etPassword;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();

        // Check to see if the user is already logged in
        if (currentUser != null) {
            goMainActivity();
        }

        context = this;

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Attempt to login, using the login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });

        // Go to the register activity in order to create an account
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Login by checking if the user exists in the database. If yes go to their timeline, if not
    // show a toast saying they could not login.
    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login: " + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(context, "Username and password combination incorrect or does not exist", Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
        });
    }

    // Go to the timeline.
    private void goMainActivity() {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}