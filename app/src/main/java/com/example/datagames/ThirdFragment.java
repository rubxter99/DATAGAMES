package com.example.datagames;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class ThirdFragment extends Fragment {

   private TextView back;
    private ViewPager viewPager;
    private Button LoginButton,RegisterButton,LoginButton2,RegisterButton2;
    private String name="";
    private String email="";
    private String password="";
    private EditText eUserEmailLog;
    private EditText ePasswordLog;
    private EditText eUserNameReg;
    private EditText eMail;
    private EditText ePasswordReg;
    private TextView resetPassword;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    public ThirdFragment() {
        // Required empty public constructor

    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_third, container, false);
        //Initialize ViewPager from Main Activity
        viewPager=getActivity().findViewById(R.id.viewPager);

        back=view.findViewById(R.id.slideOneBack2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        login(view);

        registrar(view);

        return view;

    }



    private void login(View view){


        LoginButton = (Button)view.findViewById(R.id.btnLogin1);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //init the bottom sheet view

                final BottomSheetDialog bottomSheetDialog =new BottomSheetDialog(v.getContext());
                bottomSheetDialog.setContentView(R.layout.btn_sheet_login);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                //Initialize And Assign Variable

                ePasswordLog=bottomSheetDialog.findViewById(R.id.text_passwordlog);
                eUserEmailLog=bottomSheetDialog.findViewById(R.id.text_useremaillog);

                resetPassword=bottomSheetDialog.findViewById(R.id.text_sendResetPassword);
                resetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(),ResetPassword.class));
                    }
                });



                LoginButton2 = (Button)bottomSheetDialog.findViewById(R.id.btnLogin2);
                LoginButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {

                        email=eUserEmailLog.getText().toString();
                        password=ePasswordLog.getText().toString();
                        //Verification
                        if(!email.isEmpty()  && !password.isEmpty()){

                            loginUser(vv);

                        }else{
                            Toast.makeText(vv.getContext(),"Debe completar los campos",Toast.LENGTH_SHORT).show();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }




        });
    }

    private void registrar(View view) {


        RegisterButton = (Button)view.findViewById(R.id.btnRegistrer1);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                final BottomSheetDialog bottomSheetDialog2 =new BottomSheetDialog(view.getContext());
                bottomSheetDialog2.setContentView(R.layout.btn_sheet_registrer);
                bottomSheetDialog2.setCanceledOnTouchOutside(false);

                eUserNameReg=(EditText)bottomSheetDialog2.findViewById(R.id.text_usernamereg);
                eMail=bottomSheetDialog2.findViewById(R.id.text_mailreg);
                ePasswordReg=bottomSheetDialog2.findViewById(R.id.text_passwordreg);

                RegisterButton2 = bottomSheetDialog2.findViewById(R.id.btnRegistrer2);
                RegisterButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name=eUserNameReg.getText().toString();
                        password=ePasswordReg.getText().toString();
                        email=eMail.getText().toString();
                        //Verification
                        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                            if(password.length()>=6){
                                registrerUser(v);
                            }else{
                                Toast.makeText(v.getContext(),"El password debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(v.getContext(),"Debe completar los campos",Toast.LENGTH_SHORT).show();
                        }
                        bottomSheetDialog2.dismiss();

                    }
                });



                bottomSheetDialog2.show();
            }
        });
    }

    public void loginUser(final View v1){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String,Object> map= new HashMap<>();
                    map.put("password",password);
                    String id=mAuth.getCurrentUser().getUid();

                    mDatabase.child("usuarios").child(id).updateChildren(map);
                    startActivity(new Intent(v1.getContext(),Menu.class));
                    getActivity().finish();
                }else {
                    Toast.makeText(v1.getContext(),"No se pudo iniciar sesion, compruebe los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrerUser(final View v2){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String,Object> map= new HashMap<>();
                    map.put("name",name);
                    map.put("email",email);
                    map.put("password",password);

                    String id=mAuth.getCurrentUser().getUid();

                    mDatabase.child("usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                //Actividad a donde quieres ir
                                //startActivity(new Intent(v2.getContext(),Profile.class));
                                //getActivity().finish();
                                Toast.makeText(v2.getContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(v2.getContext(),"No se pudieron crear los datos correctamente",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else{
                    Toast.makeText(v2.getContext(),"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();


    }
}