package com.example.chinmoydash.womenssafety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chinmoydash.womenssafety.firebasedata.UserDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {


    public final static String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    MaterialEditText et_name;
    MaterialEditText et_email;
    MaterialEditText et_mobile;
    MaterialEditText et_password;
    MaterialEditText et_re_password;

    Button signUp;
    Context context;
    ProgressDialog mSignUpDialog;


    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mListener;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUsersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersReference = FirebaseDatabase.getInstance().getReference().child("user");

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    finish();
            }

        };

        et_name = (MaterialEditText) findViewById(R.id.etname);
        et_email = (MaterialEditText) findViewById(R.id.etemail);
        et_mobile = (MaterialEditText) findViewById(R.id.etmobile);
        et_password = (MaterialEditText) findViewById(R.id.etpass);
        et_re_password = (MaterialEditText) findViewById(R.id.etpass1);
        signUp = (Button) findViewById(R.id.btnSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();

            }

        });


    }
    private void submitData() {

        String name = et_name.getText().toString().trim();
        String mail = et_email.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String re_password = et_re_password.getText().toString().trim();

        final UserDataModel udm = new UserDataModel(name, mail, mobile);

        if (!et_name.isCharactersCountValid())
            return;
        else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches() || !et_email.isCharactersCountValid()) {
            et_email.setError("invalid email");
            return;
        }
        if (!et_mobile.isCharactersCountValid()) {
            et_mobile.setError("invalid_mobile");
            return;
        }

        if (!et_password.isCharactersCountValid() || !password.matches(PASSWORD_PATTERN)) {
            et_password.setError("invalid password");
            return;
        }
        if (!et_re_password.isCharactersCountValid() || !password.equals(re_password)) {
            et_re_password.setError("passwords do not match");
            return;
        }

        mSignUpDialog = ProgressDialog.show(this, null,"Signing in ..", false, false);

        auth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Log.e(LOG_TAG, getString(R.string.signup_success));
                mSignUpDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();

                setUserData(udm, authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mSignUpDialog.dismiss();
                if (task.isSuccessful()) {
                    //Log.e(LOG_TAG, getString("");
                    Intent intent = new Intent(getBaseContext(), UserDetailActivity.class);
                    startActivity(intent);
                }
            }
        });


}
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(mListener);
    }



    private void setUserData(UserDataModel udm, String uid) {
        mUsersReference.child(uid).setValue(udm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Profile created Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RegisterActivity.this, "Profile not created ", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    }