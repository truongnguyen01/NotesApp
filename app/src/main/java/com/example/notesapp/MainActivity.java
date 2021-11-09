package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText mloginEmail, mloginPassword;
    private RelativeLayout mlogin, mgoToSignup;
    private TextView mgoToForgotPassword;

    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressbarofmainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginEmail = (EditText) findViewById(R.id.loginemail);
        mloginPassword = (EditText) findViewById(R.id.loginpassword);
        mlogin = (RelativeLayout) findViewById(R.id.login);
        mgoToSignup = (RelativeLayout) findViewById(R.id.gotosignup);
        mgoToForgotPassword = (TextView) findViewById(R.id.gotoforgotpassword);

        mprogressbarofmainactivity = (ProgressBar) findViewById(R.id.progressbarofmainactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser!= null){
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        }

        mgoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });

        mgoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginEmail.getText().toString().trim();
                String password = mloginPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    //login the user

                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkMailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }
            }
        });

    }

    private void checkMailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified() == true){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, notesActivity.class));
        }
        else {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your mail first.", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}