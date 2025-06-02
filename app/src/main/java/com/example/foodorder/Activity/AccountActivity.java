package com.example.foodorder.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.example.foodorder.databinding.ActivityAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class AccountActivity extends BaseActivity {
    ActivityAccountBinding binding;
    private String emailKey;
    private String email;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        emailKey = email.replace(".", ",");

        goback();
        initSp();
        initinfo();
        saveInfoUser();
    }

    private void saveInfoUser() {
        binding.accountSaveBtn.setOnClickListener(v -> {
            String name = binding.accountName.getText().toString().trim();
            String email = binding.accountEmail.getText().toString().trim();
            String phone = binding.accountNumber.getText().toString().trim();
            String location = binding.accountLoc.getText().toString().trim();

            DatabaseReference userRef = database.getReference("Account").child(emailKey);
            userRef.child("Name").setValue(name);
            userRef.child("Email").setValue(email);
            userRef.child("Number").setValue(phone);

            // Lưu location mới vào node Location với key là số tự tăng
            DatabaseReference locationRef = userRef.child("Location");
            locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long nextIndex = snapshot.getChildrenCount()+1;
                    locationRef.child(String.valueOf(nextIndex)).setValue(location)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sau khi lưu xong, cập nhật lại spinner
                                initSp();
                            }
                        });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu cần
                }
            });
        });
    }

    private void initinfo() {

        DatabaseReference userRef = database.getReference("Account").child(emailKey);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("Uid", "snapshot.exists(): " + snapshot.exists());
                    String name = snapshot.child("Name").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String phone = snapshot.child("Number").getValue(String.class);

                    binding.accountName.setText(name);
                    binding.accountEmail.setText(email);
                    binding.accountNumber.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void initSp() {
        DatabaseReference userRef = database.getReference("Account").child(emailKey);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> locations = new ArrayList<>();
                    for (DataSnapshot locationSnap : snapshot.child("Location").getChildren()) {
                        String location = locationSnap.getValue(String.class);
                        if (location != null) {
                            locations.add(location);
                        }
                    }
                    adapter = new ArrayAdapter<>(AccountActivity.this, android.R.layout.simple_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.accountSp.setAdapter(adapter);

                    // Set listener to update EditText when item selected
                    binding.accountSp.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                            String selectedLocation = locations.get(position);
                            binding.accountLoc.setText(selectedLocation);
                        }
                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) {
                            // Do nothing
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void goback() {
        binding.accountBackBtn.setOnClickListener(v -> finish());
    }

}

