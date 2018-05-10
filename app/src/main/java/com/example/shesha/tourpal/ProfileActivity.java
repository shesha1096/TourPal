package com.example.shesha.tourpal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView name;
    private TextView email;
    private ImageView image;
    private Button blogBtn;
    private Button chatBtn;
    private FirebaseFirestore firebaseFirestore;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private ProgressDialog mProgressdialog;
    private Uri uri;
    private StorageReference mImageStorage;
    private Bundle extras;
    private String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.profile_name);
        email = (TextView) findViewById(R.id.profile_emailID);
        image = (CircleImageView) findViewById(R.id.profile_photo);
        chatBtn = (Button) findViewById(R.id.profile_chat);
        extras = getIntent().getExtras();
        blogBtn = (Button) findViewById(R.id.profile_blog);
        emailID = extras.getString("Email ID");
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(ProfileActivity.this,UsersActivity.class);
                chatIntent.putExtra("email",emailID);
                startActivity(chatIntent);
            }
        });
        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blogIntent = new Intent(ProfileActivity.this,BlogPostsActivity.class);
                blogIntent.putExtra("email",emailID);
                startActivity(blogIntent);
            }
        });
        chatBtn = (Button) findViewById(R.id.profile_chat);


        mImageStorage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Select Image");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Camera")) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);

                        } else if (items[which].equals("Gallery")) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select File"), SELECT_FILE);
                    /*CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SettingsActivity.this);*/

                        } else if (items[which].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();

            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseFirestore.collection(emailID).get().addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("Tag", document.getId() + " => " + document.getData());
                        String nameString = document.getString("username");
                        name.setText(nameString);
                        String emailString = document.getString("email");
                        email.setText(emailString);
                        String imageuri = document.getString("image uri");
                        Picasso.with(ProfileActivity.this).load(imageuri).placeholder(R.drawable.gmail).into(image);

                    }
                    progressDialog.dismiss();
                } else {
                    Log.d("Tag", "Error getting documents: ", task.getException());
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mProgressdialog = new ProgressDialog(ProfileActivity.this);
            mProgressdialog.setTitle("Saving Image");
            mProgressdialog.setMessage("Please Wait");
            mProgressdialog.setCanceledOnTouchOutside(false);
            mProgressdialog.show();
            if (requestCode == REQUEST_CAMERA) {


                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                Log.d("Bitmap", bitmap.toString());
                mProgressdialog.dismiss();
            } else if (requestCode == SELECT_FILE) {
                 uri = data.getData();

            }
        }
        image.setImageURI(uri);
        final Map<String,String> imageMap = new HashMap<>();
        imageMap.put("image uri",uri.toString());
        firebaseFirestore.collection(emailID).document("Details").set(imageMap, SetOptions.merge()).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    String randString = rand();
                    StorageReference filepath =mImageStorage.child("Profile Images").child(emailID+".jpg");
                    final StorageReference thumb_filepath = mImageStorage.child("Profile_Images").child("thumbnail").child(emailID+".jpg");
                    filepath.putFile(uri).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Uri fileUri = task.getResult().getDownloadUrl();
                                imageMap.put("image uri",fileUri.toString());
                                firebaseFirestore.collection(emailID).document("Details").set(imageMap,SetOptions.merge()).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ProfileActivity.this,"Saved",Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }

                        }
                    });

                }else{
                    Toast.makeText(ProfileActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });
        mProgressdialog.dismiss();
    }

    private String rand() {
        String uuid = UUID.randomUUID().toString();
        return   uuid;
    }
}
