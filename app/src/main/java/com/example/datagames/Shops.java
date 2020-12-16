package com.example.datagames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.datagames.DetailActivity.mGamesFav;

public class Shops extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CardView cdGame;
    private CardView cdSteam;
    private CardView cdOrigin;
    private CardView cdUbisoft;
    private CardView cdEpicGames;
    private CardView cdPlaystation;
    private CardView cdXbox;
    private CardView cdNintendo;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final int CODINTFAVGAME = 1;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        mAuth = FirebaseAuth.getInstance();
        mostrarDatos();

    }

    private void mostrarDatos() { //Muestra la lista de las tiendas digitales online de la actividad Tiendas y cada uno con sus respectivos enlaces a sus páginas web
        //Menu superior solo con el menú deslizante
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        setToolBar();
        navigationDrawer();

        //Listado de las tiendas digitales de videojuegos
        cdGame = findViewById(R.id.cd_game);
        cdGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                String url = "https://www.game.es/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        cdSteam = findViewById(R.id.cd_steam);
        cdSteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                String url = "https://store.steampowered.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        cdOrigin = findViewById(R.id.cd_origin);
        cdOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                String url = "https://www.origin.com/esp/es-es/store";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        cdUbisoft = findViewById(R.id.cd_ubisoft);
        cdUbisoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                String url = "https://store.ubi.com/es/games";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        cdEpicGames = findViewById(R.id.cd_EpicGames);
        cdEpicGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                String url = "https://www.epicgames.com/store/es-ES/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        cdPlaystation = findViewById(R.id.cd_playstationstore);
        cdPlaystation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                String url = "https://store.playstation.com/es-es/latest";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        cdXbox = findViewById(R.id.cd_xboxstore);
        cdXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                String url = "https://www.xbox.com/es-ES/games";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        cdNintendo = findViewById(R.id.cd_nintendoeshop);
        cdNintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser();
                String url = "https://www.nintendo.es/My-Nintendo-Store/Nintendo-eShop/Nintendo-eShop-1806894.html?redirect=true";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
    }

    private void setToolBar() { //Creación del menú deslizante
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //Mostrar menú deslizante al seleccionar el botón de tres lineas del menú superior
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                //return true;
                break;

        }

        return true;

        // return super.onOptionsItemSelected(item);
    }

    private void navigationDrawer() { //Mostrar los apartados del menú deslizante con sus respectivas direcciones a cada ventana

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent intent1 = new Intent(Shops.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.nav_games:
                        Intent intent6 = new Intent(Shops.this, GameList.class);
                        startActivity(intent6);
                        finish();
                        break;

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(Shops.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_fav:
                        Intent favGames = new Intent(Shops.this, FavGames.class);
                        favGames.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                        startActivityForResult(favGames, CODINTFAVGAME);
                        leerDatosSPFavs();
                        finish();
                        break;

                    case R.id.nav_profile:
                        Intent intent2 = new Intent(Shops.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(Shops.this, MainActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Cierra todas las actividades anteriores
                        startActivity(intent3);
                        finish();
                        break;


                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }

    private void leerDatosSPFavs() { //Transforma los datos recogidos del sharedpreferences en nuestro caso los videojuegos favoritos para mostrarlos en la actividad de favoritos
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

}