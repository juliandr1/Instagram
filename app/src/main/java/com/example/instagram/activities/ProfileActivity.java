package com.example.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;
import com.parse.ParseUser;

/*
     ProfileActivity.java is not finished, but it is meant to be the profile page of a user,
     feauturing their key stats (followers, following, posts, etc).
 */

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";

    Button btnLogOut;
    Context context;
    BottomNavigationView bottomNavigationView;
    ImageView ivProfilePic;
    TextView numPosts, numFollowers, numFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnLogOut = findViewById(R.id.btnLogOut);

        context = this;

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timeline:
                        Log.i(TAG,"logout -> timeline");
                        startActivity(new Intent(context, TimelineActivity.class));
                        return true;
                    case R.id.post:
                        Log.i(TAG,"logout -> post");
                        startActivity(new Intent(context, PostActivity.class));
                        return true;
                }
                return false;
            }
        });

        ivProfilePic = findViewById(R.id.ivProfilePic);
        ParseUser user = ParseUser.getCurrentUser();
        Glide.with(context).load(user.getParseFile("profilePic").getUrl()).transform(new RoundedCorners(300)).into(ivProfilePic);
    }
}
