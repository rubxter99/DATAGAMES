package com.example.datagames;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.SearchView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        loadNewGames();
        loadUpcomingGames();
        loadGames(200);


        mAuth = FirebaseAuth.getInstance();
        mLv = findViewById(R.id.list_notify);
        newgames = findViewById(R.id.recicler);
        upcoming = findViewById(R.id.upcoming);
        toolbar = findViewById(R.id.toolbar);
        setToolBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        navigationDrawer();
        scrollView = findViewById(R.id.scrollView6);



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
                contextMenu.add(0, 2, 0, HelperGlobal.AÑADIRFAV);
            }
        });
        newgames.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 2, 0, HelperGlobal.AÑADIRFAV);
            }
        });
        upcoming.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu,
                                            View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 2, 0, HelperGlobal.AÑADIRFAV);
            }
        });


        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID,mGamesRellenos.get(position).getId());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT,mGamesRellenos.get(position).getShort_screenshots());
                intent.putExtra(HelperGlobal.EXTRA_GENRE,mGamesRellenos.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP,mGamesRellenos.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mGamesRellenos.get(position).getStore());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mGamesRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM,mGamesRellenos.get(position).getPlatforms());

                startActivity(intent);

                mAdapter.notifyDataSetChanged();
            }
        });
        newgames.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v2, MotionEvent event) {
                v2.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        newgames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID,mNewGamesRellenos.get(position).getId());
                Log.d("mrellenos",mNewGamesRellenos.get(position).getShort_screenshots());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT,mNewGamesRellenos.get(position).getShort_screenshots());
                intent.putExtra(HelperGlobal.EXTRA_GENRE,mNewGamesRellenos.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP,mNewGamesRellenos.get(position).getClip());
                Log.d("holaaaa1",mNewGamesRellenos.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mNewGamesRellenos.get(position).getStore());
                Log.d("galery",mNewGamesRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mNewGamesRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM,mNewGamesRellenos.get(position).getPlatforms());
                startActivity(intent);

                mNewAdapter.notifyDataSetChanged();
            }
        });
        upcoming.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v3, MotionEvent event) {
                v3.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        upcoming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_ID,mUpcomingRellenos.get(position).getId());
                intent.putExtra(HelperGlobal.EXTRA_SHORTSCREENSHOT,mUpcomingRellenos.get(position).getShort_screenshots());
                intent.putExtra(HelperGlobal.EXTRA_GENRE,mUpcomingRellenos.get(position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_CLIP,mUpcomingRellenos.get(position).getClip());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mUpcomingRellenos.get(position).getStore());
                intent.putExtra(HelperGlobal.EXTRA_STORE,mUpcomingRellenos.get(position).getUrlstore());
                intent.putExtra(HelperGlobal.EXTRA_PLATFORM,mUpcomingRellenos.get(position).getPlatforms());
                startActivity(intent);

                mUpcomingGames.notifyDataSetChanged();
            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.toolbar_search).getActionView();

       /* int searchHintBtnId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_mag_icon", null, null);

        int id =  searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);


        int closeBtnId = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);

        ImageView closeIcon = (ImageView) searchView.findViewById(closeBtnId);
        closeIcon.setColorFilter(Color.BLACK);

        ImageView searchHintIcon = (ImageView) searchView.findViewById(searchHintBtnId);
        searchHintIcon.setColorFilter(Color.BLACK);

        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        textView.setHintTextColor(Color.BLUE);
*/


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
           StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           GamesParse gamesParse = new GamesParse();
                           mGames = gamesParse.parseGame(response);
                           relleno = false;
                           for (int i = 0; i < mGames.size(); i++) {
                               if (mGames.get(i).getId()==""||mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
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

            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GamesParse gamesParse = new GamesParse();
                            mGames = gamesParse.parseGame(response);
                            relleno = false;
                            for (int i = 0; i < mGames.size(); i++) {
                                if (mGames.get(i).getId()==""||mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
                                        mGames.get(i).getRating().contentEquals("0") || mGames.get(i).getReleased() == "null" || mGames.get(i).getGenres() == "") {

                                }else{
                                    mNewGamesRellenos.add(mGames.get(i));
                                    Log.d("holaaaa1",mNewGamesRellenos.get(i).getClip());
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

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(date));
        String url3 = "https://api.rawg.io/api/games?dates="+formatter.format(date)+",2023-10-10&ordering=-added&page_size=40";
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GamesParse gamesParse = new GamesParse();
                        mGames = gamesParse.parseGame(response);
                        relleno = false;
                        for (int i = 0; i < mGames.size(); i++) {
                            if (mGames.get(i).getId()==""||mGames.get(i).getName() == "" || mGames.get(i).getImage() == "" ||
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

           /* case 1:
                Intent intent = new Intent(GameList.this, DetailActivity.class);
                intent.putExtra(HelperGlobal.EXTRA_NAME, mGamesRellenos.get(info.position).getName());
                intent.putExtra(HelperGlobal.EXTRA_DRAWABLE, mGamesRellenos.get(info.position).getImage());
                intent.putExtra(HelperGlobal.EXTRA_GENRE, mGamesRellenos.get(info.position).getGenres());
                intent.putExtra(HelperGlobal.EXTRA_RATING, mGamesRellenos.get(info.position).getRating());
                intent.putExtra(HelperGlobal.EXTRA_RELEASED, mGamesRellenos.get(info.position).getReleased());
                startActivity(intent);

                mAdapter.notifyDataSetChanged();
                break;
             case 2:
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
                    ArrayList<GamesParse.game> FilteredGames = new ArrayList<GamesParse.game>();


                    if(mOriginalNames  == null ){
                        mOriginalNames  = new ArrayList<GamesParse.game>(mGamesRellenos);

                    }
                    if(constraint == null || constraint.length() == 0){
                        results.count = mOriginalNames .size();
                        results.values = mOriginalNames ;
                    }
                    else{
                        constraint = constraint.toString().toLowerCase();
                        for(int i = 0; i < mOriginalNames .size(); i++){

                            if(mOriginalNames .get(i).getName().toLowerCase().startsWith(constraint.toString())){
                                FilteredGames.add(mOriginalNames .get(i));
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
