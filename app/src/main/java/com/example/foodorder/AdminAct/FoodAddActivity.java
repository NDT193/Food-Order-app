package com.example.foodorder.AdminAct;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityFoodAddTestBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodAddActivity extends AppCompatActivity {
    ActivityFoodAddTestBinding binding;
    private ArrayAdapter<String> adapterCate;
    private ArrayAdapter<String> adapterTime;
    private FirebaseDatabase database;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodAddTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initSp();
        binding.fooditemBtn1.setOnClickListener(v -> addFood());
        binding.backFditemBtn1.setOnClickListener(v -> finish());
    }

    private void addFood() {
        String title = binding.foodItemTitle1.getText().toString().trim();
        String priceStr = binding.foodItemPrice1.getText().toString().trim();
        String description = binding.foodItemDes1.getText().toString().trim();
        boolean bestFood = binding.checkBox1.isChecked();
        int categoryId = binding.fooditemCate1.getSelectedItemPosition();
        int timeId = binding.fooditemTime1.getSelectedItemPosition();

        price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            price = 0;
        }

        // Tạo id mới cho food
        DatabaseReference foodsRef = database.getReference("Foods");
        foodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long totalItems = snapshot.getChildrenCount();
                String newFoodId = String.valueOf(totalItems);

                // Tạo object food
                java.util.HashMap<String, Object> foodData = new java.util.HashMap<>();
                foodData.put("Title", title);
                foodData.put("Price", price);
                foodData.put("Id", Integer.parseInt(newFoodId));
                foodData.put("Description", description);
                foodData.put("BestFood", bestFood);
                foodData.put("CategoryId", categoryId);
                foodData.put("TimeId", timeId);
                foodData.put("ImagePath", "data:image/jpeg;base64,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");
//                foodData.put("LocationId", 1);
//                foodData.put("PriceId", 1);
//                foodData.put("Star", 3);
//                foodData.put("TimeValue", 10);


                // Đẩy dữ liệu lên Firebase
                foodsRef.child(newFoodId).setValue(foodData);
                Toast.makeText(FoodAddActivity.this, "Thêm móm ăn mơi thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initSp() {
        ArrayList<String> categoryNames = new ArrayList<>();
        DatabaseReference categoryRef = database.getReference("Category");
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryNames.clear();
                for (DataSnapshot categorySnap : snapshot.getChildren()) {
                    String name = categorySnap.child("Name").getValue(String.class);
                    if (name != null) {
                        categoryNames.add(name);
                    }
                }
                adapterCate = new ArrayAdapter<>(FoodAddActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fooditemCate1.setAdapter(adapterCate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

        ArrayList<String> Timereq = new ArrayList<>();
        DatabaseReference TimeRef = database.getReference("Time");
        TimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Timereq.clear();
                for (DataSnapshot TimeSnap : snapshot.getChildren()) {
                    String valued = TimeSnap.child("Value").getValue(String.class);
                    if (valued != null) {
                        Timereq.add(valued);
                    }
                }
                adapterTime = new ArrayAdapter<>(FoodAddActivity.this, android.R.layout.simple_spinner_item, Timereq);
                adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fooditemTime1.setAdapter(adapterTime);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}