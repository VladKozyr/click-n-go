package com.clickandgo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.clickandgo.AuthActivity;
import com.clickandgo.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this, AuthActivity.class));
        }else{
            startActivity(new Intent(this, SearchActivity.class));
        }
        finish();
    }


}