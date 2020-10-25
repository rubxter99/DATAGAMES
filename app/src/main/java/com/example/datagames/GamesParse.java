package com.example.datagames;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesParse {
    private final String TAG =getClass().getSimpleName();

    public class game{
        private String name;
        private String released;
        private String image;
        private String rating;
        private String genres;
        private String id;
      //  private String extensionImg;
      //  private String price;

        public game(String name, String released, String image, String rating,String genres,String id) {
            this.name = name;
            this.released = released;
            this.image = image;
            this.genres=genres;
            this.rating = rating;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getReleased() {
            return released;
        }

        public String getImage() {
            return image;
        }

        public String getGenres() {
            return genres;
        }

        public String getRating() {
            return rating;
        }
    }

    public ArrayList<game> parseGame (String content){
        ArrayList<game> lGame = new ArrayList<game>();

        JSONArray array;
        JSONObject json = null;


        try {
            json = new JSONObject(content);

            array = json.getJSONArray(HelperGlobal.JSONARRAY);

            for(int i = 0; i < array.length();i++){
                JSONObject node = array.getJSONObject(i);
                game pnode = parseGame (node);

                lGame.add(pnode);
            }
            return lGame;
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    private game parseGame(JSONObject jsonData) throws JSONException {
        String id= "";
        String name = "";
        String released = "";
        String image = "";
        String rating = "";
        String namegenres="";


        try{

            if(jsonData.has(HelperGlobal.JSONDATATITLE))
                name = jsonData.getString(HelperGlobal.JSONDATATITLE);
            if(jsonData.has(HelperGlobal.JSONRELEASED))
                released = jsonData.getString(HelperGlobal.JSONRELEASED);
            if(jsonData.has(HelperGlobal.JSONIMAGE)){
                image = jsonData.getString(HelperGlobal.JSONIMAGE);
            }
            if(jsonData.has(HelperGlobal.JSONRATING)){
                rating = jsonData.getString(HelperGlobal.JSONRATING);
                }
            if(jsonData.has(HelperGlobal.JSONGENRES)){
                JSONArray genress = jsonData.getJSONArray(HelperGlobal.JSONGENRES);
                for(int i = 0; i < genress.length();i++) {
                    JSONObject node = genress.getJSONObject(i);
                    if(node.has(HelperGlobal.JSONGENRESNAME))
                        namegenres = node.getString(HelperGlobal.JSONGENRESNAME);
                    // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                    //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
                }

            }
            if(jsonData.has(HelperGlobal.JSONDATAID))
                id = jsonData.getString(HelperGlobal.JSONDATAID);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        game game = new game(name,released,image,rating,namegenres,id);
        return game;
    }

}
