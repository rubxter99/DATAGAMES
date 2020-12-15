package com.example.datagames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SecondFragment extends Fragment {
    TextView next;
    TextView back;
    ViewPager viewPager;

    public SecondFragment() { //Constructor de la segunda pantalla de Introducción/Fragmento

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //Visualización de la vista del Segundo Fragmento
        // Englobar el layout de este Fragmento
        View view= inflater.inflate(R.layout.fragment_second, container, false);
        //Inicializar el ViewPager de la actividad MainActivity
        viewPager=getActivity().findViewById(R.id.viewPager);

        //Función para llevar al Tercer fragmento
        next=view.findViewById(R.id.slideOneNext2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        //Función para llevar al Primer fragmento
        back=view.findViewById(R.id.slideOneBack1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        return view;
    }
}