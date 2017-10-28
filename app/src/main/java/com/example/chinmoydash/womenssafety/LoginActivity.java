package com.example.chinmoydash.womenssafety;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText etemail;
    MaterialEditText etpassword;
    Button btnSignIn;
    FirebaseAuth mFirebaseAuth;
    String email, pass;
    ProgressDialog mSignInDialog;
    private FirebaseAuth.AuthStateListener mStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }
        };

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etemail = (MaterialEditText) findViewById(R.id.etmail);
        etpassword = (MaterialEditText) findViewById(R.id.etpass);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {

        email = etemail.getText().toString().trim();
        pass = etpassword.getText().toString().trim();

        if (!etemail.isCharactersCountValid() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("invalid email");
            return;
        }
        if (!etpassword.isCharactersCountValid() || !pass.matches(RegisterActivity.PASSWORD_PATTERN)) {
            etpassword.setError("invalid password");
            return;
        }
        mSignInDialog = ProgressDialog.show(this, null, "Signing In...", false, false);

        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "authentication failed", Toast.LENGTH_LONG).show();
                            mSignInDialog.dismiss();
                        } else {

                            Intent intent = new Intent(LoginActivity.this, UserDetailActivity.class);
                            mSignInDialog.dismiss();
                            mSignInDialog = null;
                            startActivity(intent);

                        }
                    }

                });
    }



    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mStateListener);
    }
}
