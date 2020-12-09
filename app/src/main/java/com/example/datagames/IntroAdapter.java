package com.example.datagames;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter( FragmentManager fm) { //Constructor de la Actividad IntroAdapter
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { //Manejar todos los Fragmentos y contenerlos en un Adaptador
        switch (position){
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            case 3:
                return new FourFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    } //Contador de Fragmentos
}
