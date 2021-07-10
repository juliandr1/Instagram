package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.R;
import com.example.instagram.activities.DetailActivity;
import com.example.instagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.json.JSONException;
import java.util.List;
/*
     PostsAdapter.java is an adapter that binds posts to the recycler view.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static String TAG = "PostsAdapter";

    private final Context context;
    private final List<Post> posts;
    private final ParseUser user;

    // Pass in the context and list of tweets
    public PostsAdapter(Context context, List<Post> posts, ParseUser user) {
        this.context = context;
        this.posts = posts;
        this.user = user;
    }

    // For each row, inflate the layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Post post = posts.get(position);
        // Bind the tweet with view holder
        holder.bind(post);
    }

    // Return amount of posts
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView ivPostedImage, ivProfilePicTimeline;
        private final TextView tvDescription, tvNumLikes, tvUsername;
        private final Button btnLike;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            ivPostedImage = itemView.findViewById(R.id.ivPostedImage);
            ivProfilePicTimeline = itemView.findViewById(R.id.ivProfilePicTimeline);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
            btnLike = itemView.findViewById(R.id.btnLike);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);

            // Like or unlike posts and update the color of the heart.
            btnLike.setOnClickListener(v -> {
                Post post = posts.get(getAdapterPosition());
                try {
                    // If the user has liked the post before then unlike it.
                    if (post.didLike(user)) {
                        Log.i(TAG, "Unlike");
                        btnLike.setActivated(false);
                        post.unlike(user);
                        int numLikes = post.getNumLikes();
                        checkLikes(numLikes);
                    } else {
                        Log.i(TAG, "Like");
                        btnLike.setActivated(true);
                        post.like(user);
                        int numLikes = post.getNumLikes();
                        Log.i("TAG", ""+numLikes);
                        checkLikes(numLikes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPostedImage);
            }

            ParseFile profilePic = post.getUser().getParseFile("profilePic");

            assert profilePic != null;
            Glide.with(context).load(profilePic.getUrl()).transform(new RoundedCorners(300)).into(ivProfilePicTimeline);

            try {
                btnLike.setActivated(!post.didLike(user));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int numLikes = post.getNumLikes();
            checkLikes(numLikes);

        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // New intent
                Intent intent = new Intent(context, DetailActivity.class);
                // Serialize the tweets using parceler, using the tweets shorter name
                intent.putExtra(Post.class.getSimpleName(), posts.get(position));
                // show the activity
                context.startActivity(intent);
            }
        }

        // Check how the number of likes affects the text describing likes
        public void checkLikes (int numLikes) {
            if (numLikes == 0) {
                tvNumLikes.setText("");
            } else if (numLikes == 1) {
                tvNumLikes.setText(numLikes + " like");
            } else {
                tvNumLikes.setText(numLikes + " likes");
            }
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
