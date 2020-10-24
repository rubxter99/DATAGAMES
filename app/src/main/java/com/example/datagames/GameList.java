package com.example.datagames;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameList extends AppCompatActivity {
    private ListView mLv ;
    private ListView upcoming;
    private ProgressDialog mPd;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;
    private ArrayList<GamesParse.game> mGames = new ArrayList<>();
    private ArrayList<GamesParse.game> mGamesRellenos = new ArrayList<>();
    private ArrayList<GamesParse.game> mNewGamesRellenos = new ArrayList<>();
    private ArrayList<GamesParse.game> mUpcomingRellenos = new ArrayList<>();
    private Boolean relleno;
    private GamesAdapter mAdapter = null;
    private NewGamesAdapter mNewAdapter=null;
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
    private ListView newgames;
    private UpcomingGamesAdapter mUpcomingGames = null;


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

        loadGames(200);
        loadNewGames();
        loadUpcomingGames();

        mAuth= FirebaseAuth.getInstance();
        mLv = findViewById(R.id.list_notify);
        newgames=findViewById(R.id.recicler);
        upcoming=findViewById(R.id.upcoming);
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
        mLv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 1, 0, HelperGlobal.ABRIRGAME);
                contextMenu.add(0, 2, 0, HelperGlobal.AÑADIRFAV);
            }
        });
        newgames.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v2, MotionEvent event) {
                v2.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        upcoming.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v3, MotionEvent event) {
                v3.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });






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
   /*private void newgames(){
       newgames.setHasFixedSize(true);
       newgames.setLayoutManager(new LinearLayoutManager(this));
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
                       if(mAdapter2==null){
                           mAdapter2 = new NewGamesAdapter();
                          // mLv.setAdapter(mAdapter2);
                       }else{
                           mAdapter2.notifyDataSetChanged();
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

          // mAdapter.getFilter().filter(s.toString());
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

                    case R.id.nav_maps:
                        Intent intent4 =new Intent(GameList.this, MapsActivity.class);
                        startActivity(intent4);
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

   private void loadGames(final int max){
        int page = 0;
       RequestQueue queue = Volley.newRequestQueue(this);
       for ( page=1;page<=max;page++){
           String url = "https://api.rawg.io/api/games?page="+page;
          Log.d("this",url);
           StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           GamesParse gamesParse = new GamesParse();
                           mGames = gamesParse.parseGame(response);
                           relleno = false;
                           for (int i = 0; i < mGames.size(); i++) {
                               if (mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                       mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {

                               }else{
                                   mGamesRellenos.add(mGames.get(i));
                               }
                           }
                           if(mAdapter==null){
                               mAdapter = new GamesAdapter(GameList.this);
                               mLv.setAdapter( mAdapter);
                               mPd.dismiss();
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
   }
    private void loadNewGames(){
        int page = 0;
        RequestQueue queue2 = Volley.newRequestQueue(this);

            String url2 = "https://api.rawg.io/api/games?dates=2020-05-05,2020-10-10";
            Log.d("this",url2);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GamesParse gamesParse = new GamesParse();
                            mGames = gamesParse.parseGame(response);
                            relleno = false;
                            for (int i = 0; i < mGames.size(); i++) {
                                if (mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                        mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {

                                }else{
                                    mNewGamesRellenos.add(mGames.get(i));
                                }
                            }
                            if(mNewAdapter==null){
                                mNewAdapter = new NewGamesAdapter();
                                newgames.setAdapter( mNewAdapter);
                            }else{
                                mNewAdapter.notifyDataSetChanged();
                            }

                            // actualizar();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            stringRequest2.setShouldCache(false);
            queue2.add(stringRequest2);

    }
    private void loadUpcomingGames(){
        int page = 0;
        RequestQueue queue3 = Volley.newRequestQueue(this);

        String url3 = "https://api.rawg.io/api/games?dates=2020-10-10,2021-10-10&ordering=-added";
        Log.d("this",url3);
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GamesParse gamesParse = new GamesParse();
                        mGames = gamesParse.parseGame(response);
                        relleno = false;
                        for (int i = 0; i < mGames.size(); i++) {
                            if (mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                    mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {

                            }else{
                                mUpcomingRellenos.add(mGames.get(i));
                            }
                        }
                        if(mUpcomingGames==null){
                            mUpcomingGames = new UpcomingGamesAdapter();
                            upcoming.setAdapter( mUpcomingGames);
                        }else{
                            mUpcomingGames.notifyDataSetChanged();
                        }

                        // actualizar();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest3.setShouldCache(false);
        queue3.add(stringRequest3);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case 1:
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_NAME, mGamesRellenos.get(info.position).getName());
                intent.putExtra(HelperGlobal.EXTRA_DRAWABLE, mGamesRellenos.get(info.position).getImage());
                intent.putExtra(HelperGlobal.EXTRA_GENRE, mGamesRellenos.get(info.position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_RATING, mGamesRellenos.get(info.position).getRating());
                intent.putExtra(HelperGlobal.EXTRA_RELEASED, mGamesRellenos.get(info.position).getReleased());
                startActivity(intent);

                mAdapter.notifyDataSetChanged();
                break;
            /* case 2:
               boolean encontrado = false;
                GamesParse.game gamesfav = mTiendasFinal.get(info.position);
                if(mTiendasFavorito.size()!=0) {
                    for (int x = 0; x < mTiendasFavorito.size(); x++) {
                        if (mTiendasFavorito.get(x).getName().equalsIgnoreCase(gamesfav.getName())
                                && (mTiendasFavorito.get(x).getIcon().equalsIgnoreCase(gamesfav.getIcon()))) {

                            encontrado = true;
                            break;
                        }
                    }
                }

                if(encontrado){
                    Toast.makeText(ListTiendas.this,
                            HelperGlobal.TIENDAYAFAV, Toast.LENGTH_LONG).show();
                }else{
                    mTiendasFavorito.add(tiendasfav);
                    Toast.makeText(ListTiendas.this,
                            HelperGlobal.AÑADIDOFAV, Toast.LENGTH_LONG).show();
                    guardarDatoSPFavs();
                }
                break;*/
        }
        return true;
    }

    // https://api.rawg.io/api/games?dates=2019-10-10,2020-10-10&ordering=-added
    public class GamesAdapter extends  BaseAdapter  implements Filterable {

        Integer i = 0;
        private  Context context;

        public GamesAdapter(Context context) {
            this.context = context;

        }


        @Override
        public int getCount() {
            return mGamesRellenos.size();
        }

        @Override
        public Object getItem(int i) {
            return mGamesRellenos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = null;

            if (view == view1) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1 = inflater.inflate(R.layout.final_lista_juegos, null);
            } else {
                view1 = view;
            }

            ImageView img = view1.findViewById(R.id.imageIcon);
            Picasso.get().load(mGamesRellenos.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView txtTitle = view1.findViewById(R.id.titleGame);
            txtTitle.setText(mGamesRellenos.get(i).getName());

            TextView txtGenres = view1.findViewById(R.id.genres);
            txtGenres.setText(mGamesRellenos.get(i).getGenres());

            TextView txtRating = view1.findViewById(R.id.rating);
            txtRating.setText(mGamesRellenos.get(i).getRating() );

            TextView txtReleased = view1.findViewById(R.id.released);
            txtReleased.setText(mGamesRellenos.get(i).getReleased());

            return view1;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mGamesRellenos = (ArrayList<GamesParse.game>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {


                    FilterResults results = new FilterResults();
                    ArrayList<GamesParse.game> FilteredArrayNames = new ArrayList<GamesParse.game>();


                    if(mOriginalNames == null ){
                        mOriginalNames = new ArrayList<GamesParse.game>(mGamesRellenos);

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


    /*public  class NewGamesAdapter extends {

        ArrayList<GamesParse.game> newgame;
        View view;
        public NewGamesAdapter(ArrayList<GamesParse.game> newgame,View view){
            this.view=view;
            this.newgame=newgame;
        }

        public NewGamesAdapter() {

        }

        public class newGamesViewHolder extends RecyclerView.ViewHolder{

            ImageView image;
            TextView title,genre,realeased;

            public newGamesViewHolder(@NonNull View itemView) {
                super(itemView);


                image=itemView.findViewById(R.id.imageIcon2);
                title=itemView.findViewById(R.id.titleGame2);
                genre=itemView.findViewById(R.id.genres2);
                realeased=itemView.findViewById(R.id.released2);
            }
        }
        @NonNull
        @Override
        public newGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.newgamescardview,parent,false);
            newGamesViewHolder newGamesViewHolder=new newGamesViewHolder(view);
            return newGamesViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull newGamesViewHolder holder, int position) {
            GamesParse.game gamesAdapter=newgame.get(position);
            Picasso.get().load(newgame.get(position).getImage()).resize(2048, 1600)
                    .into(holder.image);
            holder.title.setText(newgame.get(position).getName());
            holder.genre.setText(newgame.get(position).getGenres());
            holder.realeased.setText(newgame.get(position).getReleased());

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }*/
    public class NewGamesAdapter extends  BaseAdapter  {

        Integer i = 0;


        @Override
        public int getCount() {
            return mNewGamesRellenos.size();
        }

        @Override
        public Object getItem(int i) {
            return mNewGamesRellenos.get(i);
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
                view2 = inflater.inflate(R.layout.newgamescardview, null);
            } else {
                view2 = view;
            }

            ImageView img = view2.findViewById(R.id.imageIcon2);
            Picasso.get().load(mNewGamesRellenos.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView txtTitle = view2.findViewById(R.id.titleGame2);
            txtTitle.setText(mNewGamesRellenos.get(i).getName());

            TextView txtGenres = view2.findViewById(R.id.genres2);
            txtGenres.setText(mNewGamesRellenos.get(i).getGenres());

            TextView txtRating = view2.findViewById(R.id.rating2);
            txtRating.setText(mNewGamesRellenos.get(i).getRating());

            TextView txtReleased = view2.findViewById(R.id.released2);
            txtReleased.setText(mNewGamesRellenos.get(i).getReleased());

            return view2;
        }

    }

    public class UpcomingGamesAdapter extends  BaseAdapter  {

        Integer i = 0;


        @Override
        public int getCount() {
            return mUpcomingRellenos.size();
        }

        @Override
        public Object getItem(int i) {
            return mUpcomingRellenos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view3 = null;

            if (view == view3) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view3 = inflater.inflate(R.layout.upcoming_cardview, null);
            } else {
                view3 = view;
            }

            ImageView img = view3.findViewById(R.id.imageIcon3);
            Picasso.get().load(mUpcomingRellenos.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView txtTitle = view3.findViewById(R.id.titleGame3);
            txtTitle.setText(mUpcomingRellenos.get(i).getName());

            TextView txtGenres = view3.findViewById(R.id.genres3);
            txtGenres.setText(mUpcomingRellenos.get(i).getGenres());

            TextView txtRating = view3.findViewById(R.id.rating3);
            txtRating.setText(mUpcomingRellenos.get(i).getRating());

            TextView txtReleased = view3.findViewById(R.id.released3);
            txtReleased.setText(mUpcomingRellenos.get(i).getReleased());

            return view3;
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
