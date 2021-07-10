package com.example.instagram.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.models.Post;
import com.example.instagram.R;
/*
    DetailActivity.java allows the user to get a separate view with just the post, including the
    the time when the post was published.
 */

public class DetailActivity extends AppCompatActivity {

    TextView tvUsernameDetail, tvDescriptionDetail, tvCreatedAtDetail;
    ImageView ivPostedImageDetail;
    Button btnBack;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Unwrap the desired post which is inflated
        post = getIntent().getParcelableExtra(Post.class.getSimpleName());

        // Set the post details as needed
        tvUsernameDetail = findViewById(R.id.tvUsernameDetail);
        tvUsernameDetail.setText(post.getUser().getUsername());

        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvDescriptionDetail.setText(post.getDescription());

        tvCreatedAtDetail = findViewById(R.id.tvCreatedAtDetail);
        tvCreatedAtDetail.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ivPostedImageDetail = findViewById(R.id.ivPostedImageDetail);
        Glide.with(this).load(post.getImage().getUrl()).into(ivPostedImageDetail);


        // Back button returns to the timeline
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

    }
}
