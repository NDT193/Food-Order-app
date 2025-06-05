package com.example.foodorder.AdminAct;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.databinding.ActivityFoodItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodItemActivity extends BaseActivity {
    private ActivityFoodItemBinding binding;
    private String foodId;
    private Boolean textFill;
    private FirebaseDatabase database;
    private ArrayAdapter<String> adapterCate;
    private ArrayAdapter<String> adapterTime;
    private String imagePath = "";
    private Double star = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        foodId = getIntent().getStringExtra("food_id");
        Log.i("Tag", foodId);
        textFill = getIntent().getBooleanExtra("text_fill", false);

        getCurFood();
        initSp();
        foodEdit();

        binding.backFditemBtn.setOnClickListener(v -> finish());

    }

    private void foodEdit() {
        binding.fooditemBtn.setOnClickListener(v -> {
            String title = binding.foodItemTitle.getText().toString().trim();
            String priceStr = binding.foodItemPrice.getText().toString().trim();
            String description = binding.foodItemDes.getText().toString().trim();
            boolean bestFood = binding.checkBox.isChecked();
            int categoryId = binding.fooditemCate.getSelectedItemPosition();
            int timeId = binding.fooditemTime.getSelectedItemPosition();

            double price = 0;
            try {
                price = Double.parseDouble(priceStr);
            } catch (Exception e) {
                price = 0;
            }

            // Build data map
            java.util.HashMap<String, Object> foodData = new java.util.HashMap<>();
            foodData.put("Title", title);
            foodData.put("Price", price);
            foodData.put("Id", Integer.parseInt(foodId));
            foodData.put("Description", description);
            foodData.put("BestFood", bestFood);
            foodData.put("CategoryId", categoryId);
            foodData.put("TimeId", timeId);
            foodData.put("ImagePath", imagePath);
            foodData.put("Star", star);
            //??
//            foodData.put("LocationId", 1);
//            foodData.put("PriceId", 1);
//            foodData.put("TimeValue", 20);

            // Update Firebase
            DatabaseReference foodRef = database.getReference("Foods").child(foodId);
            foodRef.setValue(foodData);
            Toast.makeText(FoodItemActivity.this, "Thông tin món ăn đã được chỉnh sửa", Toast.LENGTH_SHORT).show();
        });
    }

    private void getCurFood() {
        if (foodId == null) return;
        DatabaseReference foodRef = database.getReference("Foods").child(foodId);
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imagePath = snapshot.child("ImagePath").getValue(String.class);
                    star = snapshot.child("Star").getValue(Double.class);
                    Log.d("getCurFood", "ImagePath: " + imagePath + ", Star: " + star);
                    // Use imagePath and star as needed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void IsTextFill() {
        if (foodId == null || !textFill) return;
        DatabaseReference foodRef = database.getReference("Foods").child(foodId);
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Title").getValue(String.class);
                    String price = String.valueOf(snapshot.child("Price").getValue());
                    String description = snapshot.child("Description").getValue(String.class);
                    Boolean bestFood = snapshot.child("BestFood").getValue(Boolean.class);
                    Integer categoryId = snapshot.child("CategoryId").getValue(Integer.class);
                    Integer timeId = snapshot.child("TimeId").getValue(Integer.class);

                    if (Boolean.TRUE.equals(bestFood)) {
                        binding.checkBox.setChecked(true);
                    } else {
                        binding.checkBox.setChecked(false);
                    }
                    if (name != null) binding.foodItemTitle.setText(name);
                    if (price != null) binding.foodItemPrice.setText(price);
                    if (description != null) binding.foodItemDes.setText(description);
                    // Set spinner selection for category by index
                    if (categoryId != null && adapterCate != null && categoryId < adapterCate.getCount()) {
                        binding.fooditemCate.setSelection(categoryId);
                    }
                    // Set spinner selection for time by index
                    if (timeId != null && adapterTime != null && timeId < adapterTime.getCount()) {
                        binding.fooditemTime.setSelection(timeId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
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
                adapterCate = new ArrayAdapter<>(FoodItemActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fooditemCate.setAdapter(adapterCate);
                // Gọi IsTextFill sau khi adapterCate đã khởi tạo
                if (adapterTime != null) IsTextFill();
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
                adapterTime = new ArrayAdapter<>(FoodItemActivity.this, android.R.layout.simple_spinner_item, Timereq);
                adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fooditemTime.setAdapter(adapterTime);
                // Gọi IsTextFill sau khi adapterTime đã khởi tạo
                if (adapterCate != null) IsTextFill();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

    }
}

