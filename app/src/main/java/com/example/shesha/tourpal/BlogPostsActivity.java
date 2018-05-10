package com.example.shesha.tourpal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class BlogPostsActivity extends AppCompatActivity {
    private FloatingActionButton addpost;
    private Bundle extras;
    private String email;
    private BottomNavigationView blogBottom;
    private HomeFragment homeFragment;
    private NotifFragment notifFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_posts);
        blogBottom = (BottomNavigationView) findViewById(R.id.chat_bottom_nav);
        extras = getIntent().getExtras();
        email = extras.getString("email");
        homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("email",email);
        homeFragment.setArguments(args);
        notifFragment = new NotifFragment();
        accountFragment = new AccountFragment();
        replaceFragment(homeFragment);
        blogBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_item_home:
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.bottom_item_notif:
                        replaceFragment(notifFragment);
                        return true;
                    case R.id.bottom_item_account:
                        replaceFragment(accountFragment);
                        return true;
                        default: return false;
                }

            }
        });

    addpost = (FloatingActionButton) findViewById(R.id.addpostBtn);
    addpost.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent postIntent = new Intent(BlogPostsActivity.this,PostActivity.class);
            postIntent.putExtra("Email",email);
            startActivity(postIntent);
        }
    });
    }
    private void replaceFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.blog_container,fragment);
        fragmentTransaction.commit();

    }
}
