package com.example.foodorder.AdminAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Activity.CartActivity;
import com.example.foodorder.Activity.ListFoodsActivity;
import com.example.foodorder.Activity.LoginActivity;
import com.example.foodorder.Activity.MainActivity;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.databinding.ActivityFoodBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends BaseActivity {
    ActivityFoodBinding binding;
    private FirebaseDatabase database;
    private ArrayList<Foods> allFoodsList = new ArrayList<>(); // Lưu toàn bộ danh sách món ăn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        binding.foodBackBtn.setOnClickListener(v -> finish());
        initList();
        setVariable();
    }

    private void setVariable() {
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.searchTxt.getText().toString().trim();
                ArrayList<Foods> filteredList = new ArrayList<>();
                for (Foods food : allFoodsList) {
                    if (food != null && food.getTitle() != null && food.getTitle().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(food);
                    }
                }
                binding.foodActvc.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
                RecyclerView.Adapter adapter = new FoodAdapter(filteredList);
                binding.foodActvc.setAdapter(adapter);
            }
        });
        binding.foodBackBtn.setOnClickListener(v -> finish());
        binding.refreshFoodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.foodActvc.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
                RecyclerView.Adapter adapter = new FoodAdapter(allFoodsList);
                binding.foodActvc.setAdapter(adapter);
            }
        });
    }

    private void initList() {
        DatabaseReference myRed = database.getReference("Foods");
        ArrayList<Foods> list = new ArrayList<>();

        myRed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    allFoodsList.clear();
                    allFoodsList.addAll(list); // Lưu toàn bộ danh sách vào biến toàn cục
                    if (list.size() > 0) {
                        binding.foodActvc.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
                        RecyclerView.Adapter adapter = new FoodAdapter(list);
                        binding.foodActvc.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

