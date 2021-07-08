package com.example.instagram.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.Post;
import com.example.instagram.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView tvUsernameDetail, tvDescriptionDetail, tvCreatedAtDetail;
    ImageView ivPostedImageDetail;
    Button btnBack;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        post = getIntent().getParcelableExtra(Post.class.getSimpleName());

        tvUsernameDetail = findViewById(R.id.tvUsernameDetail);
        tvUsernameDetail.setText(post.getUser().getUsername());

        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvDescriptionDetail.setText(post.getDescription());

        tvCreatedAtDetail = findViewById(R.id.tvCreatedAtDetail);
        tvCreatedAtDetail.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ivPostedImageDetail = findViewById(R.id.ivPostedImageDetail);
        Glide.with(this).load(post.getImage().getUrl()).into(ivPostedImageDetail);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
