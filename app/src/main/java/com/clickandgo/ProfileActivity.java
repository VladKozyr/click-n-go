package com.clickandgo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    //TODO: remove code template "to remove action bar"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("ACTION BAR", e.getMessage());
        }
    }

    public void onBackArrowClick(View view) {
        finish();
    }
}