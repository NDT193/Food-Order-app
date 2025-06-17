package com.example.foodorder.AdminAct;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Adapter.SupplierAdapter;
import com.example.foodorder.Domain.Supplier;
import com.example.foodorder.Helper1.SpaceItem;
import com.example.foodorder.databinding.ActivitySupplierBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SupplierActivity extends BaseActivity {
    ActivitySupplierBinding binding;
    private FirebaseDatabase database;
    private SupplierAdapter supAdap;
    private String supName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupplierBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initList();
        setVariable();
    }

    private void setVariable() {
        binding.backSupBtn.setOnClickListener(v -> finish());

        binding.saveSupplierBtn.setOnClickListener(v -> {
            if (binding.nameSupTxt.getText().toString().isEmpty()) {
                Toast.makeText(SupplierActivity.this, "Please enter supplier name", Toast.LENGTH_SHORT).show();
            } else {
                addSupplier();
            }
        });

        binding.deleteSupplierBtn.setOnClickListener(v -> deleteSupplier());

        binding.upSupplierBtn.setOnClickListener(v -> updateSupplier());
    }

    private void updateSupplier() {
        Supplier selectedSupplier = supAdap.getSelectedSupplier();
        if (selectedSupplier == null) {
            Toast.makeText(this, "Please select a supplier to update", Toast.LENGTH_SHORT).show();
            return;
        }

        final EditText input = new EditText(this);
        input.setText(selectedSupplier.getName());

        new AlertDialog.Builder(this)
                .setTitle("Update Supplier")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DatabaseReference supRef = database.getReference("Supplier")
                            .child(String.valueOf(selectedSupplier.getIdSup()))
                            .child("name");
                    supRef.setValue(newName)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Supplier updated", Toast.LENGTH_SHORT).show();
                                initList();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteSupplier() {
        Supplier selectedSupplier = supAdap.getSelectedSupplier();
        if (selectedSupplier == null) {
            Toast.makeText(this, "Please select a supplier to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        int supplierId = selectedSupplier.getIdSup();
        DatabaseReference supRef = database.getReference("Supplier").child(String.valueOf(supplierId));
        supRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Supplier deleted", Toast.LENGTH_SHORT).show();
                    initList();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }

    private void addSupplier() {
        supName = binding.nameSupTxt.getText().toString();

        DatabaseReference supRef = database.getReference("Supplier");
        supRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxId = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Supplier supplier = child.getValue(Supplier.class);
                    if (supplier != null && supplier.getIdSup() > maxId) {
                        maxId = supplier.getIdSup();
                    }
                }
                int newId = maxId + 1;
                Supplier newSupplier = new Supplier(newId, supName);
                supRef.child(String.valueOf(newId)).setValue(newSupplier)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(SupplierActivity.this, "Supplier added", Toast.LENGTH_SHORT).show();
                            binding.nameSupTxt.setText("");
                            initList();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(SupplierActivity.this, "Add failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initList() {
        //Tạo khoảng cách giũa các item
        int spaceInDp = 5;
        float scale = getResources().getDisplayMetrics().density;
        int spaceInPx = (int) (spaceInDp * scale + 0.5f);


        DatabaseReference supRef = database.getReference("Supplier");
        ArrayList<Supplier> supList = new ArrayList<>();

        supRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                supList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Supplier supplier = child.getValue(Supplier.class);
                    if (supplier != null) {
                        supList.add(supplier);
                    }
                }
                supAdap = new SupplierAdapter(supList);
                binding.supList.addItemDecoration(new SpaceItem(spaceInPx));
                binding.supList.setLayoutManager(new LinearLayoutManager(SupplierActivity.this, LinearLayoutManager.VERTICAL, false));
                binding.supList.setAdapter(supAdap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}