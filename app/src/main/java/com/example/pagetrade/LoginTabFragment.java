package com.example.pagetrade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment {
    private EditText email, pass;
    private Button forgetPass;
    private ImageView hidePass;
    private Button login;
    float v = 0;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is signed in (non-null) and update the UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(requireContext(), "Already logged in!", Toast.LENGTH_SHORT).show();
            checkUser();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        email = root.findViewById(R.id.etEmail);
        pass = root.findViewById(R.id.etPassword);
        hidePass = root.findViewById(R.id.eye);
        forgetPass = root.findViewById(R.id.btnForPass);
        login = root.findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        hidePass.setImageResource(R.drawable.hide_pwd);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        hidePass.setTranslationX(800);
        forgetPass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        hidePass.setAlpha(v);
        forgetPass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        hidePass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        hidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());           //hide password
                    hidePass.setImageResource(R.drawable.show_pwd);
                } else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass.setImageResource(R.drawable.hide_pwd);
                }
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Can reset Password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), FogetPwdActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        return root;
    }

    String enteredEmail="" ,enteredPassword="";
    private void validateData() {
        enteredEmail = String.valueOf(email.getText());
        enteredPassword = String.valueOf(pass.getText());

        // Check for empty fields
        if (TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
        } else{
            loginUser();
        }
    }
    private void showError(View view, String message) {
        if (view instanceof EditText) {
            ((EditText) view).setError(message);
            view.requestFocus();
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    private void loginUser() {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        if (TextUtils.isEmpty(enteredEmail) || !Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            showError(email, "Please enter a valid email.");
        } else if (TextUtils.isEmpty(enteredPassword) || enteredPassword.length() < 3) {
            showError(pass, "Password should be at least 3 characters.");
        }
        mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        progressDialog.setMessage("Checking User...");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                progressDialog.dismiss();
                                String userType=""+snapshot.child("userType").getValue();
                                if(userType.equals("Buyer")){
                                    Toast.makeText(requireContext(), "LogIn Successful as a buyer :)", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(requireContext(), MainBuyerActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if(userType.equals("Seller")){
                                    Toast.makeText(requireContext(), "LogIn Successful as a seller :)", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(requireContext(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
    }
}
