package com.example.datagames;

public class PlacesParse {

    double lat;
    double lon;
    String name;


    public PlacesParse() { //Constructor de las tiendas físicas de videojuegos

    }


    public double getLat() { //Obtener latitud de la localización de las tiendas
        return lat;
    }

    public void setLat(double lat) { //Introducir latitud de la localización de las tiendas
        this.lat = lat;
    }

    public double getLon() { //Obtener longitud de la localización de las tiendas
        return lon;
    }

    public void setLon(double lon) { //Introducir longitud de la localización de las tiendas
        this.lon = lon;
    }

    public String getName() { //Obtener el nombre de la localización de las tiendas
        return name;
    }

    public void setName(String name) { //Introducir el nombre de la localización de las tiendas
        this.name = name;
    }
}
