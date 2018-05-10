package com.example.shesha.tourpal;

import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shesha.tourpal.Adapter.CustomSwipeAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.unity3d.player.UnityPlayerActivity;



public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    private static final int PLACE_PICKER_REQUEST = 1;
    private Button confirmButton;
    private FirebaseAuth mAuth;
    private TextView welcomename;
    private TextView welemail;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private LatLng latLng;
    private Marker marker;
    private static final int REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;
    private Bundle extras;
    private String email;
    private FirebaseFirestore firebaseFirestore;
    private String emailString;
    private String nameString;
    private String imageuri;
    private ImageView profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        requestPermission();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        confirmButton = (Button) findViewById(R.id.confirmBtn);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new CustomSwipeAdapter(HomePage.this);
        viewPager.setAdapter(adapter);
        extras = getIntent().getExtras();
        email = extras.getString("email ID");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(email).get().addOnCompleteListener(HomePage.this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        builder.setTitle("Account Creation");
                        builder.setMessage("Looks like you haven't created an account yet. Please set up your account with the same email ID.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent signIntent = new Intent(HomePage.this,SignUpPage.class);
                                startActivity(signIntent);

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("Tag", document.getId() + " => " + document.getData());
                         nameString = document.getString("username");
                        // imageuri = document.getString("image uri");


                        emailString = document.getString("email");
                    }
                }

            }
        });


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Email ID");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = mAuth.getCurrentUser();


                Toast.makeText(HomePage.this,"Name is"+user.getEmail(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatbotIntent = new Intent(HomePage.this,ChatBotActivity.class);
                startActivity(chatbotIntent);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(HomePage.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        welcomename = (TextView)header.findViewById(R.id.welcomeusername);
        welemail = (TextView)header.findViewById(R.id.usernameemailID);
        profile = (ImageView) header.findViewById(R.id.user_nav_image);
        welcomename.setText(nameString);
        //Log.d("HomeUri",imageuri);
        Picasso.with(HomePage.this).load(imageuri).placeholder(R.mipmap.ic_launcher_round).into(profile);

        welemail.setText(emailString);


    }

    private void requestPermission() {
        if(ActivityCompat.checkSelfPermission(HomePage.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_FINE_LOCATION:
                if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Location Permissions are required",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
       if(getFragmentManager().getBackStackEntryCount()>0)
       {
           getFragmentManager().popBackStackImmediate();
           return;
       }
       super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.quickguide:{
                Intent quickIntent = new Intent(HomePage.this,CityIntermediate.class);
                quickIntent.putExtra("email",email);
                startActivity(quickIntent);
            }

                break;
            case R.id.imagerecognition :

                startActivity(new Intent(HomePage.this,CaptureActivity.class));





            break;
            case R.id.contactus : fragment = new ContactUs(); break;
            case R.id.logout : {
                mAuth.signOut();
                startActivity(new Intent(HomePage.this,LoginAndSignUp.class));
                finish();
            }
            break;
            case R.id.agreality:

                Intent intent = new Intent(HomePage.this, com.example.shesha.tourpal.UnityPlayerActivity.class);
                intent.putExtra("Message","Hello");
                intent.putExtra("Value",123);
                //startActivity(intent);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.profileactivity:
            {
                Intent profileIntent = new Intent(HomePage.this,ProfileActivity.class);
                profileIntent.putExtra("Email ID",emailString);
                startActivity(profileIntent);
            }
            break;
            case R.id.langtranslate:
                Intent langIntent = new Intent(HomePage.this,LanguageTranslator.class);
                startActivity(langIntent);
                break;


        }
        if(fragment!=null)
        {
            viewPager.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area,fragment);
            fragmentTransaction.commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(HomePage.this, data);
                Intent intent = new Intent(HomePage.this, MapsActivity.class);
                intent.putExtra("City Name", place.getName());
                intent.putExtra("email",emailString);
                Log.d("Attributions", place.getId());
                startActivity(intent);
                if (requestCode == REQUEST_CODE)
                    if (resultCode == RESULT_OK) {
                        String message = data.getStringExtra("Message");
                        Toast.makeText(HomePage.this, message, Toast.LENGTH_LONG).show();
                    }


            }

            }

        }

    }


