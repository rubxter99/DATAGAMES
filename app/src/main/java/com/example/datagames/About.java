package com.example.datagames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.datagames.DetailActivity.mGamesFav;

public class About extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final int CODINTFAVGAME = 1;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.toolbar);//Creación menu superior
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//Panel menú deslizante
        navigationView = (NavigationView) findViewById(R.id.navview);//Menú deslizante
        mAuth= FirebaseAuth.getInstance();//Conexión con la base de datos de Firebase
        setToolBar();
        navigationDrawer();
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

    }

    private void navigationDrawer() { //Mostrar los apartados del menú deslizante con sus respectivas direcciones a cada ventana

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent intent1 = new Intent(About.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.nav_games:
                        Intent intent6 = new Intent(About.this, GameList.class);
                        startActivity(intent6);
                        finish();
                        break;

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(About.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;


                    case R.id.nav_profile:
                        Intent intent2 = new Intent(About.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(About.this, MainActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Cierra todas las actividades anteriores
                        startActivity(intent3);

                        finish();
                        break;

                    case R.id.nav_shops:
                        Intent intent5 = new Intent(About.this, Shops.class);
                        startActivity(intent5);
                        finish();
                        break;
                    case R.id.nav_fav:
                        Intent favGames = new Intent(About.this, FavGames.class);
                        favGames.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                        startActivityForResult(favGames, CODINTFAVGAME);
                        leerDatosSPFavs();
                        finish();
                        break;
                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }
    private void leerDatosSPFavs() { //Transformación de los datos recogidos del sharedpreferences en nuestro caso los videojuegos favoritos para mostrarlos en la actividad de favoritos
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