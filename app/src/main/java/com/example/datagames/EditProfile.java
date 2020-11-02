package com.example.datagames;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private static final String TAG="TAG";
    private EditText profileFullName,profileEmail;
    private ImageView profileImageView;
    private Button saveBtn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser user;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent getIntent = getIntent();
        String fullName=getIntent.getStringExtra("fullName");
        String email=getIntent.getStringExtra("email");

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        user=fAuth.getCurrentUser();
        storageReference=FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        profileFullName=findViewById(R.id.editextUserName);
        profileEmail=findViewById(R.id.editTextEmail);
        profileImageView=findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "CAMBIO CORRECTO", Toast.LENGTH_SHORT).show();
            }
        });
        saveBtn=findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileFullName.getText().toString().isEmpty()|| profileEmail.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "GUARDADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email=profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference=fStore.collection("usuarios").document(user.getUid());
                        String id=fAuth.getCurrentUser().getUid();

                        Map<String,Object> edited=new HashMap<>();
                        edited.put("email",email);
                        edited.put("name",profileFullName.getText().toString());
                        mDatabase.child("usuarios").child(id).updateChildren(edited);
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "PERFIL ACTUALIZADO", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        });


                        Toast.makeText(EditProfile.this, "EL EMAIL FUE CAMBIADO", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        StorageReference profileRef=storageReference.child("usuarios/perfil.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });

        profileEmail.setText(email);
        profileFullName.setText(fullName);

        Log.d(TAG,"onCreate: "+fullName+" "+email);

    }
}
