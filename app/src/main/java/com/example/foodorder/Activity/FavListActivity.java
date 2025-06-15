package com.example.foodorder.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.Adapter.FavListAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.AdminAct.FoodActivity;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityFavListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavListActivity extends BaseActivity {
    ActivityFavListBinding binding;
    private ArrayList<Foods> foodFav = new ArrayList<>();
    private FavListAdapter favListAdapter;
 private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initList();
        setVariable();
    }

    private void setVariable() {
        binding.backFavBtn.setOnClickListener(v -> finish());
        binding.deletedFavBtn.setOnClickListener(v -> deleteFavFood());
        binding.saveFavBtn.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        if (favListAdapter != null) {
            Foods selectedFood = favListAdapter.getSelectedFood();
            if (selectedFood != null) {
                selectedFood.setNumberInCart(1);
                ManagmentCart managmentCart = new ManagmentCart(this);
                managmentCart.insertFood(selectedFood);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a food item", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteFavFood() {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xoá món ăn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (favListAdapter != null) {
                        Foods selectedFood = favListAdapter.getSelectedFood();
                        if (selectedFood != null) {
                            // Lấy Id của món ăn (dạng String hoặc int, nếu int thì chuyển sang String)
                            String foodId = String.valueOf(selectedFood.getId());
                            DatabaseReference foodRef = database.getReference("Favorite/"+ uid ).child(foodId);
                            foodRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    foodFav.remove(selectedFood);
                                    favListAdapter = new FavListAdapter(foodFav);
                                    binding.favList.setAdapter(favListAdapter);

                                    Toast.makeText(FavListActivity.this, "Xoá món ăn thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FavListActivity.this, "Xoá món ăn thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(FavListActivity.this, "Chưa chọn món ăn", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void initList() {
        DatabaseReference myRed = database.getReference("Favorite").child(uid);
        ArrayList<Foods> list = new ArrayList<>();

        myRed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                    }
                    foodFav.clear();
                    foodFav.addAll(list); // Lưu toàn bộ danh sách vào biến toàn cục
                    if (list.size() > 0) {
                        binding.favList.setLayoutManager(new LinearLayoutManager(FavListActivity.this, LinearLayoutManager.VERTICAL, false));
                        favListAdapter = new FavListAdapter(list);
                        binding.favList.setAdapter(favListAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

