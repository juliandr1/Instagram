package com.example.instagram.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.instagram.BitmapScaler;
import com.example.instagram.models.Post;
import com.example.instagram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;
import java.io.File;

/*
    PostActivity.java is an activity which allows users to post a picture with their camera,
    along with a description.
 */

public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    public String photoFileName = "photo.jpg";
    private File photoFile;
    private EditText etDescription;
    private ImageView ivPostImage;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        context = this;

        etDescription = findViewById(R.id.etDescription);
        ivPostImage = findViewById(R.id.ivPostImage);

        // Setup the bottom navigation view for the activity and outline the flow
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.post);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.timeline:
                    Log.i(TAG,"post -> timeline");
                    startActivity(new Intent(context, TimelineActivity.class));
                    return true;
                case R.id.profile:
                    Log.i(TAG,"post -> logout");
                    startActivity(new Intent(context, ProfileActivity.class));
                    return true;
            }
            return false;
        });

        // Post the post with an image and a description
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            String description = etDescription.getText().toString();
            // Check to see if description is empty
            if (description.isEmpty()) {
                Toast.makeText(PostActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile == null || ivPostImage.getDrawable() == null) {
                Toast.makeText(PostActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(description, currentUser, photoFile);
        });

        Button btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"Activity Result");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 350);
                ivPostImage.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void launchCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files--api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider.INSTAGRAM", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.i(TAG, "Intent successful");
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving", e);
                Toast.makeText(PostActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "Post save was successful!");
            etDescription.setText("");
            ivPostImage.setImageResource(0);
            startActivity(new Intent(context, TimelineActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

}

