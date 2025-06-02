package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorder.Adapter.PaymentAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Env.Env;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.databinding.ActivityPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends BaseActivity {
    ActivityPaymentBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.titlePayment.setText(intent.getStringExtra("result"));
        binding.totalPaymentTxt.setText(intent.getStringExtra("totalAmount") + " VND");

        InitList();
        BacktoHomePage();
        getCurrentUserInfo();
    }

    private void getFoodBill() {
        // Lấy danh sách món ăn trong giỏ hàng
        ArrayList<Foods> paymentList = new ArrayList<>();
        managmentCart = new ManagmentCart(this);
        paymentList.addAll(managmentCart.getListCart());

        // Lấy tên khách hàng
        String name = binding.namePaymentTxt.getText().toString();
        // Lấy địa chỉ
        String location = binding.locPaymentTxt.getText().toString();
        // Lấy uid
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";
        // Lấy tổng tiền
        String totalAmountStr = binding.totalPaymentTxt.getText().toString().replace(" VND", "").replace(",", "");
        totalAmount = 0;
        try {
            totalAmount = Integer.parseInt(totalAmountStr);
        } catch (Exception e) {
            totalAmount = 0;
        }

        // Lấy số lượng đơn hàng hiện tại để tạo node con là số nguyên tăng dần
        DatabaseReference foodBillRef = database.getReference("FoodBill");
        foodBillRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long orderIndex = snapshot.getChildrenCount() + 1; // node con là số nguyên tăng dần
                String orderId = String.valueOf(orderIndex);
                for (Foods food : paymentList) {
                    java.util.HashMap<String, Object> bill = new java.util.HashMap<>();
                    bill.put("FoodTittle", food.getTitle());
                    bill.put("Location", location);
                    bill.put("Name", name);
                    bill.put("Price", totalAmount);
                    bill.put("Quantity", food.getNumberInCart());
                    bill.put("Uid", uid);

                    foodBillRef.child(orderId).child(food.getTitle()).setValue(bill);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getCurrentUserInfo() {
        // Lấy tên người dùng từ Firebase
        String email = mAuth.getCurrentUser().getEmail();
        if (email != null) {
            String emailKey = email.replace(".", ",");
            database.getReference("Account").child(emailKey).child("Name")
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot snapshot) {
                            String name = snapshot.getValue(String.class);
                            binding.namePaymentTxt.setText(name);
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                        }
                    });
        }

        binding.locPaymentTxt.setText(Env.selectedLocation);

    }

    private void BacktoHomePage() {
        binding.hompageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent);
            getFoodBill();
            managmentCart.clearCart();
        });
    }

    private void InitList() {
        ArrayList<Foods> paymentList = new ArrayList<>();
        managmentCart = new ManagmentCart(this);
        paymentList.addAll(managmentCart.getListCart());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.paymentList.setLayoutManager(linearLayoutManager);
        adapter = new PaymentAdapter(paymentList);
        binding.paymentList.setAdapter(adapter);
    }
}

