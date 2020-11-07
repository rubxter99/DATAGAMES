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

        StorageReference profileRef=storageReference.child("usuarios/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
        profileFullName=findViewById(R.id.editextUserName);
        profileEmail=findViewById(R.id.editTextEmail);
        profileImageView=findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
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
                        String id=user.getUid();
                        Map<String,Object> edited=new HashMap<>();
                        edited.put("email",email);
                        edited.put("name",profileFullName.getText().toString());
                        mDatabase.child("usuarios").child(id).updateChildren(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "PERFIL ACTUALIZADO", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile.class));
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

        Log.d(TAG,"onCreate: "+fullName+" "+email);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){

                Uri imageUri=data.getData();
                //profileImageView.setImageURI(imageUri);
                Log.d("tihss",imageUri.toString());
                uploadImageFirebase(imageUri);
            }
        }
    }

    private void uploadImageFirebase(Uri imageUri) {

        final StorageReference fileRef=storageReference.child("usuarios/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
                Toast.makeText(EditProfile.this, "LA IMAGEN FUE CAMBIADA", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "LA IMAGEN FUE CAMBIADA", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
