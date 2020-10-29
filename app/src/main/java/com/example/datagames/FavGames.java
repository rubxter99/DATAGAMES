package com.example.datagames;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavGames extends AppCompatActivity {
    public static ArrayList<String> mGamesFav = new ArrayList<String>();
    private ListView mLv = null;
    private MyAdapter mAdapter;
    private Location mCurrentLocation = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_games);
        Intent getIntent = getIntent();

        ArrayList<String> gamesIntent = getIntent.getStringArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY);
        mLv = findViewById(R.id.list_fav);
        mGamesFav = new ArrayList<>();
        for(int i = 0; i<gamesIntent.size();i++){
            mGamesFav.add(gamesIntent.get(i));
            Log.d("games",mGamesFav.get(i));

        }



        mAdapter = new MyAdapter();
        mLv.setAdapter(mAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i3=new Intent(FavGames.this,DetailActivity.class);
                i3.putExtra(HelperGlobal.EXTRA_DRAWABLE, mGamesFav.get(0).toString());
                i3.putExtra(HelperGlobal.EXTRA_NAME, mGamesFav.get(1));
                i3.putExtra(HelperGlobal.EXTRA_RATING, mGamesFav.get(2));
                i3.putExtra(HelperGlobal.EXTRA_GENRE, mGamesFav.get(3));
                i3.putExtra(HelperGlobal.EXTRA_RELEASED, mGamesFav.get(4));
                startActivity(i3);
            }
        });

        mLv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {

                contextMenu.add(0, 2, 0, HelperGlobal.ELIMINARFAVCONTEXTMENU);
            }
        });
    }

    private void guardarDatoSP(){
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGamesFav);
        prefsEditor.putString(HelperGlobal.ARRAYTIENDASFAV, json);
        prefsEditor.commit();
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
            Picasso.get().load(mGamesFav.get(1).toString()).resize(2048, 1600)
                    .into(img);

            TextView tTitle = myview.findViewById(R.id.title);
            tTitle.setText(mGamesFav.get(0).toString());

            TextView txtRtaing =  myview.findViewById(R.id.rating);
            txtRtaing.setText(mGamesFav.get(2).toString());

            TextView txtGenres =  myview.findViewById(R.id.genress);
            txtGenres.setText(mGamesFav.get(3).toString());

            TextView txtReleased =  myview.findViewById(R.id.releasedd);
            txtReleased.setText(mGamesFav.get(4).toString());

            return myview;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case 1:
                mGamesFav.remove(info.position);
                Toast.makeText(FavGames.this,HelperGlobal.ELIMINADOFAV, Toast.LENGTH_SHORT).show();
                guardarDatoSP();
                mAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }
}
