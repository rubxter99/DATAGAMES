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
    public static int SPLASH_SCREEN = 2800;
    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView txt_logo, txt_slogan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_datagames);

        topAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.top_animation);//Animación de la parte de arriba hacia abajo de la actividad
        bottomAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.bottom_animation);//Animación de la parte de abajo hacia arriba de la actividad

        logo = findViewById(R.id.logo);
        txt_logo = findViewById(R.id.txt_logo);
        txt_slogan = findViewById(R.id.txt_logodesc);

        logo.setAnimation(topAnim);//Animación del logo desde arriba
        txt_logo.setAnimation(bottomAnim);//Animación del texto del logo desde abajo
        txt_slogan.setAnimation(bottomAnim);//Animación del texto secundario del logo desde abajo


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { //Al terminar el Splash nos lleve a los fragmentos de introducción
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}
