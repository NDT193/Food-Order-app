package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.Api.CreateOrder;
import com.example.foodorder.Helper1.ManagmentCart;
import com.example.foodorder.databinding.ActivityCartBinding;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private String TxtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        Intent intent = getIntent();

        managmentCart = new ManagmentCart(this);
        calculateCart();
        initList();
        setVariable();

        binding.orderCartBtn.setOnClickListener(v -> {
            CreateOrder orderApi = new CreateOrder();
            try {
                JSONObject data = orderApi.createOrder(TxtAmount);
                String code = data.getString("return_code");
                Log.d("Amount", TxtAmount);
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    Log.d("Amount", token);
                    ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {
                            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                            intent.putExtra("result", "Payment successful");
                            intent.putExtra("totalAmount", TxtAmount);
                            startActivity(intent);
                            managmentCart.clearCart();
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {
                            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                            intent.putExtra("result", "The payment has been cancelled.");
                            intent.putExtra("totalAmount", TxtAmount);
                            startActivity(intent);
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                            intent.putExtra("result", "Payment error");
                            intent.putExtra("totalAmount", TxtAmount);
                            startActivity(intent);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scorllViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scorllViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cartView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cartView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.01; // 1% tax
        double delivery = 5000; //10$

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0;

        binding.totalFreeTxt.setText("VND" + itemTotal);
        binding.taxTxt.setText("VND" + tax);
        binding.deliveryTxt.setText("VND" + delivery);
        binding.totalTxt.setText("VND" + total);
        TxtAmount = String.valueOf((int) total);

    }

    private void setVariable() {
        binding.backCartBtn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}