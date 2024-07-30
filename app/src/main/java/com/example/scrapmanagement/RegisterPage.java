package com.example.scrapmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    private EditText phonenoEditText, emailEditText, passwordEditText, repasswordEditText;
    private RadioGroup radioGroupRole;
    private Button registerButton, goToLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        phonenoEditText = findViewById(R.id.phonenoEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repasswordEditText = findViewById(R.id.repasswordEditText);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        registerButton = findViewById(R.id.registerButton);
        goToLoginPage = findViewById(R.id.goToLoginPage);

        goToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
            }
        });
    }

    public void registerUser(View view) {

        String email = emailEditText.getText().toString();
        String phoneno = phonenoEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repassword = repasswordEditText.getText().toString();


        TextFieldValidation(email, phoneno, password, repassword);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        // Get selected role from RadioGroup
        int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
        String Role = (selectedRoleId == R.id.radioButtonUser) ? "User" : "ScrapMan";

        // Perform user registration with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Save user role to Firestore

                                FirebaseFirestore db;
                                db = FirebaseFirestore.getInstance();

                                DocumentReference userRef = db.collection("Users").document(user.getUid());
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("Email", email);
                                userData.put("Phone Number", phoneno);
                                userData.put("Role", Role);

                                userRef.set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(RegisterPage.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                                    goToLoginPage();
                                                } else {

                                                    Toast.makeText(RegisterPage.this, "Please Retry", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {

                            Toast.makeText(RegisterPage.this, "Couldn't Register User", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToLoginPage() {
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
    }


    private void TextFieldValidation(String email, String phoneno, String password, String repassword) {

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(phoneno)) {
            phonenoEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters");
            return;
        }

        if (!password.equals(repassword)) {
            repasswordEditText.setError("Passwords do not match");
            return;
        }
    }


}