package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodorder.Adapter.PaymentAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityPaymentBinding;

import java.util.ArrayList;

public class PaymentActivity extends BaseActivity {
    ActivityPaymentBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.titlePayment.setText(intent.getStringExtra("result"));
        binding.totalPaymentTxt.setText(intent.getStringExtra("totalAmount") + " VND");
        InitList();
        BacktoHomePage();
    }

    private void BacktoHomePage() {
        binding.hompageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InitList() {
        ArrayList<Foods> paymentList = new ArrayList<>();
         managmentCart = new ManagmentCart(this);
         paymentList.addAll(managmentCart.getListCart());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.paymentList.setLayoutManager(linearLayoutManager);
        adapter = new PaymentAdapter(paymentList);
        binding.paymentList.setAdapter(adapter);
    }
}