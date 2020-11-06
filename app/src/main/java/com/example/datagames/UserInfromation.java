package com.example.datagames;

public class UserInfromation {

    public String name;
    public String latitud;
    public String longitud;

    public UserInfromation(){               //default constructor which invokes on object creation of respective class in MainActivity.java

    }

    public UserInfromation(String name, String latitud, String longitud){    //parameterized constructor which will store the retrieved data from firebase
        this.name=name;
        this.latitud=latitud;
        this.longitud=longitud;
    }
}