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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class PostActivity extends AppCompatActivity {
    private ImageView addImage;
    private EditText addDesc;
    private Button addPost;
    private static final int REQUEST_CAMERA = 1,SELECT_FILE=0;
    private ProgressDialog mProgressdialog;
    private Uri uri;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore,firebaseFirestore1;
    private Bundle extras;
    private String emailID;
    private String rand;
    private String imageuri;
    private static final int MAX_LENGTH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        extras = getIntent().getExtras();
        emailID = extras.getString("Email");
        addImage = (ImageView) findViewById(R.id.newpost_image);
        addDesc = (EditText) findViewById(R.id.newpost_desc);
        addPost = (Button) findViewById(R.id.blog_newpost);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore1 = FirebaseFirestore.getInstance();
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = addDesc.getText().toString();
                if(!TextUtils.isEmpty(desc) && uri!=null){
                     rand = FieldValue.serverTimestamp().toString();
                    StorageReference filepath = storageReference.child("Blog Posts").child(random()+".jpg");
                    firebaseFirestore1.collection(emailID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("Tag", document.getId() + " => " + document.getData());


                                     imageuri = document.getString("image uri");
                                }

                            }
                        }
                    });
                    final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Please wait while we save the image");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    filepath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                                Date currentLocalTime = cal.getTime();
                                DateFormat date = new SimpleDateFormat("HH:mm:ss a");

                                date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                                String localTime = date.format(currentLocalTime);
                                Toast.makeText(PostActivity.this,"Successfully Saved", Toast.LENGTH_SHORT).show();
                                String downloadUrl = task.getResult().getDownloadUrl().toString();
                                Map<String,Object> blogMap = new HashMap<>();
                                blogMap.put("blogimage",downloadUrl);
                                blogMap.put("description",desc);
                                blogMap.put("timestamp",localTime);
                                blogMap.put("emailid",emailID);
                                blogMap.put("imageuri",imageuri);
                               firebaseFirestore.collection("Blogs").add(blogMap).addOnCompleteListener(PostActivity.this, new OnCompleteListener<DocumentReference>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentReference> task) {
                                       if(task.isSuccessful()){
                                           progressDialog.dismiss();
                                           Toast.makeText(PostActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                                           Intent blogIntent = new Intent(PostActivity.this,BlogPostsActivity.class);
                                           blogIntent.putExtra("email",emailID);
                                           startActivity(blogIntent);
                                       }else{
                                           Toast.makeText(PostActivity.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               });
                            }
                        }
                    });

                }

            }
        });
    }
    private void selectImage(){
        final CharSequence[] items = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera")){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,REQUEST_CAMERA);

                }else if(items[which].equals("Gallery")){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent.createChooser(galleryIntent,"Select File"),SELECT_FILE);
                    /*CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SettingsActivity.this);*/

                }else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mProgressdialog = new ProgressDialog(PostActivity.this);
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
                 mProgressdialog.dismiss();
            }
        }
        addImage.setImageURI(uri);
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
