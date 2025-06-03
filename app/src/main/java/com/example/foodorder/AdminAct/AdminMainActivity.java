package com.example.foodorder.AdminAct;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity {
    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.userManagerImg.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminMainActivity.this, UserManagerActivity.class);
//            startActivity(intent);
        });

        binding.foodManagerImg.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, FoodActivity.class);
            startActivity(intent);
        });

        binding.orderManagerImg.setOnClickListener(v -> {

        });


    }
}

