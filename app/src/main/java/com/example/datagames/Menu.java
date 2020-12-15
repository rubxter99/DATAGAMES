package com.example.datagames;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.ActionCodeUrl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.datagames.DetailActivity.mGamesFav;


public class Menu extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CardView cdPerfil;
    private CardView cdGames;
    private CardView cdMaps;
    private CardView cdInfo;
    private CardView cdFavoritos;
    private CardView cdStore;
    private boolean finish;
    private static final int CODINTFAVGAME = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        finish = getIntent().getBooleanExtra("finish", false);
        mAuth = FirebaseAuth.getInstance();//Iniciar la conexión con la base de datos de Firebase
        mostrarDatos();


    }

    private void mostrarDatos() { //Muestra la lista de opciones del menú y cada uno con sus respectivas direcciones hacia su actividad


        cdPerfil = findViewById(R.id.cd_perfil);
        cdPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this, Profile.class));

            }
        });
        cdGames = findViewById(R.id.cd_games);
        cdGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this, GameList.class));

            }
        });
        cdStore = findViewById(R.id.cd_tiendas);
        cdStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this, Shops.class));


            }
        });

        cdMaps = findViewById(R.id.cd_mapa);
        cdMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this, MapsActivity.class));
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
                startActivityForResult(favGames, CODINTFAVGAME);
                leerDatosSPFavs();

            }
        });

    }

    private void leerDatosSPFavs() { //Llama a la recogida de datos del sharedpreferences en nuestro caso los videojuegos favoritos para mostrarlos en la actividad de favoritos
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYTIENDASFAV, "");
        Type founderListType = new TypeToken<ArrayList<DetailParse.details>>() {
        }.getType();
        ArrayList<DetailParse.details> restoreArray = gson.fromJson(json, founderListType);

        if (restoreArray != null) {
            mGamesFav = restoreArray;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//Al comienzo de la llamada a la actividad  se llevará los datos del sharedpreferences
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODINTFAVGAME) {
            leerDatosSPFavs();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}