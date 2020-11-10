package com.example.datagames;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailParse {
    private final String TAG =getClass().getSimpleName();
    public static class details implements Parcelable{
        private String name;
        private String released;
        private String image;
        private String rating;
        private String genres;
        private String id;
        private String description;
        private String store;
        private String platforms;
        private String clip;
        private String website;
        private String metacritic;

        public details(String name, String released, String image, String rating, String genres, String id, String description, String platforms, String website, String metacritic) {
            this.name = name;
            this.released = released;
            this.image = image;
            this.rating = rating;
            this.genres = genres;
            this.id = id;
            this.description = description;
            this.platforms = platforms;
            this.website = website;
            this.metacritic = metacritic;
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


        public String getRating() {
            return rating;
        }



        public String getGenres() {
            return genres;
        }



        public String getId() {
            return id;
        }



        public String getDescription() {
            return description;
        }



        public String getStore() {
            return store;
        }



        public String getPlatforms() {
            return platforms;
        }



        public String getClip() {
            return clip;
        }



        public String getWebsite() {
            return website;
        }



        public String getMetacritic() {
            return metacritic;
        }

        public  details(Parcel in){
            name = in.readString();
            image = in.readString();
            rating = in.readString();
            genres=in.readString();
            released=in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(image);
            parcel.writeString(rating);
            parcel.writeString(genres);
            parcel.writeString(released);
        }

        public Parcelable.Creator<DetailParse.details> CREATOR =
                new Parcelable.Creator<DetailParse.details>() {
                    @Override
                    public DetailParse.details createFromParcel(Parcel parcel) {
                        return new DetailParse.details(parcel);
                    }

                    @Override
                    public DetailParse.details[] newArray(int i) {
                        return new DetailParse.details[i];
                    }
                };


    }
    public JSONObject parseDetailsGame (String content){


        JSONArray array;
        JSONObject json = null;
        JSONObject data = null;

        try {
            json = new JSONObject(content);
            DetailParse.details pnode = parseDetailGame (json);


            return json;
            } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
            return null;

        }
    }



    private details parseDetailGame(JSONObject jsonData) throws JSONException {
         String name="";
         String released="";
         String image="";
         String rating="";
         String namegenres="";
         String id="";
         String description="";
         String store="";
         String nameplatforms="";
         String clip="";
         String website="";
         String metacritic="";


        try{
            if(jsonData.has(HelperGlobal.JSONDATAID))
                id = jsonData.getString(HelperGlobal.JSONDATAID);
            if(jsonData.has(HelperGlobal.JSONDATATITLE))
                name = jsonData.getString(HelperGlobal.JSONDATATITLE);
            if(jsonData.has(HelperGlobal.JSONRELEASED))
                released = jsonData.getString(HelperGlobal.JSONRELEASED);
            if(jsonData.has(HelperGlobal.JSONIMAGE)){
                image = jsonData.getString(HelperGlobal.JSONIMAGE);

                // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
            }
            if(jsonData.has(HelperGlobal.JSONDESCRIPTION)){
                description = jsonData.getString(HelperGlobal.JSONDESCRIPTION);
            }
            if(jsonData.has(HelperGlobal.JSONMETACRITIC)){
                metacritic = jsonData.getString(HelperGlobal.JSONMETACRITIC);
            }
            if(jsonData.has(HelperGlobal.JSONWEBSITE)){
                website = jsonData.getString(HelperGlobal.JSONWEBSITE);
            }
            if(jsonData.has(HelperGlobal.JSONRATING)){
                rating = jsonData.getString(HelperGlobal.JSONRATING);
            }
            if(jsonData.has(HelperGlobal.JSONPLATFORMS)){
                JSONArray platforms = jsonData.getJSONArray(HelperGlobal.JSONPLATFORMS);
                for(int i = 0; i < platforms.length();i++) {
                    JSONObject node = platforms.getJSONObject(i);
                    if(node.has(HelperGlobal.JSONPLATFORM)){
                        JSONObject platform = node.getJSONObject(HelperGlobal.JSONPLATFORM);

                            if (platform.has(HelperGlobal.JSONPLATFORMNAME))
                                nameplatforms = platform.getString(HelperGlobal.JSONPLATFORMNAME);
                            // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                            //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
                        }
                    }

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


        } catch (JSONException e) {
            e.printStackTrace();
        }
     details detail = new details(id,name,released,image,description,metacritic,website,rating,nameplatforms,namegenres);
        return detail;
    }


}
