package com.example.datagames;

import com.google.firebase.auth.FirebaseAuth;

public class HelperGlobal { //Clase para globalizar todas las variables generales llamadas desde distintas actividades
    public final static FirebaseAuth mAuth= FirebaseAuth.getInstance();
    public final static  String KEYARRAYFAVSPREFERENCES = mAuth.getCurrentUser().getUid().toString();
    public final static String ARRAYTIENDASFAV = "ARRAYTIENDASFAVS";
    public final static  String KEYARRAYFILTROSPREFERENCESGAMES = mAuth.getCurrentUser().getUid().toString();
    public final static String ARRAYGAMESFILTROS = "ARRAYGAMESFILTROS";
    public final static String TITLEINPUTTIENDASCERCANAS="TITLE";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_GENRE = "genre";
    public static final String EXTRA_SHORTSCREENSHOT = "short_screenshots";
    public static final String EXTRA_CLIP = "clip";
    public static final String EXTRA_STORE = "stores";
    public static final String EXTRA_STORENAME = "store";
    public static final String EXTRA_PLATFORM = "platforms";
    public final static String LATINPUTTIENDASCERCANAS="LAT";
    public final static String LONINPUTTIENDASCERCANAS="LON";
    public final static String RADIUSINPUTTIENDASCERCANAS="RADIUS";
    public final static String PERMISIONDENIED="Permission Denied...";
    public final static String GPSPERMISEDGARANTED="GPS Permission granted!";
    public final static String PERMISSIONDENIEDUSER= "Permission denied by user!";
    public final static String MARKEROPTIONSTITLE=" Are you here ";
    public final static String PERMISSIONGRANTEDPAST="[LOCATION] Permission granted in the past!";
    public final static String ELIMINADOFAV="Removed from Favorites";
    public final static String ELIMINARFAVCONTEXTMENU="Delete from Favourites";
    public final static String PARCELABLEKEYARRAY="KEY_ARRAY";
    public final static String JSONARRAY="results";
    public final static String JSONDATAID="id";
    public final static String JSONDATATITLE="name";
    public final static String JSONIMAGE="background_image";
    public final static String JSONSHORTSCREEN="short_screenshots";
    public final static String JSONSHORTSCREENIMAGE="image";
    public final static String JSONWEBSITE="website";
    public final static String JSONRATING="rating";
    public final static String JSONRELEASED="released";
    public final static String JSONGENRES="genres";
    public final static String JSONGENRESNAME="name";
    public final static String JSONPLATFORMS="platforms";
    public final static String JSONDESCRIPTION="description";
    public final static String JSONMETACRITIC="metacritic";
    public final static String JSONPLATFORM="platform";
    public final static String JSONPLATFORMNAME="name";
    public final static String JSONSTORES="stores";
    public final static String JSONSTORESURL="url_en";
    public final static String JSONSTORE="store";
    public final static String JSONSTORENAME="name";
    public final static String PROGRESSTITTLE="GAMES";
    public final static String PROGRESSMESSAGE="SEARCHING... WAIT A SECOND";

}
