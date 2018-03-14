package com.example.shesha.tourpal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlaceDescription extends AppCompatActivity {
    private static final String TAG = "Placedesc";
    private TextView placetitle;
    private TextView placedesc;
    private StorageReference storageReference;
    private Bitmap my_image;
    private File localFile;
    private ImageView imagedesc;
    String placename;
    private static final int GALLERY_PICK = 1;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        placetitle = (TextView) findViewById(R.id.placeTitleID);
        placedesc = (TextView) findViewById(R.id.place_description);
        imagedesc = (ImageView) findViewById(R.id.place_image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
         placename = extras.getString("Place Name");
        String placedescription = extras.getString("Place Description");
        placetitle.setText(placename);
        placedesc.setText(placedescription);

    }

    public void selectImage(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
       /* CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(PlaceDescription.this);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK){
            if(resultCode == RESULT_OK){
                final Uri imageuri = data.getData();
                StorageReference filepath = storageReference.child("Mangaluru").child(placename+".jpg");
                filepath.putFile(imageuri).addOnCompleteListener(PlaceDescription.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                                Toast.makeText(PlaceDescription.this,"Saved",Toast.LENGTH_SHORT).show();
                                String downloadurl = task.getResult().getDownloadUrl().toString();
                                final DocumentReference doc = firebaseFirestore.collection("Mangaluru").document(placename);
                                HashMap<String,String> imageHashmap = new HashMap<>();
                                imageHashmap.put("image",downloadurl);

                                firebaseFirestore.collection("Mangaluru").document(placename).set(imageHashmap, SetOptions.merge()).addOnCompleteListener(PlaceDescription.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PlaceDescription.this," Successful",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(PlaceDescription.this,"Failed",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                        }else{
                            Toast.makeText(PlaceDescription.this,"Failure",Toast.LENGTH_SHORT).show();
                        }

                        }


                });
                Toast.makeText(PlaceDescription.this,imageuri.toString(),Toast.LENGTH_SHORT).show();
                final DocumentReference docRef = firebaseFirestore.collection("Mangaluru").document(placename);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Toast.makeText(PlaceDescription.this,"Description Data: "+document.get("description"),Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(PlaceDescription.this,"Could Not Get Data",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlaceDescription.this, "Could Not Get Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }


            }
        }
    }

