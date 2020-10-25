package com.example.datagames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String imageUrl;
    private String mId;
    private String mTitle;
    private String mReleased;
    private String mGenres;
    private Double mRating;
    private ScrollView scrollView;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private JSONObject mDetailsGames = new JSONObject();
    private ArrayList<JSONObject> mDetailsGamesRellenos = new ArrayList<>();
    private DetailsGamesAdapter mDetailAdapter;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        mAuth = FirebaseAuth.getInstance();
        scrollView = findViewById(R.id.scrollView6);
        // Añadir action bar

        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent != null) {

            mId = intent.getStringExtra(HelperGlobal.EXTRA_ID);
            loadGameDetail(mId);


            Log.d("id2:", mId);
            ImageView imagegame = findViewById(R.id.image_paralax);
            TextView titlegame = findViewById(R.id.titleGame);

            TextView releasedgame = findViewById(R.id.released);
            TextView genresgame = findViewById(R.id.genres);

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
            case R.id.action_favorite:

                showSnackBar("Añadir a favoritos");
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

                            } else {
                                Log.d("mdetails1", mDetailsGames.get("name").toString());
                                mDetailsGamesRellenos.add(mDetailsGames);
                                Log.d("mdetails", mDetailsGamesRellenos.get(0).get("name").toString());
                            }


                            try {
                                Log.d("mDetailsAdapter", mDetailsGamesRellenos.get(0).get("name").toString());
                                ImageView img = findViewById(R.id.image_paralax);
                                Picasso.get().load(mDetailsGamesRellenos.get(0).get("background_image").toString()).resize(2048, 1600)
                                        .into(img);


                                txtTitle = findViewById(R.id.titleGame);
                                txtTitle.setText(mDetailsGamesRellenos.get(0).get("name").toString());

                                TextView txtGenres = findViewById(R.id.genres);
                                txtGenres.setText(mDetailsGamesRellenos.get(0).get("description").toString());


                                TextView txtReleased = findViewById(R.id.released);
                                txtReleased.setText(mDetailsGamesRellenos.get(0).get("released").toString());

                                setToolbar();

                            } catch (JSONException e) {
                                e.printStackTrace();
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

}