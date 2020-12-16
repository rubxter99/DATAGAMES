package com.example.datagames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String imageUrl;
    private String mId;
    private String mGenres;
    private String mVideo;
    private String mStore;
    private String mStoreName;
    private String mPlatform;
    private NestedScrollView scrollView;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private JSONObject mDetailsGames = new JSONObject();
    private ArrayList<JSONObject> mDetailsGamesRellenos = new ArrayList<>();
    public static ArrayList<DetailParse.details> mGamesFav = new ArrayList();
    private TextView txtTitle;
    private static final int CODINTFAVGAME = 1;
    private RecyclerView recyclerView;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail);

        mAuth = FirebaseAuth.getInstance();//Conexión Base de Datos Firebase

        // Añadir action bar
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            mostrarDatos(intent);
        }
    }

    private void mostrarDatos(Intent intent) { //Mostrar todos los datos recogidos del videojuego tanto del intent como de la actividad Detalles

        //Visualización de cada dato en la actividad
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        scrollView = findViewById(R.id.scroll);
        recyclerView = findViewById(R.id.recycler_view);
        mId = intent.getStringExtra(HelperGlobal.EXTRA_ID);
        imageUrl = intent.getStringExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT);
        mGenres = intent.getStringExtra(HelperGlobal.EXTRA_GENRE);
        mVideo = intent.getStringExtra(HelperGlobal.EXTRA_CLIP);
        mStoreName = intent.getStringExtra(HelperGlobal.EXTRA_STORENAME);
        mStore = intent.getStringExtra(HelperGlobal.EXTRA_STORE);
        mPlatform = intent.getStringExtra(HelperGlobal.EXTRA_PLATFORM);
        ImageView shorts_imageview = findViewById(R.id.snapshot);
        Picasso.get().load(imageUrl).fit().centerCrop().into(shorts_imageview);
        TextView genresgame = findViewById(R.id.genres);
        VideoView video = findViewById(R.id.video);
        TextView txtStore = findViewById(R.id.store);
        TextView txtStoreName = findViewById(R.id.storename);
        TextView txtPlatform = findViewById(R.id.platform);
        final MediaController mediaController = new MediaController(findViewById(R.id.frame).getContext());
        Uri uri = Uri.parse(mVideo);

        //Cargar métodos de lista de detalles del videojuego y el menú deslizante
        loadGameDetail(mId);
        navigationDrawer();


        //Mostrar video del videojuego
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.setVideoURI(uri);
        video.requestFocus();

        //Hacer que al deslizar se deshabilite la reproducción del video
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });

        //Introducir el texto de cada campo de la actividad
        genresgame.setText(mGenres);
        txtStore.setText(mStore);
        Log.d("url",mStore);

        if (mStoreName.equalsIgnoreCase("")) {
            txtStoreName.setText("Non information");
        } else {
            txtStoreName.setText(mStoreName);
        }


        if (mPlatform.equalsIgnoreCase("")) {
            txtPlatform.setText("Non information");
        } else {
            txtPlatform.setText(mPlatform);
        }

    }

    private void setToolbar() { //Añadirá el menú superior mediante un toolbar

        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(txtTitle.getText());

    }

    private void navigationDrawer() { //Mostrar las opciones que muestra nuestro menú deslizante y nos llevará a cada dirección seleccionada
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent intent1 = new Intent(DetailActivity.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.nav_games:
                        Intent intent6 = new Intent(DetailActivity.this, GameList.class);
                        startActivity(intent6);
                        finish();
                        break;

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(DetailActivity.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;

                    case R.id.nav_fav:
                        Intent favGames = new Intent(DetailActivity.this, FavGames.class);
                        favGames.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                        startActivityForResult(favGames, CODINTFAVGAME);
                        leerDatosSPFavs();
                        break;

                    case R.id.nav_profile:
                        Intent intent2 = new Intent(DetailActivity.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(DetailActivity.this, MainActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Cierra todas las actividades anteriores
                        startActivity(intent3);
                        restaurar();
                        finish();
                        break;
                    case R.id.nav_shops:
                        Intent intent5 = new Intent(DetailActivity.this, Shops.class);
                        startActivity(intent5);
                        finish();
                        break;

                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }




    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) { //Mostrará un mensaje inferior al presionar favourites del menú superior
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Coger la referencia del layout menú
        getMenuInflater().inflate(R.menu.detail_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Mostrar las opciones del menú superior genérico y sus respectivas funciones
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (id) {
            case android.R.id.home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_about:
                showSnackBar("About us");
                Intent intent = new Intent(DetailActivity.this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_favorite:
                boolean encontrado = false;
                try {
                    if (mGamesFav.size() != 0) {
                        for (int x = 0; x < mGamesFav.size(); x++) {
                            if (mGamesFav.get(x).getName().equalsIgnoreCase(mDetailsGamesRellenos.get(0).get("name").toString())
                                    && (mGamesFav.get(x).getImage().equalsIgnoreCase(mDetailsGamesRellenos.get(0).get("background_image").toString()))) {

                                encontrado = true;
                                break;
                            }
                        }
                    }

                    if (encontrado) {
                        showSnackBar("This Game is already added ");
                    } else {
                        mGamesFav.add(new DetailParse.details(mId, mDetailsGamesRellenos.get(0).get("background_image").toString(), mDetailsGamesRellenos.get(0).get("name").toString(), mDetailsGamesRellenos.get(0).get("rating").toString(), mGenres, mDetailsGamesRellenos.get(0).get("released").toString(), mDetailsGamesRellenos.get(0).get("description_raw").toString(), mPlatform, mDetailsGamesRellenos.get(0).get("website").toString(), mDetailsGamesRellenos.get(0).get("metacritic").toString(), mStoreName, mStore, mVideo));
                        guardarDatoSPFavs();
                        showSnackBar("Added to Favorites");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return true;
            case R.id.action_share:
                try {
                    Intent intent3 = new Intent(Intent.ACTION_SEND);
                    intent3.setType("text/plain");
                    intent3.putExtra(Intent.EXTRA_TEXT, "The selected game search for it on the website " + mDetailsGamesRellenos.get(0).get("website").toString());
                    startActivity(Intent.createChooser(intent3, "Share with"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadGameDetail(String mId) { //Cargar el conjunto de datos del videojuego recogiendo el id del mismo y llamada del JSONOBJECT directamente de la actividad DetailParse

        RequestQueue queue4 = Volley.newRequestQueue(this);
        String url4 = "https://api.rawg.io/api/games/" + mId;
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DetailParse detailParse = new DetailParse();
                        mDetailsGames = detailParse.parseDetailsGame(response);

                        try {

                            if (mDetailsGames.get("name").toString() == "" || mDetailsGames.get("description").toString() == "" ||
                                    mDetailsGames.get("rating").toString().contentEquals("0") || mDetailsGames.get("released").toString() == "null" || mDetailsGames.get("genres").toString() == "" || mDetailsGames.get("id").toString() == "") {
                                Toast.makeText(DetailActivity.this, "No data needed from the selected game", Toast.LENGTH_SHORT).show();

                            } else {

                                mDetailsGamesRellenos.add(mDetailsGames);
                                Toast.makeText(DetailActivity.this, "The required data for the selected game may not be displayed", Toast.LENGTH_SHORT).show();
                                try {

                                    ImageView img = findViewById(R.id.image_paralax);
                                    Picasso.get().load(mDetailsGamesRellenos.get(0).get("background_image").toString()).resize(2048, 1600)
                                            .into(img);


                                    txtTitle = findViewById(R.id.titleGame);
                                    txtTitle.setText(mDetailsGamesRellenos.get(0).get("name").toString());

                                    TextView txtRtaing = findViewById(R.id.rating);
                                    txtRtaing.setText(mDetailsGamesRellenos.get(0).get("rating").toString());

                                    TextView txtMetacritic = findViewById(R.id.metacritic);
                                    if (mDetailsGamesRellenos.get(0).get("metacritic").toString().equalsIgnoreCase("null")) {
                                        txtMetacritic.setText("Non Info.");
                                    } else {
                                        txtMetacritic.setText(mDetailsGamesRellenos.get(0).get("metacritic").toString());
                                    }

                                    TextView txtDescription = findViewById(R.id.description);
                                    txtDescription.setText(mDetailsGamesRellenos.get(0).get("description_raw").toString());


                                    TextView txtReleased = findViewById(R.id.released);
                                    txtReleased.setText(mDetailsGamesRellenos.get(0).get("released").toString());


                                    TextView txtWebsite = findViewById(R.id.website);
                                    if (mDetailsGamesRellenos.get(0).get("website").toString().equalsIgnoreCase("")) {
                                        txtWebsite.setText("Non information");
                                    } else {
                                        txtWebsite.setText(mDetailsGamesRellenos.get(0).get("website").toString());
                                    }

                                    setToolbar();

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest4.setShouldCache(false);
        queue4.add(stringRequest4);

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

    private void guardarDatoSPFavs() { //Envío de datos del videojuego dirigidos al sharedpreferences
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGamesFav);
        prefsEditor.putString(HelperGlobal.ARRAYTIENDASFAV, json);
        prefsEditor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //Al comienzo de la llamada a la actividad se llevará los datos del sharedpreferences
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODINTFAVGAME) {
            leerDatosSPFavs();
        }

    }
    private void restaurar() { //Eliminar los filtros guardados junto con el sharedpreferences y marcarlo por defecto
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences mPrefs2 = getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES, MODE_PRIVATE);
        prefsEditor2 = mPrefs.edit();
        prefsEditor2.clear();
        prefsEditor2.commit();
    }

}