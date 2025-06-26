package com.example.foodorder.AdminAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity {
    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable() {
        binding.userManagerImg.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, UserMnActivity.class);
            startActivity(intent);
        });

        binding.foodManagerImg.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, FoodActivity.class);
            startActivity(intent);
        });

        binding.orderManagerImg.setOnClickListener(v -> {

        });
        binding.supplierManagerImg.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, SupplierActivity.class);
            startActivity(intent);
        });

        binding.cateManagerImg.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });


    }
}

