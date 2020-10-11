package com.example.datagames;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class GameList extends AppCompatActivity {
    private ListView mLv = null;
    private ProgressDialog mPd;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_juegos);

        pedirPermisos();

        mLv = findViewById(R.id.list_notify);

       /* LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(HelperGlobal.INTENT_LOCALIZATION_ACTION));*/

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i3=new Intent(GameList.this,GameList.class);
                startActivity(i3);
            }
        });
        mLv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 1, 0, HelperGlobal.ABRIRMAPSCONTEXTMENU);
                contextMenu.add(0, 2, 0, HelperGlobal.AÑADIRFAV);
            }
        });

      /*  ImageButton filtroButton = findViewById(R.id.imgBtnFiltros);
        filtroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filtroTienda = new Intent(ListTiendas.this, FiltroTiendas.class);
                startActivityForResult(filtroTienda, CODINTFILTROTIENDA);
            }
        });*/

       /* final ImageButton favoritosButton = findViewById(R.id.imgBtnFavoritos);
        favoritosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favoritosLista = new Intent(ListTiendas.this, FavoritosTiendas.class);
                favoritosLista.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mTiendasFavorito);
                favoritosLista.putExtra(HelperGlobal.LOCATIONLAT, mCurrentLocation.getLatitude());
                favoritosLista.putExtra(HelperGlobal.LOCATIONLONG, mCurrentLocation.getLongitude());
                startActivityForResult(favoritosLista, CODINTFAVORITOTIENDA);
            }
        });
        leerDatosSPFavs();*/
    }

   /* public void startService() {
        mServiceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(mServiceIntent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location posicion = intent.getParcelableExtra(HelperGlobal.KEY_MESSAGE);
            ArrayList<TiendasParse.Tiendas> tiendasCercanas = new ArrayList<>();
            leerDatosSPFavs();
            int cont = 0;
            for(int i = 0; i<mTiendasFavorito.size();i++){
                Location location = new Location("");
                location.setLatitude(mTiendasFavorito.get(i).getLat());
                location.setLongitude(mTiendasFavorito.get(i).getLng());

                double distance = posicion.distanceTo(location);
                mTiendasFavorito.get(i).setDistance(distance);
                if(distance<1000){
                    cont++;
                    tiendasCercanas.add(mTiendasFavorito.get(i));
                }
            }

            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String CHANNEL_ID="my_channel_01";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name="my_channel";
                String Description="This is my channel";
                int importance=NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel=new NotificationChannel(CHANNEL_ID,name,importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }

            Intent cercanas = new Intent(ListTiendas.this, TiendasCercanas.class);
            cercanas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            cercanas.putParcelableArrayListExtra(HelperGlobal.PARCELABLEARRAYNEARBY,tiendasCercanas);
            PendingIntent pendingIntent=PendingIntent.getActivity(ListTiendas.this,0,cercanas,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ListTiendas.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.marv_serv)
                    .setContentTitle(HelperGlobal.NOTIFICATIONNEARBYTITLE)
                    .setContentText("Tienes " + cont + " tiendas favoritas cerca de ti.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(2,builder.build());
        }
    };

    @Override
    protected void onDestroy() {
        if (mLocManager != null) {
            mLocManager.removeUpdates(this);
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/
    private void pedirPermisos(){
        // Ask user permission for location.
        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(GameList.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(GameList.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_GPS_FINE_LOCATION);

        } else {
            mPd = new ProgressDialog(GameList.this);
            mPd.setProgressStyle(Spinner.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
            mPd.setTitle(HelperGlobal.PROGRESSTITTLELIBRARIES);
            mPd.setMessage(HelperGlobal.PROGRESSMESSAGE);
            mPd.setProgress(100);
            mPd.show();


        }
    }
}
