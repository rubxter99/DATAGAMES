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

        viewPager=findViewById(R.id.viewPager);

        mAuth= FirebaseAuth.getInstance();


        IntroAdapter adapter=new IntroAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);



    }
    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,Menu.class));
            finish();
        }

    }

}