package com.example.datagames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {


    ViewPager viewPager;
    Button LoginButton,RegisterButton;
    LinearLayout linearLayout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();//Inicia la conexión con la base de datos de Firebase

        IntroAdapter adapter=new IntroAdapter(getSupportFragmentManager());//Recoge el objeto adapter de la clase IntroAdapter junto a sus datos

        viewPager=findViewById(R.id.viewPager);//Muestra el ViewPager de la actividad
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onStart() { //Al comienzo de la aplicación comprobará el inicio de sesion de usuario y lo llevará a la Actividad Menu
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,Menu.class));
            finish();
        }

    }

}