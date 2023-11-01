package com.example.pagetrade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashscreenActivity extends AppCompatActivity {

    ImageView logo, splashImg;
    LottieAnimationView lottieAnimationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        logo= findViewById(R.id.logo);
        splashImg= findViewById(R.id.img);
        lottieAnimationView= findViewById(R.id.lottie);

        splashImg.animate().translationY(-2800).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(2100).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1900).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashscreenActivity.this, IdeaActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}