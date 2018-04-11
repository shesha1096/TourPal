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


import com.example.shesha.tourpal.Model.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
    private String imgurl;

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
        imgurl = extras.getString("Image URL");
        Uri uri = Uri.parse(imgurl);
        Picasso.with(PlaceDescription.this).load(imgurl).placeholder(R.drawable.common_google_signin_btn_icon_dark).into(imagedesc);
        Toast.makeText(PlaceDescription.this,uri.toString(),Toast.LENGTH_SHORT).show();
        placetitle.setText(placename);
        placedesc.setText(placedescription);


    }






    }

