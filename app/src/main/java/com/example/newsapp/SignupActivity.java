package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {
    EditText name, email, passwd, cpasswd;
    Button signup, signin;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        passwd = findViewById(R.id.signup_password);
        cpasswd = findViewById(R.id.signup_cpassword);

        signup = findViewById(R.id.signup_btn);
        signin = findViewById(R.id.signin_btn_p);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });
        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_firstName = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = passwd.getText().toString();

                if(TextUtils.isEmpty(txt_firstName) && TextUtils.isEmpty(txt_email) && TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        }catch(FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            Toast.makeText(SignupActivity.this, "Already Exist", Toast.LENGTH_SHORT).show();
                                        } catch(Exception ex) {
                                            Toast.makeText(SignupActivity.this, "Some Error occurred, please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }  else {
                                        auth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}