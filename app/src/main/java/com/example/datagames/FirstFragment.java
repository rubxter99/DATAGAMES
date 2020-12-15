package com.example.datagames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment {

    TextView next;
    ViewPager viewPager;

    public FirstFragment() { //Constructor de la primera pantalla de Introducción/Fragmento

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //Visualización de la vista del Primer Fragmento
        // Englobar el layout de este Fragmento
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        //Inicializar el ViewPager de la actividad MainActivity
        viewPager = getActivity().findViewById(R.id.viewPager);
        //Función para llevar al Segundo fragmento
        next = view.findViewById(R.id.slideOneNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }
}