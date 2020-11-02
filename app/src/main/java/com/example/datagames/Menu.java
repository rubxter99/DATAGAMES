package com.example.datagames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.ActionCodeUrl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.datagames.GameList.mGamesFav;

public class Menu extends Activity  {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CardView cdPerfil;
    private CardView cdGames;
    private CardView cdMaps;
    private CardView cdInfo;
    private CardView cdFavoritos;
    private static final int CODINTFAVGAME = 1;


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

        cdMaps = findViewById(R.id.cd_mapa);
        cdMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this,MapsActivity.class));
            }
        });

        cdInfo = findViewById(R.id.cd_settings);
        cdInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this, About.class));
            }
        });
        cdFavoritos = findViewById(R.id.cd_fav);
        cdFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                Intent favGames = new Intent(Menu.this, FavGames.class);
                favGames.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                startActivityForResult(favGames,CODINTFAVGAME);
                leerDatosSPFavs();

            }
        });


    }
    private void leerDatosSPFavs() {
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYTIENDASFAV, "");
        Type founderListType = new TypeToken<ArrayList<GamesParse.game>>() {
        }.getType();
        ArrayList<GamesParse.game> restoreArray = gson.fromJson(json, founderListType);

        if (restoreArray != null) {
            mGamesFav = restoreArray;

        }
    }
}