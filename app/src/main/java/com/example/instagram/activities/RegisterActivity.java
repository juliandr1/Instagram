package com.example.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.instagram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/*
    RegisterActivity.java allows the user to create a new account with a username and a password.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText etUsernameCreate, etPasswordCreate;
    Button btnCreate;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ParseUser user = new ParseUser();

        context = this;

        etUsernameCreate = findViewById(R.id.etUsernameCreate);
        etPasswordCreate = findViewById(R.id.etPasswordCreate);

        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(etUsernameCreate.getText().toString());
                user.setPassword(etPasswordCreate.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            goMainActivity();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void goMainActivity() {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }

}


