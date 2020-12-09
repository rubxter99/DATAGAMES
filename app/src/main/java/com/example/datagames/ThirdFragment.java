package com.example.datagames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThirdFragment extends Fragment {

    TextView next;
    TextView back;
    ViewPager viewPager;

    public ThirdFragment() { //Constructor de la tercera pantalla de Introduccion/Fragmento

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //Visualización de la vista del Segundo Fragmento
        // Englobar el layout de este Fragmento
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        //Inicializar el ViewPager de la actividad MainActivity
        viewPager = getActivity().findViewById(R.id.viewPager);
        //Función para llevar al cuarto fragmento
        next = view.findViewById(R.id.slideOneNext3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
            }
        });

        //Función para llevar al segundo fragmento
        back = view.findViewById(R.id.slideOneBack2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        return view;
    }
}