package com.example.foodorder.AdminAct;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.Adapter.UserMnAdapter;
import com.example.foodorder.Domain.Account;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityUserMnBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMnActivity extends BaseActivity {
    ActivityUserMnBinding binding;
    private FirebaseDatabase database;

    private ArrayList<Account> accountsList = new ArrayList<>();
    private UserMnAdapter UserMnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initSp();
        binding.userMnBack.setOnClickListener(v -> finish());
        binding.userMnDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletedUser();
            }
        });
    }
    public void DeletedUser() {
        if (UserMnAdapter != null) {
            Account selectedAccount = UserMnAdapter.getSelectedAccount();
            if (selectedAccount != null && selectedAccount.getEmail() != null) {
                String key = selectedAccount.getEmail().replace(".", ",");
                DatabaseReference userRef = database.getReference("Account").child(key);
                userRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Xoá người dùng thành công", Toast.LENGTH_SHORT).show();
                        initSp();
                    } else {
                        Toast.makeText(this, "Xoá người dùng Thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Chọn người dùng cần xoá", Toast.LENGTH_SHORT).show();
            }
        }
    }
        private void initSp () {
            if (database == null || binding == null) return;
            try {
                DatabaseReference usersRef = database.getReference("Account");
                if (usersRef == null) return;

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isDestroyed() || isFinishing() || binding == null) return;

                        accountsList.clear();
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            try {
                                // Đọc từng field riêng lẻ
                                String email = userSnap.child("Email").getValue(String.class);
                                String name = userSnap.child("Name").getValue(String.class);
                                String uid = userSnap.child("Uid").getValue(String.class);
                                String number = userSnap.child("Number").getValue(String.class);
                                Boolean isAdmin = userSnap.child("IsAdmin").getValue(Boolean.class);

                                Account account = new Account();
                                account.setEmail(email);
                                account.setName(name);
                                account.setUid(uid);
                                account.setNumber(number);
                                account.setAdmin(isAdmin != null && isAdmin);

                                accountsList.add(account);

                                if (binding.userMnRv != null) {
                                    binding.userMnRv.setLayoutManager(new LinearLayoutManager(UserMnActivity.this));
                                    UserMnAdapter = new UserMnAdapter(accountsList);
                                    binding.userMnRv.setAdapter(UserMnAdapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






