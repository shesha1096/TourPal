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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeStatus extends AppCompatActivity {
    private Bundle extras;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userStatus;
    private EditText user_input_status;
    private Button changeBtn;
    private String email;
    private FirebaseFirestore firebaseFirestore;
    private String username,image;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private ProgressDialog mProgressdialog;
    private Uri uri;
    private StorageReference mImageStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        extras = getIntent().getExtras();
        email = extras.getString("email");
        userImage = (CircleImageView) findViewById(R.id.status_image);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeStatus.this);
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
        userName = (TextView) findViewById(R.id.status_username);
        userStatus = (TextView) findViewById(R.id.status_currstatus);
        user_input_status = (EditText) findViewById(R.id.status_newstatus);
        changeBtn = (Button) findViewById(R.id.status_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(email).document("Details").get().addOnCompleteListener(ChangeStatus.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    username = task.getResult().get("username").toString();
                    image = task.getResult().get("image uri").toString();
                    Picasso.with(ChangeStatus.this).load(image).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(userImage);
                    userName.setText(username);

                }else{
                    Toast.makeText(ChangeStatus.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                }

            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = user_input_status.getText().toString();
                if(!status.isEmpty()){
                    userStatus.setText(status);
                    Map<String,String> statusMap = new HashMap<>();
                    statusMap.put("status",status);
                    FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
                    firebaseFirestore1.collection("Users").document(email).set(statusMap, SetOptions.merge()).addOnCompleteListener(ChangeStatus.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangeStatus.this,"Saved status successfully",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(ChangeStatus.this,"Error",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mProgressdialog = new ProgressDialog(ChangeStatus.this);
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
        userImage.setImageURI(uri);
        final Map<String,String> imageMap = new HashMap<>();
        imageMap.put("image uri",uri.toString());
        firebaseFirestore.collection(email).document("Details").set(imageMap, SetOptions.merge()).addOnCompleteListener(ChangeStatus.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageReference filepath =mImageStorage.child("Profile Images").child(email+".jpg");
                    final StorageReference thumb_filepath = mImageStorage.child("Profile_Images").child("thumbnail").child(email+".jpg");
                    filepath.putFile(uri).addOnCompleteListener(ChangeStatus.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Uri fileUri = task.getResult().getDownloadUrl();
                                imageMap.put("image uri",fileUri.toString());
                                firebaseFirestore.collection(email).document("Details").set(imageMap,SetOptions.merge()).addOnCompleteListener(ChangeStatus.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ChangeStatus.this,"Saved",Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }

                        }
                    });

                }else{
                    Toast.makeText(ChangeStatus.this,"Unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });
        mProgressdialog.dismiss();
    }
}
