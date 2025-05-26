package com.example.foodorder.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
ActivityDetailBinding binding;
private Foods object;
int num=1;
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
        binding.backDetailBtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.picDetail);

        binding.priceDetailTxt.setText("$" + object.getPrice());
        binding.titleDetailTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateDetailTxt.setText(object.getStar()+"Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalPriceTxt.setText((num * object.getPrice()) + "$");

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");

    }
}