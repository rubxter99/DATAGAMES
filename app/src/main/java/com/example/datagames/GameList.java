package com.example.datagames;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.datagames.DetailActivity.mGamesFav;

public class GameList extends AppCompatActivity {
    private ListView mLv;
    private ListView upcoming;
    private ProgressDialog mPd;
    private static final Integer MY_PERMISSIONS_GPS_FINE_LOCATION = 1;
    private ArrayList<GamesParse.game> mGames = new ArrayList<>();
    private ArrayList<GamesParse.game> mGamesRellenos = new ArrayList<>();
    private ArrayList<GamesParse.game> mNewGamesRellenos = new ArrayList<>();
    private ArrayList<GamesParse.game> mUpcomingRellenos = new ArrayList<>();
    private ArrayList<GamesParse.game> mGamesFiltrados = new ArrayList<>();
    private Boolean relleno;
    private GamesAdapter mAdapter = null;
    private NewGamesAdapter mNewAdapter = null;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private ScrollView scrollView;
    private ArrayList<GamesParse.game> mOriginalNames;
    private ListView newgames;
    private UpcomingGamesAdapter mUpcomingGames = null;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private final int CODINFILTROGAME = 0;
    private static final int CODINTFAVGAME = 1;
    private ObjectFilterGame mFiltroGame = null;
    private FirebaseUser firebaseUser;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditor2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);

        //Ejecución del hilo cuya función es mostrar un cuadro de diálogo a la espera de cargar las listas de videojuegos
        new MyTask().execute();
        //Conexión a la base de datos de Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recycler_view);
        mLv = findViewById(R.id.list_notify);
        newgames = findViewById(R.id.recicler);
        upcoming = findViewById(R.id.upcoming);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        scrollView = findViewById(R.id.scrollView);

        //Mostrar el listview inicial en horizontal
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GameList.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Mostrar menú superior
        setToolBar();
        navigationDrawer();


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.list_notify).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mostrarListado();


    }

    private void mostrarListado() { //Mostrara las listas de videojuegos genérica,los actuales y los próximos
        mLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { //Lista de videojuegos genéricos
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

            }
        });
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID, mGamesFiltrados.get(position).getId());
                intent.putExtra(HelperGlobal.EXTRA_GENRE, mGamesFiltrados.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP, mGamesFiltrados.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORENAME, mGamesFiltrados.get(position).getStorename());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT, mGamesFiltrados.get(position).getShort_screenshotsimage());
                intent.putExtra(HelperGlobal.EXTRA_STORE, mGamesFiltrados.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM, mGamesFiltrados.get(position).getPlatforms());

                startActivity(intent);

                mAdapter.notifyDataSetChanged();
            }
        });
        newgames.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v2, MotionEvent event) { //Lista de videojuegos actuales
                v2.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        newgames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID, mNewGamesRellenos.get(position).getId());
                intent.putExtra(HelperGlobal.EXTRA_GENRE, mNewGamesRellenos.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP, mNewGamesRellenos.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORENAME, mNewGamesRellenos.get(position).getStorename());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT, mNewGamesRellenos.get(position).getShort_screenshotsimage());
                intent.putExtra(HelperGlobal.EXTRA_STORE, mNewGamesRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM, mNewGamesRellenos.get(position).getPlatforms());
                startActivity(intent);

                mNewAdapter.notifyDataSetChanged();
            }
        });
        upcoming.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v3, MotionEvent event) { //Lista de videojuegos próximos
                v3.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        upcoming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID, mUpcomingRellenos.get(position).getId());
                intent.putExtra(HelperGlobal.EXTRA_GENRE, mUpcomingRellenos.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP, mUpcomingRellenos.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORENAME, mUpcomingRellenos.get(position).getStorename());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT, mUpcomingRellenos.get(position).getShort_screenshotsimage());
                intent.putExtra(HelperGlobal.EXTRA_STORE, mUpcomingRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM, mUpcomingRellenos.get(position).getPlatforms());
                startActivity(intent);

                mUpcomingGames.notifyDataSetChanged();
            }
        });

    }

    public class MyTask extends AsyncTask<Void, Void, Void> { //Hilo de ejecución para el cuadro de diálogo mientras cargan las listas de los videojuegos

        public void onPreExecute() { //Al comienzo de la aplicación
            mPd = new ProgressDialog(GameList.this);
            mPd.setProgressStyle(Spinner.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
            mPd.setTitle(HelperGlobal.PROGRESSTITTLE);
            mPd.setMessage(HelperGlobal.PROGRESSMESSAGE);
            mPd.setProgress(10);
            mPd.setMax(100);
            mPd.show();
            loadGames(200); //Cargar Lista genérica de mas de 200 páginas con 50 videojuegos cada una
            loadNewGames(); //Cargar Lista Videojuegos actuales con 50 de ellos
            loadUpcomingGames(); //Cargar Lista Videojuegos próximos con 50 de ellos

        }

        public Void doInBackground(Void... unused) { //Mientras se esta ejecutando el cuadro de diálogo espere 20 segundos

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { //Al terminar de cargar el método anterior se cerrará el cuadro de diálogo

            mPd.dismiss();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) { //Mostrar botón de búsqueda en el menú superior y aplicarle la función de filtrado
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.toolbar_search).getActionView();

            searchView.setBackgroundColor(Color.BLACK);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setToolBar() { //Creación del menú superior junto con el menú deslizante ,búsqueda y filtros
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Mostrar menú deslizante al seleccionar el botón de tres lineas del menú superior y la aplicación de los filtros
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                break;
            case R.id.toolbar_filtros:

                Intent filtroGame = new Intent(GameList.this, FilterGames.class);
                startActivityForResult(filtroGame, CODINFILTROGAME);
                break;

        }

        return true;

    }

    private void navigationDrawer() {

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { //Mostrar los apartados del menú deslizante con sus respectivas direcciones a cada ventana

                switch (item.getItemId()) {

                    case R.id.nav_home:
                        Intent intent1 = new Intent(GameList.this, com.example.datagames.Menu.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.nav_maps:
                        Intent intent4 = new Intent(GameList.this, MapsActivity.class);
                        startActivity(intent4);
                        finish();
                        break;
                    case R.id.nav_fav:
                        Intent favGames = new Intent(GameList.this, FavGames.class);
                        favGames.putParcelableArrayListExtra(HelperGlobal.PARCELABLEKEYARRAY, mGamesFav);
                        startActivityForResult(favGames, CODINTFAVGAME);
                        leerDatosSPFavs();
                        finish();
                        break;

                    case R.id.nav_profile:
                        Intent intent2 = new Intent(GameList.this, Profile.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent3 = new Intent(GameList.this, MainActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Cierra todas las actividades anteriores
                        startActivity(intent3);
                        restaurar();
                        finish();
                        break;
                    case R.id.nav_shops:
                        Intent intent5 = new Intent(GameList.this, Shops.class);
                        startActivity(intent5);
                        finish();
                        break;

                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);

    }


    private void loadGames(int max) { //Cargar Listado Genérico de los videojuegos con un máximo de páginas en nuestro caso 200

        RequestQueue queue = Volley.newRequestQueue(this);
        for (int page = 1; page < max; page++) {
            String url = "https://api.rawg.io/api/games?page_size=50&page=" + page; //Consulta a RAWG API
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {//Consulta a RAWG API
                            GamesParse gamesParse = new GamesParse();
                            mGames = gamesParse.parseGame(response);

                            for (int i = 0; i < mGames.size(); i++) {

                                if (mGames.get(i).getId() == "" || mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                        mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {

                                } else {
                                    mGamesRellenos.add(mGames.get(i));
                                    relleno = true;
                                }

                            }
                            actualizar();
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

    private void loadNewGames() { //Cargar Listado Actual de los nuevos videojuegos siendo en nuestro caso 40 de ellos desde nuestra fecha actual

        RequestQueue queue2 = Volley.newRequestQueue(this);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String url2 = "https://api.rawg.io/api/games?dates=2020-09-09," + formatter.format(date) + "&page_size=40";//Consulta a RAWG API
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//Consulta a RAWG API
                        GamesParse gamesParse = new GamesParse();
                        mGames = gamesParse.parseGame(response);
                        relleno = false;
                        for (int i = 0; i < mGames.size(); i++) {
                            if (mGames.get(i).getId() == "" || mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                    mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "" || mGames.get(i).getGenres() == "") {
                            } else {
                                mNewGamesRellenos.add(mGames.get(i));
                            }
                        }
                        if (mainAdapter == null) {
                            mainAdapter = new MainAdapter(GameList.this, mNewGamesRellenos);
                            recyclerView.setAdapter(mainAdapter);

                        } else {
                            mainAdapter.notifyDataSetChanged();
                        }
                        if (mNewAdapter == null) {
                            mNewAdapter = new NewGamesAdapter(GameList.this);
                            newgames.setAdapter(mNewAdapter);
                        } else {
                            mNewAdapter.notifyDataSetChanged();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest2.setShouldCache(false);
        queue2.add(stringRequest2);

    }

    private void loadUpcomingGames() { //Cargar Listado Próximo de los próximos videojuegos siendo en nuestro caso 40 de ellos desde nuestra fecha actual hasta finales del 2025

        RequestQueue queue3 = Volley.newRequestQueue(this);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String url3 = "https://api.rawg.io/api/games?dates=" + formatter.format(date) + ",2025-10-10&ordering=-added&page_size=40";//Consulta a RAWG API
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//Consulta a RAWG API
                        GamesParse gamesParse = new GamesParse();
                        mGames = gamesParse.parseGame(response);
                        relleno = false;
                        for (int i = 0; i < mGames.size(); i++) {
                            if (mGames.get(i).getId() == "" || mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                    mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {
                            } else {
                                mUpcomingRellenos.add(mGames.get(i));
                            }
                        }
                        if (mUpcomingGames == null) {
                            mUpcomingGames = new UpcomingGamesAdapter(GameList.this);
                            upcoming.setAdapter(mUpcomingGames);
                        } else {
                            mUpcomingGames.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest3.setShouldCache(false);
        queue3.add(stringRequest3);
    }


    public class MainAdapter extends RecyclerView.Adapter<GameList.ViewHolder> { //Adaptador para mostrar el listado de videojuegos en horizontal en nuestro caso será de los nuevos videojuegos

        ArrayList<GamesParse.game> mNewGamesRellenos;
        Context context;

        public MainAdapter(Context context, ArrayList<GamesParse.game> mNewGamesRellenos) { //Constructor
            this.context = context;
            this.mNewGamesRellenos = mNewGamesRellenos;
        }

        @NonNull
        @Override
        public GameList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Creación de la vista de la lista horizontal
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_horizontal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameList.ViewHolder holder, final int position) { //Contenido del listado de videojuegos ,tendrá una imagen y al mantenerlo presionado nos llevará a la actividad Detalles
            Picasso.get().load(mNewGamesRellenos.get(position).getImage()).resize(2048, 1600)
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GameList.this, DetailActivity.class);
                    intent.putExtra(HelperGlobal.EXTRA_ID, mNewGamesRellenos.get(position).getId());
                    intent.putExtra(HelperGlobal.EXTRA_GENRE, mNewGamesRellenos.get(position).getGenres());
                    intent.putExtra(HelperGlobal.EXTRA_CLIP, mNewGamesRellenos.get(position).getClip());
                    intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT, mNewGamesRellenos.get(position).getShort_screenshotsimage());
                    intent.putExtra(HelperGlobal.EXTRA_STORENAME, mNewGamesRellenos.get(position).getStorename());
                    intent.putExtra(HelperGlobal.EXTRA_STORE, mNewGamesRellenos.get(position).getUrlstore());
                    intent.putExtra(HelperGlobal.EXTRA_PLATFORM, mNewGamesRellenos.get(position).getPlatforms());
                    startActivity(intent);

                    mainAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() { //Contador de tamaño de la lista
            return mNewGamesRellenos.size();
        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder { //Clase para almacenar la vista de la imagen de cada videojuego actual en la lista horizontal
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);

        }

    }

    public class GamesAdapter extends BaseAdapter implements Filterable { //Adaptador para mostrar el listado de videojuegos en nuestro caso sera de los videojuegos genéricos con 200 páginas

        private Context context;

        public GamesAdapter(Context context) { //Constructor
            this.context = context;

        }

        @Override
        public int getCount() { //Contador de tamaño de la lista
            return mGamesFiltrados.size();
        }

        @Override
        public Object getItem(int i) { //Obtención del objeto
            return mGamesFiltrados.get(i);
        }

        @Override
        public long getItemId(int i) { //Obtener id
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) { //Mostrar la vista de la lista de videojuegos genérica  ya filtrados por búsqueda
            View view1 = null;

            if (view == view1) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view1 = inflater.inflate(R.layout.games_cardview, null);
            } else {
                view1 = view;
            }

            ImageView img = view1.findViewById(R.id.imageIcon);
            Picasso.get().load(mGamesFiltrados.get(i).getImage()).resize(2048, 1600)
                    .into(img);

            TextView txtTitle = view1.findViewById(R.id.titleGame);
            txtTitle.setText(mGamesFiltrados.get(i).getName());

            TextView txtGenres = view1.findViewById(R.id.genres);
            txtGenres.setText(mGamesFiltrados.get(i).getGenres());

            TextView txtRating = view1.findViewById(R.id.rating);
            txtRating.setText(mGamesFiltrados.get(i).getRating());

            TextView txtReleased = view1.findViewById(R.id.released);
            txtReleased.setText(mGamesFiltrados.get(i).getReleased());

            return view1;
        }

        @Override
        public Filter getFilter() { //Método filtrar por búsqueda de la lista de videojuegos genérica
            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mGamesFiltrados = (ArrayList<GamesParse.game>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {


                    FilterResults results = new FilterResults();
                    ArrayList<GamesParse.game> FilteredGames = new ArrayList<GamesParse.game>();


                    if (mOriginalNames == null) {
                        mOriginalNames = new ArrayList<GamesParse.game>(mGamesFiltrados);

                    }
                    if (constraint == null || constraint.length() == 0) {
                        results.count = mOriginalNames.size();
                        results.values = mOriginalNames;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalNames.size(); i++) {

                            if (mOriginalNames.get(i).getName().toLowerCase().startsWith(constraint.toString())) {
                                FilteredGames.add(mOriginalNames.get(i));
                            }
                        }

                        results.count = FilteredGames.size();
                        System.out.println(results.count);
                        results.values = FilteredGames;

                    }

                    return results;
                }
            };
            return filter;
        }


    }


    public class NewGamesAdapter extends BaseAdapter { //Adaptador para mostrar el listado de videojuegos en nuestro caso será de los videojuegos actuales/nuevos

        private Context context;

        public NewGamesAdapter(Context context) { //Constructor
            this.context = context;

        }
        @Override
        public int getCount() { //Contador de tamaño de la lista
            return mNewGamesRellenos.size();
        }

        @Override
        public Object getItem(int i) { //Obtención del objeto
            return mNewGamesRellenos.get(i);
        }

        @Override
        public long getItemId(int i) { //Obtener id
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) { //Mostrar la vista de la lista de videojuegos actuales
            View view2 = null;

            if (view == view2) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view2 = inflater.inflate(R.layout.newgames_cardview, null);
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

    public class UpcomingGamesAdapter extends BaseAdapter { //Adaptador para mostrar el listado de videojuegos en nuestro caso será de los videojuegos próximos

        private Context context;

        public UpcomingGamesAdapter(Context context) { //Constructor
            this.context = context;

        }
        @Override
        public int getCount() { //Contador de tamaño de la lista
            return mUpcomingRellenos.size();
        }

        @Override
        public Object getItem(int i) { //Obtención del objeto
            return mUpcomingRellenos.get(i);
        }

        @Override
        public long getItemId(int i) { //Obtener id
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) { //Mostrar la vista de la lista de videojuegos próximos
            View view3 = null;

            if (view == view3) {
                LayoutInflater inflater = (LayoutInflater) GameList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view3 = inflater.inflate(R.layout.upcoming_games_cardview, null);
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //Al comienzo de la llamada a la actividad se llevará el listado de videojuegos genérico ya filtrado junto con los datos del sharedpreferences de los videojuegos favoritos
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODINFILTROGAME) {
            actualizar();
        } else if (requestCode == CODINTFAVGAME) {
            leerDatosSPFavs();
        }
    }

    private void actualizar() { //Método que sirve para comprobar el listado genérico de los videojuegos de 200 páginas y eliminar o añadir videojuegos dependiendo  de la indicación de filtrado
        leerDatosSPFiltro();
        mGamesFiltrados = new ArrayList<>();
        if (mFiltroGame != null) {

            for (int i = 0; i < mGamesRellenos.size(); i++) {
                mGamesFiltrados.add(mGamesRellenos.get(i));

            }

            String datosRating[] = mFiltroGame.getRating().split(" ");
            String datosPlatform[] = mFiltroGame.getPlatform().split("  ");
            String datosGenres[] = mFiltroGame.getGenre().split("  ");

            for (int i = 0; i < mGamesFiltrados.size(); i++) {

                if (Double.parseDouble(mGamesFiltrados.get(i).getRating()) > Double.parseDouble(datosRating[0]) || !mGamesFiltrados.get(i).getPlatforms().toString().equalsIgnoreCase(datosPlatform[0]) || !mGamesFiltrados.get(i).getGenres().equalsIgnoreCase(datosGenres[0])) {
                    mGamesFiltrados.remove(i); //Eliminar los videojuegos comparados con la elección realizada en la actividad de Filtros
                    i--;
                }
            }
        } else {
            mGamesFiltrados = mGamesRellenos; //Sino los añadirá
        }

        if (mAdapter == null) {
            mAdapter = new GamesAdapter(GameList.this);
            mLv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void leerDatosSPFiltro() {  //Llama a la recogida de datos del sharedpreferences en nuestro caso los filtros de la actividad Filtros para mostrarlos en esta misma actividad sobre la lista genérica con 200 páginas
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYGAMESFILTROS, "");
        ObjectFilterGame jsonFiltro = gson.fromJson(json, ObjectFilterGame.class);
        if (jsonFiltro != null) {
            mFiltroGame = jsonFiltro;

        }
    }


    private void leerDatosSPFavs() {  //Llama a la recogida de datos del sharedpreferences en nuestro caso los videojuegos favoritos para mostrarlos en la actividad de favoritos
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(HelperGlobal.ARRAYTIENDASFAV, "");
        Type founderListType = new TypeToken<ArrayList<DetailParse.details>>() {
        }.getType();
        ArrayList<DetailParse.details> restoreArray = gson.fromJson(json, founderListType);

        if (restoreArray != null) {
            mGamesFav = restoreArray;

        }
    }
    private void restaurar() { //Eliminar los filtros guardados junto con el sharedpreferences y marcarlo por defecto
        SharedPreferences mPrefs = getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences mPrefs2 = getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES, MODE_PRIVATE);
        prefsEditor2 = mPrefs.edit();
        prefsEditor2.clear();
        prefsEditor2.commit();
    }


}
