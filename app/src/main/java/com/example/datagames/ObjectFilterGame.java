package com.example.datagames;

public class ObjectFilterGame {
    private String rating;
    private int posRating;
    private String genre;
    private String released;

    public ObjectFilterGame(String rating, int posRating, String genre, String released) {
        this.rating = rating;
        this.posRating = posRating;
        this.genre = genre;
        this.released = released;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPosRating() {
        return posRating;
    }

    public void setPosRating(int posRating) {
        this.posRating = posRating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }
}
