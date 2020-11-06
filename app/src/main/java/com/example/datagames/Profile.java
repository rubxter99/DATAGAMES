package com.example.datagames;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView emailprofile;
    private TextView nameprofile;
    private ImageView user;
    private CircleImageView profileImageView;
    private String DISPLAY_NAME = null;
    private String PROFILE_IMAGE_URL = null;
    private int TAKE_IMAGE_CODE = 10001;
    private Button SignOut;
    private Button ResetPassword;
    private Button ChangeProfile;
    private static final String TAG = "Profile";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private StorageReference storageReference;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        emailprofile=findViewById(R.id.emailprofile);
        nameprofile=findViewById(R.id.nameprofile);
        profileImageView=findViewById(R.id.profileImageView1);
        user=findViewById(R.id.profileImageView);


        SignOut=(Button)findViewById(R.id.btnSignOut);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
                finish();
            }
        });
        getUser();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        storageReference=FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("usuarios/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
        ChangeProfile=findViewById(R.id.btnChangeProfile);
        ChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",nameprofile.getText().toString());
                i.putExtra("email",emailprofile.getText().toString());
                startActivity(i);

            }
        });




    }
    public void getUser(){
        String id=mAuth.getCurrentUser().getUid();

        mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String email=snapshot.child("email").getValue().toString();
                    Log.d("perfil",email+" "+name);
                    nameprofile.setText(name);
                    emailprofile.setText(email);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("tiendas").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String email=snapshot.child("latitud").getValue().toString();
                    Log.d("maps",email+" "+name);


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }




}