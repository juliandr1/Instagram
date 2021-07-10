package com.example.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

/*
    TimelineActivity.java displays the timeline of posts and allows users to scroll and like
    posts.
 */

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    private SwipeRefreshLayout swipeContainer;
    private List<Post> posts;
    private PostsAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        context = this;
        ParseUser user = ParseUser.getCurrentUser();

        // Find the Recycler View
        RecyclerView rvTweets = findViewById(R.id.rvPosts);
        // Init the list of tweets and adapter
        posts = new ArrayList<>();
        adapter = new PostsAdapter(this, posts, user);
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        // Setup the bottom navigation view for the activity and outline the flow
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.timeline);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.post:
                    Log.i(TAG,"timeline -> post");
                    startActivity(new Intent(context, PostActivity.class));
                    return true;
                case R.id.profile:
                    Log.i(TAG,"timeline -> logout");
                    startActivity(new Intent(context, ProfileActivity.class));
                    return true;
            }
            return false;
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {

            fetchTimelineAsync();
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Get the posts
        queryPosts();
    }

    // Get all the posts in the database, including the ones for the current user.
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        // Order the posts by date
        query.addDescendingOrder("createdAt");
        // Get the posts
        query.findInBackground((newPosts, e) -> {
            if (e != null) {
                Log.e(TAG,"Issue with getting posts", e);
                return;
            }
            for (Post post: newPosts) {
                Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getCreatedAt().toString());
            }
            posts.addAll(newPosts);
            adapter.notifyDataSetChanged();
        });
    }

    // Refresh by clearing everything then getting the latest post
    public void fetchTimelineAsync() {
        adapter.clear();
        queryPosts();
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
}
