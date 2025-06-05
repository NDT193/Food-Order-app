package com.example.foodorder.AdminAct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.databinding.ActivityFoodBinding;
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
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        binding.foodBackBtn.setOnClickListener(v -> finish());
        initList();
        setSearchVariable();
        setbuttonVariable();

        //Đảm bảo rằng RecyclerView được khởi tạo sau khi giao diện đã sẵn sàng
        binding.getRoot().getViewTreeObserver().addOnWindowFocusChangeListener(hasFocus -> {
            if (hasFocus) {
                initList();
            }
        });
    }

    private void setbuttonVariable() {
        binding.foodDeleteBtn.setOnClickListener(v -> {
            if (foodAdapter != null) {
                Foods selectedFood = foodAdapter.getSelectedFood();
                if (selectedFood != null) {
                    // Lấy Id của món ăn (dạng String hoặc int, nếu int thì chuyển sang String)
                    String foodId = String.valueOf(selectedFood.getId());
                    DatabaseReference foodRef = database.getReference("Foods").child(foodId);
                    foodRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            allFoodsList.remove(selectedFood);
                            foodAdapter = new FoodAdapter(allFoodsList);
                            binding.foodActvc.setAdapter(foodAdapter);

                            Toast.makeText(FoodActivity.this, "Xoá món ăn thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FoodActivity.this, "Xoá món ăn thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FoodActivity.this, "Chưa chọn món ăn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.foodEditBtn.setOnClickListener(v -> getfoodname(FoodItemActivity.class, true));
        binding.foodAddBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(FoodActivity.this, FoodAddActivity.class);
            startActivity(intent1);
        });
    }

    private void setSearchVariable() {
        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchTxt.getText().toString().trim();
            ArrayList<Foods> filteredList = new ArrayList<>();
            for (Foods food : allFoodsList) {
                if (food != null && food.getTitle() != null && food.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(food);
                }
            }
            binding.foodActvc.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
            foodAdapter = new FoodAdapter(filteredList);
            binding.foodActvc.setAdapter(foodAdapter);
        });
        binding.foodBackBtn.setOnClickListener(v -> finish());
        binding.refreshFoodList.setOnClickListener(v -> {
            binding.foodActvc.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false));
            foodAdapter = new FoodAdapter(allFoodsList);
            binding.foodActvc.setAdapter(foodAdapter);
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
                        foodAdapter = new FoodAdapter(list);
                        binding.foodActvc.setAdapter(foodAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getfoodname(Class<?> GoalClass, Boolean fill) {
        if (foodAdapter != null) {
            Foods selectedFood = foodAdapter.getSelectedFood();
            if (selectedFood != null) {
                String foodId = String.valueOf(selectedFood.getId());
                Intent intent = new Intent(FoodActivity.this, GoalClass);
                intent.putExtra("food_id", foodId);
                intent.putExtra("text_fill", fill); // Sửa key cho đồng nhất
                Log.i("UID", foodId);
                startActivity(intent);
            }
        }
    }
}
