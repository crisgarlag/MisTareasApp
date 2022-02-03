package com.example.mistareas;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    /**
     * Metodo para crear la activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        ImageView logo = (ImageView) findViewById(R.id.logo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animacion);
        logo.startAnimation(anim);
        anim.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     * Al finalizar la animacion pasa a la activity de login
     * @param animation
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}