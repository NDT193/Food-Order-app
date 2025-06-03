package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodorder.AdminAct.AdminMainActivity;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database= FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();

        setVariable();
        initDialogFP();
    }

    private void initDialogFP() {
        binding.forgetPassEdit.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(""); // Để custom title

            // Tạo layout cho dialog
            android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 10);

            // Custom title căn giữa
            android.widget.TextView title = new android.widget.TextView(this);
            title.setText("Nhập Email để nhận mật khẩu mới");
            title.setPadding(0, 0, 0, 30);
            title.setGravity(android.view.Gravity.CENTER);
            title.setTextAppearance(this, R.style.textStyle);
            layout.addView(title);

            // EditText nhập email
            android.widget.EditText input = new android.widget.EditText(this);
            input.setHint("Nhập email");
            layout.addView(input);

            // Button gửi
            android.widget.Button btn = new android.widget.Button(this);
            btn.setBackgroundResource(R.drawable.red_button_background);
            btn.setText("Gửi");
            layout.addView(btn);
            builder.setView(layout);


            android.app.AlertDialog dialog = builder.create();
            btn.setOnClickListener(v1 -> {
                email = input.getText().toString();
                forgotPassword();
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void forgotPassword() {
        String emailAddress = email;
        mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    });
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.userEdit.getText().toString();
                String password = binding.passEdit.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    binding.userEdit.setError("Please enter your email");
                    binding.passEdit.setError("Please enter your password");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            String useremail = mAuth.getCurrentUser().getEmail();
                            String emailKey = useremail.replace(".", ",");

                            DatabaseReference userRef = database.getReference("Account").child(emailKey);
                            userRef.child("IsAdmin").get().addOnCompleteListener(isAdminTask -> {
                                if (isAdminTask.isSuccessful() && isAdminTask.getResult() != null) {
                                    Boolean isAdmin = isAdminTask.getResult().getValue(Boolean.class);
                                    if (Boolean.TRUE.equals(isAdmin)) {
                                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication For Admin failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        binding.Signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}

