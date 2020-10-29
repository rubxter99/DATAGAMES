package com.example.datagames;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datagames.ObjectFilterGame;
import com.google.gson.Gson;

public class FilterGames extends AppCompatActivity {

    private String mRating;
    private Spinner spRating;
    private int mPostRating=0;
    private String mPlatforms;
    private Spinner spPlatform;
    private int mPostPlatform=0;
    private String mGenres;
    private Spinner spGenres;
    private int mPostGenres=0;
    private ObjectFilterGame filtroleidogame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_games);

        leerDatosSP();

        spRating = findViewById(R.id.spRating);
        spRating.setVisibility(View.VISIBLE);
        final ArrayAdapter<CharSequence> adapterRating = ArrayAdapter.createFromResource(FilterGames.this, R.array.sp_rating,
                R.layout.spinner_item);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRating.setAdapter(adapterRating);
        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRating = adapterView.getItemAtPosition(i).toString();
                mPostRating=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spPlatform = findViewById(R.id.spPlatformas);
        spPlatform.setVisibility(View.VISIBLE);
        final ArrayAdapter<CharSequence> adapterPlatform = ArrayAdapter.createFromResource(FilterGames.this, R.array.sp_plataformas,
                R.layout.spinner_item);
        adapterPlatform.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlatform.setAdapter(adapterPlatform);
        spPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlatforms = adapterView.getItemAtPosition(i).toString();
                mPostPlatform=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spGenres = findViewById(R.id.spGeneros);
        spGenres.setVisibility(View.VISIBLE);
        final ArrayAdapter<CharSequence> adapterGenres = ArrayAdapter.createFromResource(FilterGames.this, R.array.sp_generos,
                R.layout.spinner_item);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenres.setAdapter(adapterGenres);
        spGenres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGenres = adapterView.getItemAtPosition(i).toString();
                mPostGenres=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button btn_savefiltros=findViewById(R.id.btn_FILTROS);
        btn_savefiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectFilterGame establecerFiltro = new ObjectFilterGame(mRating,mPostRating,mPlatforms,mPostPlatform,mGenres,mPostGenres);
                guardarDatoSP(establecerFiltro);
                finish();
            }
        });
        if(filtroleidogame !=null){
            if(filtroleidogame.getPosRating()!=0 && filtroleidogame.getPlatform()!="" && filtroleidogame.getGenre()!=""  ){
                spRating.setSelection(filtroleidogame.getPosRating());
                spPlatform.setSelection(filtroleidogame.getPostPlatform());
                spGenres.setSelection(filtroleidogame.getPostGenre());

            }
        }

    }

    private void guardarDatoSP(ObjectFilterGame objetcFiltro){
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(objetcFiltro);
        prefsEditor.putString(HelperGlobal.ARRAYGAMESFILTROS, json);
        prefsEditor.commit();
    }

    private void leerDatosSP(){
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYGAMESFILTROS, "");
        ObjectFilterGame jsonFiltro= gson.fromJson(json, ObjectFilterGame.class);
        if(jsonFiltro!=null){
            filtroleidogame = jsonFiltro;
        }
    }
}

