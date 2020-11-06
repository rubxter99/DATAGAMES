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
    private String mTitle;
    private String mReleased;
    private String mGenres;
    private String mVideo;
    private String mWebsite;
    private String mStore;
    private String mStoreName;
    private String mPlatform;
    private Double mRating;
    private NestedScrollView scrollView;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private JSONObject mDetailsGames = new JSONObject();
    private ArrayList<JSONObject> mDetailsGamesRellenos = new ArrayList<>();
    public static ArrayList<GamesParse.game> mGamesFav = new ArrayList<>();
    private DetailsGamesAdapter mDetailAdapter;
    private TextView txtTitle;
    private static final int CODINTFAVGAME = 1;
    private ArrayList<GamesParse.game> mDetailsGamesRellenoFinal=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        mAuth = FirebaseAuth.getInstance();
        scrollView = findViewById(R.id.scroll);
        // Añadir action bar

        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent != null) {

            mId = intent.getStringExtra(HelperGlobal.EXTRA_ID);
            imageUrl = intent.getStringExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT);
            mGenres = intent.getStringExtra(HelperGlobal.EXTRA_GENRE);
            mVideo = intent.getStringExtra(HelperGlobal.EXTRA_CLIP);
            mStoreName = intent.getStringExtra(HelperGlobal.EXTRA_STORENAME);
            mStore = intent.getStringExtra(HelperGlobal.EXTRA_STORE);
            mPlatform = intent.getStringExtra(HelperGlobal.EXTRA_PLATFORM);

            loadGameDetail(mId);
            Log.d("image:", imageUrl);
            ImageView shorts_imageview = findViewById(R.id.snapshot);
            Picasso.get().load(imageUrl).fit().centerCrop().into(shorts_imageview);
            TextView genresgame = findViewById(R.id.genres);
            genresgame.setText(mGenres);
            VideoView video = findViewById(R.id.video);
            Log.d("viedo:", mVideo.toString());
            Uri uri = Uri.parse(mVideo);
            final MediaController mediaController = new MediaController(findViewById(R.id.frame).getContext());
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
            video.requestFocus();
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    mediaController.hide();
                }
            });
            TextView txtStore = findViewById(R.id.store);
            txtStore.setText(mStore);
            Log.d("storeurl:", mStore);
            TextView txtStoreName = findViewById(R.id.storename);
            txtStoreName.setText(mStoreName);
            Log.d("storename:", mStoreName);
            TextView txtPlatform = findViewById(R.id.platform);
            txtPlatform.setText(mPlatform);
            navigationDrawer();


        }


    }

    private void setToolbar() {
        // Añadir la Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(txtTitle.getText());

    }

    private void navigationDrawer() {
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

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(DetailActivity.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;
/*
                    case R.id.nav_fav:


                        Intent favGames = new Intent(DetailActivity.this, FavGames.class);
                        favGames.putStringArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                        Log.d("this23",mGamesFav.toString());
                        startActivityForResult(favGames,CODINTFAVGAME);

                        leerDatosSPFavs();

                        break;*/

                    case R.id.nav_profile:
                        Intent intent2 = new Intent(DetailActivity.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(DetailActivity.this, MainActivity.class);
                        startActivity(intent3);
                        finish();
                        break;

                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }

    /**
     * Se carga una imagen aleatoria para el detalle
     */


    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (id) {
            case android.R.id.home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                //return true;
                return true;
            case R.id.action_about:
                showSnackBar("Sobre nosotros");
                Intent intent = new Intent(DetailActivity.this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_profile:
                showSnackBar("Perfil");
                Intent intent2 = new Intent(DetailActivity.this, Profile.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadGameDetail(String mId) {

        RequestQueue queue4 = Volley.newRequestQueue(this);
        String url4 = "https://api.rawg.io/api/games/" + mId;
        Log.d("idgame", url4);
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DetailParse detailParse = new DetailParse();
                        mDetailsGames = detailParse.parseDetailsGame(response);


                        try {
                            Log.d("onResponse:", mDetailsGames.get("name").toString());
                            if (mDetailsGames.get("name").toString() == "" || mDetailsGames.get("description").toString() == "" ||
                                    mDetailsGames.get("rating").toString().contentEquals("0") || mDetailsGames.get("released").toString() == "null" || mDetailsGames.get("genres").toString() == "" || mDetailsGames.get("id").toString() == "") {
                                Toast.makeText(DetailActivity.this, "No hay datos necesarios del juego seleccionado", Toast.LENGTH_SHORT).show();

                            } else {

                                mDetailsGamesRellenos.add(mDetailsGames);
                                Toast.makeText(DetailActivity.this, "Puede que no se muestren los datos necesarios del juego seleccionado", Toast.LENGTH_SHORT).show();
                                try {

                                    Log.d("mDetailsAdapter", mDetailsGamesRellenos.get(0).get("name").toString());

                                    ImageView img = findViewById(R.id.image_paralax);
                                    Picasso.get().load(mDetailsGamesRellenos.get(0).get("background_image").toString()).resize(2048, 1600)
                                            .into(img);


                                    txtTitle = findViewById(R.id.titleGame);
                                    txtTitle.setText(mDetailsGamesRellenos.get(0).get("name").toString());

                                    TextView txtRtaing = findViewById(R.id.rating);
                                    txtRtaing.setText(mDetailsGamesRellenos.get(0).get("rating").toString());

                                    TextView txtMetacritic = findViewById(R.id.metacritic);
                                    txtMetacritic.setText(mDetailsGamesRellenos.get(0).get("metacritic").toString());

                                    TextView txtDescription = findViewById(R.id.description);
                                    txtDescription.setText(mDetailsGamesRellenos.get(0).get("description_raw").toString());


                                    TextView txtReleased = findViewById(R.id.released);
                                    txtReleased.setText(mDetailsGamesRellenos.get(0).get("released").toString());

                                    TextView txtWebsite = findViewById(R.id.website);
                                    txtWebsite.setText(mDetailsGamesRellenos.get(0).get("website").toString());

                                    setToolbar();

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                                Log.d("mdetails", mDetailsGamesRellenos.get(0).get("background_image").toString());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // actualizar();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest4.setShouldCache(false);
        queue4.add(stringRequest4);

    }

    public class DetailsGamesAdapter extends BaseAdapter {

        Integer i = 0;
        private Context context;

        public DetailsGamesAdapter(Context context) {
            this.context = context;

        }


        @Override
        public int getCount() {
            return mDetailsGamesRellenos.size();
        }

        @Override
        public Object getItem(int i) {
            return mDetailsGamesRellenos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view1, ViewGroup viewGroup) {


            return view1;
        }
    }

   /* private void leerDatosSPFavs() {
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYTIENDASFAV, "");
        Type founderListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> restoreArray = gson.fromJson(json, founderListType);

        if (restoreArray != null) {
            mGamesFav = restoreArray;

        }
    }

    private void guardarDatoSPFavs() {
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGamesFav);
        prefsEditor.putString(HelperGlobal.ARRAYTIENDASFAV, json);
        prefsEditor.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODINTFAVGAME){
            leerDatosSPFavs();
        }

    }*/


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

    private void guardarDatoSPFavs() {
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGamesFav);
        prefsEditor.putString(HelperGlobal.ARRAYTIENDASFAV, json);
        prefsEditor.commit();
    }

}