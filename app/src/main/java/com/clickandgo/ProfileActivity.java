package com.clickandgo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.clickandgo.ui.profile.ProfileFragmentAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private ImageView userPhoto;
    private TextView textViewEmail;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileFragmentAdapter fragmentAdapter = new ProfileFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        ViewPager viewPager = findViewById(R.id.profileViewPager);
        viewPager.setAdapter(fragmentAdapter);

        TabLayout tabLayout = findViewById(R.id.profileTabLayout);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        toolbarLayout = findViewById(R.id.collapsingToolbar);
        userPhoto = findViewById(R.id.imageViewUserPhoto);
        textViewEmail = findViewById(R.id.textViewEmail);

        setupUserProfile();
    }

    private void setupUserProfile() {

        userPhoto.setOnClickListener(view -> ImagePicker.Companion.with(ProfileActivity.this)
                .cropSquare()
                .compress(512)
                .start());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null) {
                //TODO get from firestore nickname
                toolbarLayout.setTitle(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(userPhoto);
            }
            textViewEmail.setText(user.getEmail());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            assert data != null;
            Uri imageUri = data.getData();
            handleUpload(imageUri);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpload(Uri uri) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(uid);

        reference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    getDownloadUri(reference);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Photo load error", Toast.LENGTH_SHORT).show();
                });
    }

    private void getDownloadUri(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(this::updateUserProfile);
    }

    private void updateUserProfile(Uri photoUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(this)
                .load(photoUri)
                .circleCrop()
                .into(userPhoto);

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();

        assert user != null;
        user.updateProfile(request)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Photo updated", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Photo load error", Toast.LENGTH_SHORT).show();
                });
    }

    public void onBackArrowClick(View view) {
        finish();
    }
}