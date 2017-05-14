package com.example.alexwai.ffms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private TextView textViewForgot;
    private TextView textViewRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewForgot = (TextView) findViewById(R.id.textViewForgot);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        buttonSignIn.setOnClickListener(this);
        textViewForgot.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    private void login(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        boolean valid = true;

        if(editTextEmail.getText().toString().isEmpty() || !email.matches(emailPattern)){
            textInputLayoutEmail.setError("Email is invalid");
            valid = false;
        }
        else {
            textInputLayoutEmail.setErrorEnabled(false);
        }

        if(editTextPassword.getText().toString().isEmpty() || editTextPassword.length()<6){
            textInputLayoutPassword.setError("Password minimum length 6 characters");
            valid = false;
        }
        else {
            textInputLayoutPassword.setErrorEnabled(false);
        }

        if(valid == true){
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            return;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == buttonSignIn){
            login();
        }

        if (v == textViewForgot){
            finish();
            startActivity(new Intent(this, ResetActivity.class));
        }

        if (v == textViewRegister){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
