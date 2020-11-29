package com.example.datagames;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.datagames.DetailActivity.mGamesFav;

public class FavGames extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static ArrayList<DetailParse.details> mGamesFav;
    private ListView mLv = null;
    private MyAdapter mAdapter;
    private Location mCurrentLocation = new Location("");
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_games);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);

        Intent getIntent = getIntent();
        mAuth= FirebaseAuth.getInstance();
        //ArrayList<GamesParse.game> gamesIntent = getIntent.getParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY);
        ArrayList<DetailParse.details> gamesIntent = getIntent.getParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY);
        mLv = findViewById(R.id.list_fav);
        mGamesFav = new ArrayList<>();

        for (int i = 0; i < gamesIntent.size(); i++) {

            mGamesFav.add(gamesIntent.get(i));
            Log.d("fav2", mGamesFav.toString());
        }

        mAdapter = new MyAdapter();
        mLv.setAdapter(mAdapter);
        setToolBar();
        navigationDrawer();
       /* mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i3=new Intent(FavGames.this,DetailActivity.class);
                i3.putExtra(HelperGlobal.EXTRA_DRAWABLE, mGamesFav.get(position).getName());
                i3.putExtra(HelperGlobal.EXTRA_NAME, mGamesFav.get(position).getImage());
                i3.putExtra(HelperGlobal.EXTRA_RATING,mGamesFav.get(position).getRating());
                i3.putExtra(HelperGlobal.EXTRA_GENRE,mGamesFav.get(position).getGenres());
                i3.putExtra(HelperGlobal.EXTRA_RELEASED, mGamesFav.get(position).getReleased());
                startActivity(i3);
            }
        });*/


        mLv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {

                contextMenu.add(0, 1, 0, HelperGlobal.ELIMINARFAVCONTEXTMENU);
            }
        });
    }

    private void guardarDatoSP() {
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGamesFav);

        prefsEditor.putString(HelperGlobal.ARRAYTIENDASFAV, json);
        prefsEditor.apply();
    }



    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mGamesFav.size();
        }

        @Override
        public Object getItem(int i) {
            return mGamesFav.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View myview = null;

            if (myview == null) {

                LayoutInflater inflater = (LayoutInflater) FavGames.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                myview = inflater.inflate(R.layout.list_fav_games, null);
            } else
                myview = view;

            ImageView img = myview.findViewById(R.id.imageIcon);
            Picasso.get().load(mGamesFav.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView tTitle = myview.findViewById(R.id.title);
            tTitle.setText(mGamesFav.get(i).getName());

            TextView txtRtaing = myview.findViewById(R.id.rating);
            txtRtaing.setText(mGamesFav.get(i).getRating());

            TextView txtGenres = myview.findViewById(R.id.genress);
            txtGenres.setText(mGamesFav.get(i).getGenres());

            TextView txtReleased = myview.findViewById(R.id.releasedd);
            txtReleased.setText(mGamesFav.get(i).getReleased());

            return myview;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case 1:
                mGamesFav.remove(info.position);
                Toast.makeText(FavGames.this, HelperGlobal.ELIMINADOFAV, Toast.LENGTH_SHORT).show();
                guardarDatoSP();
                mAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }
    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void navigationDrawer() {

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent intent1 = new Intent(FavGames.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(FavGames.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;


                    case R.id.nav_profile:
                        Intent intent2 = new Intent(FavGames.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(FavGames.this, MainActivity.class);
                        startActivity(intent3);
                        finish();
                        break;

                    case R.id.nav_shops:
                        Intent intent5 = new Intent(FavGames.this, Shops.class);
                        startActivity(intent5);
                        finish();
                        break;
                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }

}
