package com.example.datagames;

public class ObjectFilterGame {
    private String rating;
    private int posRating;
    private String genre;
    private int postGenre;
    private String platform;
    private int postPlatform;

    public ObjectFilterGame(String rating, int posRating,String platform,int postPlatform,String genre,int postGenre) {
        this.rating = rating;
        this.posRating = posRating;
        this.genre = genre;
        this.postGenre = postGenre;
        this.platform = platform;
        this.postPlatform = postPlatform;
    }

    public String getRating() {
        return rating;
    }

    public int getPosRating() {
        return posRating;
    }

    public String getGenre() {
        return genre;
    }

    public int getPostGenre() {
        return postGenre;
    }

    public String getPlatform() {
        return platform;
    }

    public int getPostPlatform() {
        return postPlatform;
    }
}
