package com.example.foodorder.Activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;

    private ManagmentCart managmentCart;
    private int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();
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
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}