package com.example.foodorder.AdminAct;

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

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.Adapter.OrderMnAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Domain.GeneralBill;
import com.example.foodorder.R;
import com.example.foodorder.databinding.ActivityOrderMnBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderMnActivity extends BaseActivity {
    ActivityOrderMnBinding binding;
    private FirebaseDatabase database;
    private OrderMnAdapter orderMnAdapter;
    private ArrayList<GeneralBill> allBillsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderMnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initList();
        setVariable();
    }

    private void setVariable() {
        binding.orderBackBtn.setOnClickListener(v -> finish());
        binding.deletedBill.setOnClickListener(v -> {deletedBill();});
    }

    private void deletedBill() {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xoá hoá đơn món ăn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (orderMnAdapter != null) {
                        GeneralBill selectedBill = orderMnAdapter.selectedBill();
                        if (selectedBill != null) {
                            String BillId = String.valueOf(selectedBill.getBillId());
                            DatabaseReference billRef = database.getReference("GeneralBill").child(BillId);
                            billRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    allBillsList.remove(selectedBill);
                                    orderMnAdapter = new OrderMnAdapter(allBillsList);
                                    binding.orderList.setAdapter(orderMnAdapter);

                                    Toast.makeText(OrderMnActivity.this, "Xoá món hoá đơn ăn thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OrderMnActivity.this, "Xoá món hoá đơn ăn thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(OrderMnActivity.this, "Chưa chọn hoá đơn món ăn", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void initList() {
        DatabaseReference reference = database.getReference("GeneralBill");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allBillsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GeneralBill bill = dataSnapshot.getValue(GeneralBill.class);
                    if (bill != null) {
                        bill.setBillId(dataSnapshot.getKey());
                        allBillsList.add(bill);
                    }
                }
                binding.orderList.setLayoutManager(new LinearLayoutManager(OrderMnActivity.this));
                orderMnAdapter = new OrderMnAdapter( allBillsList);
                binding.orderList.setAdapter(orderMnAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderMnActivity", "Database error: " + error.getMessage());
            }
        });
    }
}