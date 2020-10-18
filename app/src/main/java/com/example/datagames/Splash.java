package com.example.datagames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Splash extends Activity {
    public static int SPLASH_SCREEN=2800;
    Animation topAnim,bottomAnim;
    ImageView logo;
    TextView txt_logo,txt_slogan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash1);

        topAnim= AnimationUtils.loadAnimation(Splash.this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(Splash.this,R.anim.bottom_animation);

        logo=findViewById(R.id.logo);
        txt_logo=findViewById(R.id.txt_logo);
        txt_slogan=findViewById(R.id.txt_logodesc);

        logo.setAnimation(topAnim);
        txt_logo.setAnimation(bottomAnim);
        txt_slogan.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
