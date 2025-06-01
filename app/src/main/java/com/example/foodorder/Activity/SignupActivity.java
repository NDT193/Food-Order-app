package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.foodorder.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignupActivity extends BaseActivity {
    private DatabaseReference myRef;
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Account");
        mAuth = FirebaseAuth.getInstance();

        setVariable();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.userEdit.getText().toString();
            String password = binding.passEdit.getText().toString();
            String emailKey = email.replace(".", ",");


            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));

                    String uid = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> account = new HashMap<>();
                    account.put("IsAdmin", false);
                    account.put("Uid",uid);
                    account.put("Email", email);

                    myRef.child(emailKey).setValue(account);
                } else {
                    Exception exception = task.getException();
                    if (exception != null && exception.getClass().getSimpleName().equals("FirebaseAuthUserCollisionException")) {
                        Toast.makeText(SignupActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "failure " + exception);
                        Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.lognintxt.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}