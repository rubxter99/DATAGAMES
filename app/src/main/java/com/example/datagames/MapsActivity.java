package com.example.datagames;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private DatabaseReference mShops;
    private Marker currentUserLocationMarker;
    private Marker marker;
    private static final int Request_User_Location_Code = 99;
    private static String mTitle;
    private static String name;
    private Location mCurrentLocation;
    private static double mLat;
    private static double mLon;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;
    private LocationManager mLocManager;
    private int mProximityRadius = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private final String TAG = getClass().getSimpleName();
    private Double latitude;
    private Double longitude;
    public FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<PlacesParse> arrayplaces;
    private float distance = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Intent intent = getIntent();
        if (intent != null) { //Tomará valores por defecto
            mTitle = intent.getStringExtra(HelperGlobal.TITLEINPUTTIENDASCERCANAS);
            mLat = intent.getDoubleExtra(HelperGlobal.LATINPUTTIENDASCERCANAS, 0.0);
            mLon = intent.getDoubleExtra(HelperGlobal.LONINPUTTIENDASCERCANAS, 0.0);
            mProximityRadius = intent.getIntExtra(HelperGlobal.RADIUSINPUTTIENDASCERCANAS, 1000);

        }
        mShops = FirebaseDatabase.getInstance().getReference("tiendas"); //Conexión a las tiendas de la base de datos de Firebase
        mShops.push().setValue(marker);// marcadores de las tiendas

        //Añadir la vista del Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton btn_Maps = findViewById(R.id.btn_maps);
        btn_Maps.setOnClickListener(new View.OnClickListener() { //Boton Actualizar
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    mMap.clear();

                }

                onMapReady(mMap);
            }
        });

    }

    private void checkPermissions() { //Pedir permisos de ubicación
        if (Build.VERSION.SDK_INT >= 22) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                checkUserLocationPermision();
            else
                showSettingDialog();
        } else
            showSettingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Comprobar en el log los permisos ,sino se cerrará la actividad
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel, La aplicación se cerrará");
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //Añadir el mapa con los marcadores de ubicación tanto de usuario como de las tiendas
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            checkPermissions();

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(true);
            mShops = FirebaseDatabase.getInstance().getReference();

            mShops.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    PlacesParse placesParse = new PlacesParse();
                    arrayplaces = new ArrayList<PlacesParse>();
                    for (DataSnapshot child : dataSnapshot.child("tiendas").getChildren()) {
                        latitude = (Double) child.child("latitud").getValue(Double.class);
                        longitude = (Double) child.child("longitud").getValue(Double.class);
                        name = child.child("name").getValue(String.class);
                        placesParse.setLat(latitude);
                        placesParse.setLon(longitude);
                        placesParse.setName(name);
                        arrayplaces.add(placesParse);

                        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mCurrentLocation = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//Localizacion actual del usuario

                        for (int j = 0; j < arrayplaces.size(); j++) {

                            Location location = new Location(LocationManager.GPS_PROVIDER);
                            location.setLatitude(arrayplaces.get(j).getLat());
                            location.setLongitude(arrayplaces.get(j).getLon());
                            if (mCurrentLocation != null) {

                                distance = mCurrentLocation.distanceTo(location);//Radio de búsqueda de ubicación actual

                                LatLng latLng2 = new LatLng(arrayplaces.get(j).getLat(), arrayplaces.get(j).getLon());

                                if (arrayplaces.get(j).getName().equalsIgnoreCase("game") && distance < 5000) {
                                    //Añadir los marcadores de las tiendas para game
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng2)
                                            .title(arrayplaces.get(j).getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                                } else if (arrayplaces.get(j).getName().equalsIgnoreCase("cex") && distance < 5000) {
                                    //Añadir los marcadores de las tiendas para cex
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng2)
                                            .title(arrayplaces.get(j).getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                                } else if (arrayplaces.get(j).getName().equalsIgnoreCase("media markt") && distance < 5000) {
                                    //Añadir los marcadores de las tiendas para media markt
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng2)
                                            .title(arrayplaces.get(j).getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                } else if (distance < 5000) {
                                    //Añadir los marcadores de las tiendas restantes
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng2)
                                            .title(arrayplaces.get(j).getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                }
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


    public boolean checkUserLocationPermision() { //Comprobar los permisos del usuario del Manifest
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;

        } else {
            return true;
        }

    }

    /* Método de permiso On Request para verificar si el permiso se ha otorgado o no a Marshmallow Devices */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient(); //Iniciar Cliente de Google
                        }
                        mMap.setMyLocationEnabled(true); //Activar localización del usuario
                    }
                } else {
                    Toast.makeText(this, HelperGlobal.PERMISIONDENIED, Toast.LENGTH_SHORT).show();

                }
                return;

            case 1: {

                // Si la consulta es cancelada,el resultado del array sera vacío
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permiso otorgado por el usuario
                    Toast.makeText(getApplicationContext(), HelperGlobal.GPSPERMISEDGARANTED,
                            Toast.LENGTH_SHORT).show();


                } else {
                    // Permisos denegados
                    Toast.makeText(getApplicationContext(),
                            HelperGlobal.PERMISSIONDENIEDUSER, Toast.LENGTH_SHORT).show();
                }
                return;

            }
        }
    }

    protected synchronized void buildGoogleApiClient() { //Conexión con los servicios de Google
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) { //Actualizar marcadores al cambiar la ubicación del usuario

        mLat = location.getLatitude();
        mLon = location.getLongitude();
        mCurrentLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();

        }

        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(HelperGlobal.MARKEROPTIONSTITLE);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker = mMap.addMarker(markerOptions.position(latLng));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) { //Realizar un seguimiento del estado de conexión del usuario si está conectado
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) { //Realizar un seguimiento del estado de conexión del usuario si está en reposo

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { //Realizar un seguimiento del estado de conexión del usuario si la conexión a fallado

    }

    @SuppressWarnings({"MissingPermission"})
    private void startLocation() { //Comenzar la localización del GPS del usuario
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Pedir permiso al usuario para conocer la ubicación.
        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_GPS_FINE_LOCATION);


        } else {
            Toast.makeText(getApplicationContext(),
                    HelperGlobal.PERMISSIONGRANTEDPAST,
                    Toast.LENGTH_SHORT).show();
            startLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Registrar el receptor de difusión para comprobar el estado del GPS.
    }

    @Override
    protected void onDestroy() {

        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);//No registrar el receptor de difusión
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Ejecutar en la interfaz de usuario
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    private void showSettingDialog() { //Mostrar cuadro de diálogo cuya función es comprobar y proporcionar la activación del GPS  del usuario
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("TAG", "SUCCESS");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("TAG", "RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("TAG", "GPS NOT AVAILABLE");
                        break;
                }
            }
        });
    }

    /* Receptor de difusión para comprobar el estado del GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Si la acción es la ubicación
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Compruebe si el GPS está encendido o apagado
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                } else {
                    //Si el GPS está apagado, muestre el diálogo de ubicación
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    Log.e("About GPS", "GPS is Disabled in your device");
                    finish();
                }

            }
        }
    };


}