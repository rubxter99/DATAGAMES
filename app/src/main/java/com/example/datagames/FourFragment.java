package com.example.datagames;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Button;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class FourFragment extends Fragment {

    private TextView back;
    private ViewPager viewPager;
    private Button LoginButton, RegisterButton, LoginButton2, RegisterButton2;
    private String name = "";
    private String email = "";
    private String password = "";
    private EditText eUserEmailLog;
    private EditText ePasswordLog;
    private EditText eUserNameReg;
    private EditText eMail;
    private EditText ePasswordReg;
    private TextView resetPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences.Editor prefsEditor2;

    public FourFragment() { //Constructor de la cuarta pantalla de Introducción/Fragmento

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //Visualización de la vista del Primer Fragmento
        // Englobar el layout de este Fragmento
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        //Inicializar el ViewPager de la actividad MainActivity
        viewPager = getActivity().findViewById(R.id.viewPager);
        //Función para llevar al Tercer fragmento
        back = view.findViewById(R.id.slideOneBack4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        //Conexión con la base de datos de Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        login(view);
        registrar(view);

        return view;

    }


    private void login(View view) { //Visualización de la vista de iniciar sesión de usuario para introducir los datos


        LoginButton = (Button) view.findViewById(R.id.btnLogin1);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());//Ventana del inicio de sesión
                bottomSheetDialog.setContentView(R.layout.btn_sheet_login);
                bottomSheetDialog.setCanceledOnTouchOutside(false);


                ePasswordLog = bottomSheetDialog.findViewById(R.id.text_passwordlog);
                eUserEmailLog = bottomSheetDialog.findViewById(R.id.text_useremaillog);

                resetPassword = bottomSheetDialog.findViewById(R.id.text_sendResetPassword); //Dirige al usuario a la ventana de la actividad ResetPassword
                resetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), ResetPassword.class));
                    }
                });


                LoginButton2 = (Button) bottomSheetDialog.findViewById(R.id.btnLogin2);
                LoginButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        email = eUserEmailLog.getText().toString();
                        password = ePasswordLog.getText().toString();
                        //Verificación
                        if (!email.isEmpty() && !password.isEmpty()) {

                            loginUser(vv);
                            restaurar();

                        } else {
                            Toast.makeText(vv.getContext(), "You must complete the fields", Toast.LENGTH_SHORT).show();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }


        });
    }

    private void registrar(View view) { //Visualización de la vista de registrar usuario para introducir los datos


        RegisterButton = (Button) view.findViewById(R.id.btnRegistrer1);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(view.getContext());//Ventana del registro de usuario
                bottomSheetDialog2.setContentView(R.layout.btn_sheet_registrer);
                bottomSheetDialog2.setCanceledOnTouchOutside(false);

                eUserNameReg = (EditText) bottomSheetDialog2.findViewById(R.id.text_usernamereg);
                eMail = bottomSheetDialog2.findViewById(R.id.text_mailreg);
                ePasswordReg = bottomSheetDialog2.findViewById(R.id.text_passwordreg);

                RegisterButton2 = bottomSheetDialog2.findViewById(R.id.btnRegistrer2);
                RegisterButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = eUserNameReg.getText().toString();
                        password = ePasswordReg.getText().toString();
                        email = eMail.getText().toString();
                        //Verificación
                        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                            if (password.length() >= 6) {
                                registrerUser(v);
                            } else {
                                Toast.makeText(v.getContext(), "The password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(v.getContext(), "You must complete the fields", Toast.LENGTH_SHORT).show();
                        }
                        bottomSheetDialog2.dismiss();

                    }
                });


                bottomSheetDialog2.show();
            }
        });
    }

    public void loginUser(final View v1) { //Método para acceder a la base de datos de Firebase y poder comprobar el usuario introducido
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("password", password);
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("usuarios").child(id).updateChildren(map);
                    startActivity(new Intent(v1.getContext(), Menu.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(v1.getContext(), "Failed to start session, check data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrerUser(final View v2) { //Método para acceder a la base de datos de Firebase e ingresar los datos del nuevo usuario a esta misma
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(v2.getContext(), "Successfully registered user", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v2.getContext(), "Failed to create data correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(v2.getContext(), "Failed to register user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void restaurar() { //Eliminar los filtros guardados junto con el sharedpreferences y marcarlo por defecto
        SharedPreferences mPrefs = this.getActivity().getSharedPreferences(HelperGlobal.KEYARRAYFAVSPREFERENCES, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
        SharedPreferences mPrefs2 = this.getActivity().getSharedPreferences(HelperGlobal.KEYARRAYFILTROSPREFERENCESGAMES, MODE_PRIVATE);
        prefsEditor2 = mPrefs.edit();
        prefsEditor2.clear();
        prefsEditor2.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurar();
    }

    @Override
    public void onPause() {
        super.onPause();
        restaurar();

    }
}