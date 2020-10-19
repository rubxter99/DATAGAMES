package com.example.datagames;

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


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class GameList extends AppCompatActivity {
    private ListView mLv ;
    private ProgressDialog mPd;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;
    private ArrayList<GamesParse.game> mGames = new ArrayList<>();
    private ArrayList<GamesParse.game> mGamesRellenos = new ArrayList<>();
    private Boolean relleno;
    private GamesAdapter mAdapter = null;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private EditText filtro;
    private ArrayAdapter arrayAdapter;
    private ImageView iconsearch;
    private ScrollView scrollView;
    private List<GamesParse.game> myData;
    private ArrayList<GamesParse.game> mOriginalNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_juegos);

        mPd = new ProgressDialog(GameList.this);
        mPd.setProgressStyle(Spinner.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
        mPd.setTitle(HelperGlobal.PROGRESSTITTLE);
        mPd.setMessage(HelperGlobal.PROGRESSMESSAGE);
        mPd.setProgress(100);
        mPd.show();

        loadGames();

        mAuth= FirebaseAuth.getInstance();
        mLv = findViewById(R.id.list_notify);
        toolbar=findViewById(R.id.toolbar);
        setToolBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navview);
        navigationDrawer();
        scrollView=findViewById(R.id.scrollView6);
        filtro=findViewById(R.id.filtros);
        filtro.addTextChangedListener(searchTextWatcher);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.list_notify).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        mLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

            }
        });

        filtro=findViewById(R.id.filtros);
        //arrayAdapter=new ArrayAdapter<GameList>(this,R.layout.final_lista_juegos,R.id.titleGame,mAdapter);
        //mLv.setAdapter(arrayAdapter);
        /*filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                GameList.this.arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/






       /* LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(HelperGlobal.INTENT_LOCALIZATION_ACTION));*/





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
   private TextWatcher searchTextWatcher = new TextWatcher() {
       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
           // ignore
       }

       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           // ignore
       }

       @Override
       public void afterTextChanged(Editable s) {

           mAdapter.getFilter().filter(s.toString());
       }
   };


    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.menu_toolbar, menu);
       return true;
   }
   private void setToolBar(){
       setSupportActionBar(toolbar);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                //return true;
                break;
            case R.id.toolbar_filtro:
                Intent intent=new Intent(GameList.this,Profile.class);
                startActivity(intent);
                finish();
                break;

        }

            return true;

       // return super.onOptionsItemSelected(item);
    }
    private void navigationDrawer(){
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_home:
                        Intent intent1=new Intent(GameList.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent2=new Intent(GameList.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3=new Intent(GameList.this, MainActivity.class);
                        startActivity(intent3);
                        finish();
                        break;

                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }

   private void loadGames(){

       RequestQueue queue = Volley.newRequestQueue(this);
       String url = HelperGlobal.URLGAMES;
       StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       GamesParse gamesParse = new GamesParse();
                       mGames = gamesParse.parseGame(response);

                       for (int i = 0; i< mGames.size(); i++){
                           relleno = true;
                           if(mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                   mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" ||mGames.get(i).getGenres() == "" ){

                           }else{
                               mGamesRellenos.add(mGames.get(i));
                           }
                       }
                       mPd.dismiss();
                       if(mAdapter==null){
                           mAdapter = new GamesAdapter();
                           mLv.setAdapter(mAdapter);
                       }else{
                           mAdapter.notifyDataSetChanged();
                       }
                      // actualizar();
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       stringRequest.setShouldCache(false);
       queue.add(stringRequest);
   }
    public class GamesAdapter extends  BaseAdapter  implements Filterable {

        Integer i = 0;


        @Override
        public int getCount() {
            return mGames.size();
        }

        @Override
        public Object getItem(int i) {
            return mGames.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = null;

            if (view == view2) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view2 = inflater.inflate(R.layout.final_lista_juegos, null);
            } else {
                view2 = view;
            }

            ImageView img = view2.findViewById(R.id.imageIcon);
            Picasso.get().load(mGames.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView txtTitle = view2.findViewById(R.id.titleGame);
            txtTitle.setText(mGames.get(i).getName());

            TextView txtGenres = view2.findViewById(R.id.genres);
            txtGenres.setText(mGames.get(i).getGenres());

            TextView txtRating = view2.findViewById(R.id.rating);
            txtRating.setText(mGames.get(i).getRating() );

            TextView txtReleased = view2.findViewById(R.id.released);
            txtReleased.setText(mGames.get(i).getReleased());

            return view2;
        }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mGames = (ArrayList<GamesParse.game>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {


                    FilterResults results = new FilterResults();
                    ArrayList<GamesParse.game> FilteredArrayNames = new ArrayList<GamesParse.game>();


                    if(mOriginalNames == null ){
                        mOriginalNames = new ArrayList<GamesParse.game>(mGames);

                    }
                    if(constraint == null || constraint.length() == 0){
                        results.count = mOriginalNames.size();
                        results.values = mOriginalNames;
                    }
                    else{
                        constraint = constraint.toString().toLowerCase();
                        for(int i = 0; i < mOriginalNames.size(); i++){

                            if(mOriginalNames.get(i).getName().toLowerCase().startsWith(constraint.toString())){
                                FilteredArrayNames.add(mOriginalNames.get(i));
                            }
                        }

                        results.count = FilteredArrayNames.size();
                        System.out.println(results.count);

                        results.values = FilteredArrayNames;
                        
                    }

                    return results;
                }
            };
            return filter;
        }

    }

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
