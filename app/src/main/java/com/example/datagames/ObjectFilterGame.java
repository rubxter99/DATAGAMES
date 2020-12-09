package com.example.datagames;

public class ObjectFilterGame {
    private String rating;
    private int posRating;
    private String genre;
    private int postGenre;
    private String platform;
    private int postPlatform;

    public ObjectFilterGame(String rating, int posRating,String platform,int postPlatform,String genre,int postGenre) { //Constructor del Objeto Filtros
        this.rating = rating;
        this.posRating = posRating;
        this.genre = genre;
        this.postGenre = postGenre;
        this.platform = platform;
        this.postPlatform = postPlatform;
    }

    public String getRating() { //Obtiene la puntuación inicial de los filtros
        return rating;
    }

    public int getPosRating() { //Obtiene la puntuación final de los filtros
        return posRating;
    }

    public String getGenre() { //Obtiene la categoria inicial de los filtros
        return genre;
    }

    public int getPostGenre() { //Obtiene la categoria final de los filtros
        return postGenre;
    }

    public String getPlatform() { //Obtiene la plataforma inicial de los filtros
        return platform;
    }

    public int getPostPlatform() { //Obtiene la plataforma final de los filtros
        return postPlatform;
    }
}
