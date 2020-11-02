package com.example.datagames;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesParse {
    private final String TAG = getClass().getSimpleName();

    public static class game  implements Parcelable{
        private String name;
        private String released;
        private String image;
        private String rating;
        private String genres;
        private String id;
        private String short_screenshots;
        private String clip;
        private String store;
        private String storename;
        private String urlstore;
        private String platforms;
        //  private String extensionImg;
        //  private String price;

        public game(String name, String released, String image, String rating, String genres, String id, String short_screenshots, String clip, String storename, String urlstore, String platforms) {
            this.name = name;
            this.released = released;
            this.image = image;
            this.genres = genres;
            this.rating = rating;
            this.id = id;
            this.short_screenshots = short_screenshots;
            this.clip = clip;
            this.storename = storename;
            this.urlstore = urlstore;
            this.platforms = platforms;
        }




        public String getId() {
            return id;
        }

        public String getShort_screenshots() {
            return short_screenshots;
        }

        public String getClip() {
            return clip;
        }

        public String getStorename() {
            return storename;
        }

        public String getUrlstore() {
            return urlstore;
        }

        public String getPlatforms() {
            return platforms;
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

        public  game(Parcel in){
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

        public static Parcelable.Creator<GamesParse.game> CREATOR =
                new Parcelable.Creator<GamesParse.game>() {
                    @Override
                    public GamesParse.game createFromParcel(Parcel parcel) {
                        return new GamesParse.game(parcel);
                    }

                    @Override
                    public GamesParse.game[] newArray(int i) {
                        return new GamesParse.game[i];
                    }
                };
    }




    public ArrayList<game> parseGame(String content) {
        ArrayList<game> lGame = new ArrayList<game>();

        JSONArray array;
        JSONObject json = null;


        try {
            json = new JSONObject(content);

            array = json.getJSONArray(HelperGlobal.JSONARRAY);

            for (int i = 0; i < array.length(); i++) {
                JSONObject node = array.getJSONObject(i);
                game pnode = parseGame(node);

                lGame.add(pnode);
            }
            return lGame;
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    private game parseGame(JSONObject jsonData) throws JSONException {
        String id = "";
        String name = "";
        String released = "";
        String image = "";
        String rating = "";
        String namegenres = "";
        String short_screenshotsimage = "";
        String clip = "";
        String clip2 = "";
        String namestore = "";
        String urlstore = "";
        String nameplatforms = "";


        try {

            if (jsonData.has(HelperGlobal.JSONDATATITLE))
                name = jsonData.getString(HelperGlobal.JSONDATATITLE);
            if (jsonData.has(HelperGlobal.JSONRELEASED))
                released = jsonData.getString(HelperGlobal.JSONRELEASED);
            if (jsonData.has(HelperGlobal.JSONIMAGE)) {
                image = jsonData.getString(HelperGlobal.JSONIMAGE);
            }
            if (jsonData.has(HelperGlobal.JSONRATING)) {
                rating = jsonData.getString(HelperGlobal.JSONRATING);
            }
            if (jsonData.has(HelperGlobal.JSONGENRES)) {
                JSONArray genress = jsonData.getJSONArray(HelperGlobal.JSONGENRES);
                for (int i = 0; i < genress.length(); i++) {
                    JSONObject node = genress.getJSONObject(i);
                    if (node.has(HelperGlobal.JSONGENRESNAME))
                        namegenres = node.getString(HelperGlobal.JSONGENRESNAME);
                    // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                    //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
                }

            }
            if (jsonData.has(HelperGlobal.JSONDATAID))
                id = jsonData.getString(HelperGlobal.JSONDATAID);
            if (jsonData.has(HelperGlobal.JSONSHORTSCREEN)) {
                JSONArray short_screenshotss = jsonData.getJSONArray(HelperGlobal.JSONSHORTSCREEN);
                for (int i = 0; i < short_screenshotss.length(); i++) {
                    JSONObject node = short_screenshotss.getJSONObject(i);
                    if (node.has(HelperGlobal.JSONSHORTSCREENIMAGE))
                        short_screenshotsimage = node.getString(HelperGlobal.JSONSHORTSCREENIMAGE);
                    // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                    //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
                }

            }
            if (jsonData.has(HelperGlobal.JSONCLIP)) {

                JSONObject clips = jsonData.getJSONObject(HelperGlobal.JSONCLIP);
                if (clips.has(HelperGlobal.JSONCLIP)) {

                    clip = clips.getString(HelperGlobal.JSONCLIP);

                }


            }


            if (jsonData.has(HelperGlobal.JSONSTORES)) {
                JSONArray stores = jsonData.getJSONArray(HelperGlobal.JSONSTORES);
                for (int i = 0; i < stores.length(); i++) {
                    JSONObject node = stores.getJSONObject(i);
                    if (node.has(HelperGlobal.JSONSTORESURL)) {
                        urlstore = node.getString(HelperGlobal.JSONSTORESURL);

                    }
                    if (node.has(HelperGlobal.JSONSTORE)) {
                        JSONObject node2=node.getJSONObject(HelperGlobal.JSONSTORE);
                        if (node2.has(HelperGlobal.JSONSTORENAME)){
                            namestore = node2.getString(HelperGlobal.JSONSTORENAME);
                        }
                    }


                }

            }
            if (jsonData.has(HelperGlobal.JSONPLATFORMS)) {
                JSONArray platforms = jsonData.getJSONArray(HelperGlobal.JSONPLATFORMS);
                for (int i = 0; i < platforms.length(); i++) {
                    JSONObject node = platforms.getJSONObject(i);
                    if (node.has(HelperGlobal.JSONPLATFORM)) {
                        JSONObject platform = node.getJSONObject(HelperGlobal.JSONPLATFORM);
                        if (platform.has(HelperGlobal.JSONPLATFORMNAME))
                            nameplatforms = platform.getString(HelperGlobal.JSONPLATFORMNAME);
                        // if(node.has(HelperGlobal.JSONOBJECTEXTENSION))
                        //  extensionImg = node.getString(HelperGlobal.JSONOBJECTEXTENSION);
                    }
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        game game = new game(name, released, image, rating, namegenres, id, short_screenshotsimage, clip, namestore, urlstore, nameplatforms);
        return game;
    }

}
