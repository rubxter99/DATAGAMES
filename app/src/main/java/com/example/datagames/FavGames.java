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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavGames extends AppCompatActivity {
    // public static ArrayList<GamesParse.game> mGamesFav = new ArrayList<>();
    public static ArrayList<DetailParse.details> mGamesFav;
    private ListView mLv = null;
    private MyAdapter mAdapter;
    private Location mCurrentLocation = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_games);
        Intent getIntent = getIntent();

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
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(FavGames.this, DetailActivity.class);
                i.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                startActivity(i);
            }
        });
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
}
