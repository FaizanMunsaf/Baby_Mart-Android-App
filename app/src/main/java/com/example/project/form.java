package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class form extends AppCompatActivity {

    TextView sign_up , login;
    LinearLayout linear1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sign_up = findViewById(R.id.sign_up);
        login = findViewById(R.id.login);
        linear1 = findViewById(R.id.linear1);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                form_signup F_su = new form_signup();
                loadsignupform(F_su);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                form_login f_1= new form_login();
                loadloginform(f_1);
            }
        });
    }
        private void loadsignupform(Fragment fragment){

            sign_up.setBackgroundColor(Color.parseColor("#3A1247"));
            sign_up.setTextColor(Color.parseColor("#ECE5EC"));
            login.setBackgroundColor(Color.parseColor("#ECE5EC"));
            login.setTextColor(Color.parseColor("#3A1247"));
            linear1.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_login,fragment);
            transaction.commit();

        }
         private void loadloginform(Fragment fragment){

            login.setBackgroundColor(Color.parseColor("#3A1247"));
            login.setTextColor(Color.parseColor("#ECE5EC"));
            sign_up.setBackgroundColor(Color.parseColor("#ECE5EC"));
            sign_up.setTextColor(Color.parseColor("#3A1247"));
            linear1.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_login,fragment);
            transaction.commit();

        }
}