package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class navigation_bar extends AppCompatActivity {
BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new home()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new home();
                        break;
                    case R.id.nav_person:
                        fragment = new profile();
                        break;
                    case R.id.nav_search:
                        fragment = new search();
                        break;
                    case R.id.nav_shop:
                        fragment = new shop();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();

                return true;
            }
        });
    }
}