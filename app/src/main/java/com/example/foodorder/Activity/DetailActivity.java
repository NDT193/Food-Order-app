package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private String foodId;
    private ManagmentCart managmentCart;
    private int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        getIntentExtra();
        addToFavList();
        setVariable();

    }

    private void addToFavList() {
        foodId = String.valueOf(object.getId());
        binding.favBtn.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = database.getReference("Favorite").child(uid).child(foodId);

            ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(DetailActivity.this, "Food already in favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        java.util.HashMap<String, Object> foodMap = new java.util.HashMap<>();
                        foodMap.put("Id", object.getId());
                        foodMap.put("ImagePath", object.getImagePath());
                        foodMap.put("Price", object.getPrice());
                        foodMap.put("Star", object.getStar());
                        foodMap.put("Title", object.getTitle());

                        ref.setValue(foodMap)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(DetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(DetailActivity.this, "Add to favorites failed", Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                    Toast.makeText(DetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backDetailBtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.picDetail);

        binding.priceDetailTxt.setText("VND" + object.getPrice());
        binding.titleDetailTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateDetailTxt.setText(object.getStar() + "Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalPriceTxt.setText((num * object.getPrice()) + "VND");

        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + "");
            binding.totalPriceTxt.setText("VND" + (num * object.getPrice()));
        });
        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalPriceTxt.setText("VND" + (num * object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });

        binding.commnetBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, CommentActivity.class);
            intent.putExtra("FoodId", foodId);
            startActivity(intent);
        });


    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}
