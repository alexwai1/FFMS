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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextReset;
    private Button buttonReset;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextInputLayout textInputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        editTextReset = (EditText) findViewById(R.id.editTextReset);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

        buttonReset.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void reset(){
        String email = editTextReset.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        boolean valid = true;

        if(editTextReset.getText().toString().isEmpty() || !email.matches(emailPattern)){
            textInputLayoutEmail.setError("Email is invalid");
            valid = false;
        }
        else {
            textInputLayoutEmail.setErrorEnabled(false);
        }

        if(valid == true){
            progressDialog.setMessage("Requesting for password reset...");
            progressDialog.show();

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ResetActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
        if (v == buttonReset){
            reset();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
