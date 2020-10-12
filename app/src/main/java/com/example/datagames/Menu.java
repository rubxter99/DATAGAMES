package com.example.datagames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.firebase.auth.ActionCodeUrl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Menu extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;
    private LocationManager mLocManager;
    private DatabaseReference mDatabase;
    private CardView cdPerfil;
    private CardView cdMapas;
    private CardView cdAcercade;
    private final String TAG = getClass().getSimpleName();
    private Location mCurrentLocation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth= FirebaseAuth.getInstance();

        //Click en el botón de perfil
        cdPerfil=findViewById(R.id.cd_perfil);
        cdPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser();
                startActivity(new Intent(Menu.this,Profile.class));

            }
        });

        //Click en el botón de mapas
        cdMapas = findViewById(R.id.cd_mapas);
        cdMapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MapsActivity.class);
                /*intent.putExtra("TITLE", mResults.get(i).title);
                intent.putExtra("LAT", mResults.get(i).location.latitude);
                intent.putExtra("LON", mResults.get(i).location.longitude);*/
                startActivity(intent);
            }
        });

        //Click en el botón de acerca de
        cdAcercade = findViewById(R.id.cd_acercade);
        cdAcercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this,About.class));
            }
        });
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
    }

    //Permisos para activar la localización
    @Override
    protected void onStart() {
        super.onStart();

        // Ask user permission for location.
        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(Menu.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(Menu.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_GPS_FINE_LOCATION);

        } else {
            Toast.makeText(getApplicationContext(),
                    "[LOCATION] Permission granted in the past!",
                    Toast.LENGTH_SHORT).show();

            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission granted by user
                    Toast.makeText(getApplicationContext(), "GPS Permission granted!",
                            Toast.LENGTH_SHORT).show();
                    startLocation();

                } else {
                    // permission denied
                    Toast.makeText(getApplicationContext(),
                            "Permission denied by user!", Toast.LENGTH_SHORT).show();
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public class MyAdapter extends BaseAdapter{

        private Context mContext;


        public MyAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }


    }


    @SuppressWarnings({"MissingPermission"})
    private void startLocation(){
        mLocManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (! mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
        }
        else {

          //  mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 300,this);
        }
    }


    @SuppressWarnings({"MissingPermission"})
    public void onLocationChanged(Location location) {

        Log.d(TAG, "New Location: " +
                location.getLatitude() + ", " +
                location.getLongitude() + "," +
                location.getAltitude());

        mCurrentLocation = location;

    }
    */
}