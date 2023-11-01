package com.example.pagetrade;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class FogetPwdActivity extends AppCompatActivity {
    private Button reset;
    private EditText email;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private final static String TAG = "FogetPwdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_pwd);

        reset = findViewById(R.id.btnReset);
        email = findViewById(R.id.etEmail);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(FogetPwdActivity.this);
        progressDialog.setTitle("Getting verification mail...");
        progressDialog.setCanceledOnTouchOutside(false);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(FogetPwdActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("Email is required");
                    email.requestFocus();
                } else {
                    resetPassword(mail);
                }
            }
        });
    }

    private void resetPassword(String email) {
        progressDialog.setMessage("Sending password reset email...");
        progressDialog.show();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(FogetPwdActivity.this, "Password reset email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FogetPwdActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        reset.setError("User doesn't exist or no longer valid. Please register again.");
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(FogetPwdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
