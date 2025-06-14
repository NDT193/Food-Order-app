package com.example.foodorder.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.Helper.ChangeNumberItemsListener;
import com.example.foodorder.Helper.ManagmentCart;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
private ActivityCartBinding binding;
private RecyclerView.Adapter adapter;
private ManagmentCart managmentCart;
private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        calculateCart();
        initList();
        setVariable();
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty())
        {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scorllViewCart.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scorllViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.cartView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cartView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02; // 2% tax
        double delivery = 10; //10$

        tax = Math.round(managmentCart.getTotalFee()*percentTax*100.0)/ 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0;

        binding.totalFreeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$"+tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }

    private void setVariable() {
        binding.backCartBtn.setOnClickListener(v -> finish());
    }
}