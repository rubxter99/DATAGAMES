package com.example.datagames;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "TAG";
    private EditText profileFullName, profileEmail, profilePassword;
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
        mostrarDatos(getIntent);

    }


    private void enlaceFirebase(){ //Llamada a la base de datos de Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        StorageReference profileRef = storageReference.child("usuarios/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //Coger la imagen de la base de datos de Firebase
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
    }

    private void mostrarDatos(Intent intent){ //Mostrar los datos recogidos de la actividad Editar Perfil
        String fullName = intent.getStringExtra("fullName");
        String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");

        enlaceFirebase();

        profileFullName = findViewById(R.id.editextUserName);
        profileEmail = findViewById(R.id.editTextEmail);
        profilePassword = findViewById(R.id.editTextPassW);
        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() { //Abrir la galería del teléfono
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() { //Guardar cambios recogidos por el usuario
            @Override
            public void onClick(View v) {
                if (profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "SAVED CORRECTLY", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String id = user.getUid();
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("name", profileFullName.getText().toString());
                        edited.put("password", profilePassword.getText().toString());
                        mDatabase.child("usuarios").child(id).updateChildren(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "UPDATED PROFILE", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditProfile.this, Profile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Cierra todas las actividades anteriores
                                startActivity(intent);
                                finish();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        profileEmail.setText(email);
        profileFullName.setText(fullName);
        profilePassword.setText(password);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //Envío de datos del enlace de la imagen del perfil de usuario de Firebase
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                uploadImageFirebase(imageUri);
            }
        }
    }

    private void uploadImageFirebase(Uri imageUri) { //Cambiar imagen de perfil de usuario en la base de datos de Firebase

        final StorageReference fileRef = storageReference.child("usuarios/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
                Toast.makeText(EditProfile.this, "IMAGE WAS CHANGED", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "IMAGE WASN´T CHANGED", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
