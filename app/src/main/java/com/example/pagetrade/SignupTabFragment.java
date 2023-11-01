package com.example.pagetrade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupTabFragment extends Fragment {
    private EditText email, pass, conPass, username,phone;
    private Spinner spinner;
    private Button signup;
    private ImageView hidePass1, hidePass2;
    float v = 0;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);
        username = root.findViewById(R.id.etName);
        email = root.findViewById(R.id.etEmail);
        pass = root.findViewById(R.id.etPassword);
        hidePass1 = root.findViewById(R.id.eye);
        conPass = root.findViewById(R.id.etConfirm_pass);
        hidePass2 = root.findViewById(R.id.eye1);
        phone = root.findViewById(R.id.etPhone);
        spinner = root.findViewById(R.id.spinner);
        signup = root.findViewById(R.id.btnSignup);
        signup.setEnabled(true);

        auth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        hidePass1.setImageResource(R.drawable.hide_pwd);
        hidePass2.setImageResource(R.drawable.hide_pwd);

        username.setTranslationX(800);
        email.setTranslationX(800);
        pass.setTranslationX(800);
        hidePass1.setTranslationX(800);
        conPass.setTranslationX(800);
        hidePass2.setTranslationX(800);
        phone.setTranslationX(800);
        spinner.setTranslationX(800);
        signup.setTranslationX(800);

        username.setAlpha(v);
        email.setAlpha(v);
        pass.setAlpha(v);
        hidePass1.setAlpha(v);
        conPass.setAlpha(v);
        hidePass2.setAlpha(v);
        spinner.setAlpha(v);
        signup.setAlpha(v);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        hidePass1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        conPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        hidePass2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        spinner.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(requireContext(),R.array.usertype, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        hidePass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());           //hide password
                    hidePass1.setImageResource(R.drawable.show_pwd);
                } else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass1.setImageResource(R.drawable.hide_pwd);
                }
            }
        });

        hidePass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conPass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    conPass.setTransformationMethod(PasswordTransformationMethod.getInstance());           //hide password
                    hidePass2.setImageResource(R.drawable.show_pwd);
                } else{
                    conPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hidePass2.setImageResource(R.drawable.hide_pwd);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registerUser();
            }
        });
        return root;
    }
    private void showError(View view, String message) {
        if (view instanceof EditText) {
            ((EditText) view).setError(message);
            view.requestFocus();
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setError(message);
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    String textEmail="" ,textName="" ,textPwd="" ,textConfirmPwd="" ,textPhone="" ,textAddress="" ,item="" ;
    private void registerUser() {
        textEmail = email.getText().toString();
        textName = username.getText().toString();
        textPwd = pass.getText().toString();
        textConfirmPwd = conPass.getText().toString();
        textPhone = phone.getText().toString();
        //textAddress = address.getText().toString();
        item= spinner.getSelectedItem().toString();

        //Validate Phone Number using Matcher and pattern(Regular expression)
        String mobileRegex = "[0-1][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher= mobilePattern.matcher(textPhone);

        if (TextUtils.isEmpty(textName)) {
            showError(username, "Please enter your name.");
        } else if (TextUtils.isEmpty(textEmail) || !Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
            showError(email, "Please enter a valid email.");
        } else if (TextUtils.isEmpty(textPwd) || textPwd.length() < 3) {
            showError(pass, "Password should be at least 3 characters.");
        } else if (TextUtils.isEmpty(textConfirmPwd) || !textPwd.equals(textConfirmPwd)) {
            showError(conPass, "Passwords do not match.");
        } else if (TextUtils.isEmpty(textPhone) || textPhone.length() >11 || !mobileMatcher.find()) {
            showError(email, "Phone Number should be at most 11 characters.");
        }
        progressDialog.setMessage("Creating new user");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(textEmail, textPwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Authentication failed :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving user info...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        String uid= auth.getUid();
        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", textEmail);
        hashMap.put("name", textName);
        hashMap.put("PhoneNo", textPhone);
        hashMap.put("address", "");
        hashMap.put("password", textPwd);
        hashMap.put("profileImg", "");
        hashMap.put("userType", item);
        hashMap.put("timestamp", timestamp);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        firebaseUser.sendEmailVerification();
                        if (item.equals("Buyer")) {
                            progressDialog.dismiss();
                            Toast.makeText(requireContext(), "SignUp Successful as a buyer. Please verify your email :)", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), MainBuyerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (item.equals("Seller")) {
                            progressDialog.dismiss();
                            Toast.makeText(requireContext(), "SignUp Successful as a seller. Please verify your email :)", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
