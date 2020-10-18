package com.example.datagames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.ActionCodeUrl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Menu extends Activity  {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CardView cdPerfil;
    private CardView cdGames;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth= FirebaseAuth.getInstance();

        cdPerfil=findViewById(R.id.cd_perfil);
        cdPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this,Profile.class));

            }
        });
        cdGames=findViewById(R.id.cd_games);
        cdGames.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this,GameList.class));

            }
        });
    }
}