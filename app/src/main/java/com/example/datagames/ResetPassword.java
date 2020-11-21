package com.example.datagames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;
    private String email="";
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEditTextEmail=findViewById(R.id.editTextEmail);
        btnResetPassword=findViewById(R.id.btnResetPassword);

        mAuth=FirebaseAuth.getInstance();


        mDialog=new ProgressDialog(this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=mEditTextEmail.getText().toString();

                if(!email.isEmpty()){
                    mDialog.setMessage("Espera ...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();

                }else{
                    Toast.makeText(ResetPassword.this,"Debes ingresar el email",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void resetPassword(){ //metodo reseteo
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this,"Se envio el correo para restablecer tu contraseña",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassword.this,"No se pudo enviar el correo para restablecer contraseña",Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }
}