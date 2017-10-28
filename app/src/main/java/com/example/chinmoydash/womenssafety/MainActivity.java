package com.example.chinmoydash.womenssafety;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button register,login;
    Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.but_register);
        login = (Button) findViewById(R.id.but_login);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  Toast.makeText(getApplication(),"you pressed the register button",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplication(),"you pressed the login button",Toast.LENGTH_SHORT).show();

                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }

        };

    }
        @Override
        protected void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mStateListener);
        }

        @Override
        protected void onStop() {
            super.onStop();
            mAuth.removeAuthStateListener(mStateListener);
        }


    }

