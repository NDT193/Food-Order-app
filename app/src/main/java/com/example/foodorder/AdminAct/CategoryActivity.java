package com.example.foodorder.AdminAct;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.foodorder.Activity.BaseActivity;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.Helper1.SpaceItem;
import com.example.foodorder.databinding.ActivityCategoryBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.CateMnAdapter;
import com.example.foodorder.Domain.Category;
import com.example.foodorder.Domain.Supplier;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.foodorder.R;

public class CategoryActivity extends BaseActivity {

    ActivityCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initList();
        initSpinner();
        setVariable();
    }
    private void addCategory() {
        String cateName = binding.cateNameTxt.getText().toString().trim();
        int selectedPosition = binding.supSpinner.getSelectedItemPosition();

        if (cateName.isEmpty()) {
            Toast.makeText(this, "Điền tên danh mục mới", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference supplierRef = FirebaseDatabase.getInstance().getReference("Supplier");
        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Supplier> suppliers = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Supplier supplier = data.getValue(Supplier.class);
                    if (supplier != null) suppliers.add(supplier);
                }
                if (selectedPosition < 0 || selectedPosition >= suppliers.size()) {
                    Toast.makeText(CategoryActivity.this, "Invalid supplier selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                int idSup = suppliers.get(selectedPosition).getIdSup();

                // Find max Id in Category
                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");
                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int maxId = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Category category = data.getValue(Category.class);
                            if (category != null && category.getId() > maxId) {
                                maxId = category.getId();
                            }
                        }
                        int newId = maxId + 1;

                        Category newCategory = new Category();
                        newCategory.setId(newId);
                        newCategory.setName(cateName);
                        newCategory.setIdSup(idSup);

                        categoryRef.child(String.valueOf(newId)).setValue(newCategory)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(CategoryActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                                    initList();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CategoryActivity.this, "Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(CategoryActivity.this, "Category fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CategoryActivity.this, "Supplier fetch failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCategory(){
        CateMnAdapter adapter = (CateMnAdapter) binding.cateList.getAdapter();
        if (adapter == null) return;

        Category selectedCategory = adapter.getSelectedSupplier();
        if (selectedCategory == null) {
            Toast.makeText(this, "Không có danh mục nào được chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set info to EditText and Spinner
        binding.cateNameTxt.setText(selectedCategory.getName());

        // Find supplier position in spinner
        DatabaseReference supplierRef = FirebaseDatabase.getInstance().getReference("Supplier");
        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Supplier> suppliers = new ArrayList<>();
                int selectedSupplierPos = 0;
                int i = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    Supplier supplier = data.getValue(Supplier.class);
                    if (supplier != null) {
                        suppliers.add(supplier);
                        if (supplier.getIdSup() == selectedCategory.getIdSup()) {
                            selectedSupplierPos = i;
                        }
                        i++;
                    }
                }
                binding.supSpinner.setSelection(selectedSupplierPos);

                // After user edits and clicks save, update Firebase
                binding.cateAddNew.setVisibility(View.GONE);
                binding.cateUpdate.setVisibility(View.VISIBLE);

                binding.cateUpdate.setOnClickListener(v -> {
                    String newName = binding.cateNameTxt.getText().toString().trim();
                    int newSupPos = binding.supSpinner.getSelectedItemPosition();
                    if (newName.isEmpty()) {
                        Toast.makeText(CategoryActivity.this, "Chọn 1 danh mục cần sửa", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newIdSup = suppliers.get(newSupPos).getIdSup();

                    Category updatedCategory = new Category();
                    updatedCategory.setId(selectedCategory.getId());
                    updatedCategory.setName(newName);
                    updatedCategory.setIdSup(newIdSup);

                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");
                    categoryRef.child(String.valueOf(selectedCategory.getId())).setValue(updatedCategory)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(CategoryActivity.this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                                initList();
                                binding.cateNameTxt.setText("");
                                binding.cateAddNew.setVisibility(View.VISIBLE);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CategoryActivity.this, "Chỉnh sửa thành công thất bại", Toast.LENGTH_SHORT).show();
                            });
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void deleteCategory() {
        CateMnAdapter adapter = (CateMnAdapter) binding.cateList.getAdapter();
        if (adapter == null) return;

        Category selectedCategory = adapter.getSelectedSupplier();
        if (selectedCategory == null) {
            Toast.makeText(this, "Chưa có danh mục nào được chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = selectedCategory.getId();

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");
        categoryRef.child(String.valueOf(categoryId)).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CategoryActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                    initList(); // Refresh list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CategoryActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                });
    }
    private void initSpinner() {
        DatabaseReference supplierRef = FirebaseDatabase.getInstance().getReference("Supplier");
        ArrayList<String> supplierNames = new ArrayList<>();

        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                supplierNames.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Supplier supplier = data.getValue(Supplier.class);
                    if (supplier != null) {
                        supplierNames.add(supplier.getSupName()); // or getName() if your field is Name
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CategoryActivity.this,
                        android.R.layout.simple_spinner_item,
                        supplierNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.supSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    private void initList() {
        //Tạo khoảng cách giũa các item
        int spaceInDp = 5;
        float scale = getResources().getDisplayMetrics().density;
        int spaceInPx = (int) (spaceInDp * scale + 0.5f);

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category");
        DatabaseReference supplierRef = FirebaseDatabase.getInstance().getReference("Supplier");

        ArrayList<Category> cateList = new ArrayList<>();
        Map<Integer, Supplier> supplierMap = new HashMap<>();

        // Fetch suppliers first
        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Supplier supplier = data.getValue(Supplier.class);
                    if (supplier != null) {
                        supplierMap.put(supplier.getIdSup(), supplier);
                    }
                }
                // After suppliers, fetch categories
                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        cateList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Category category = data.getValue(Category.class);
                            if (category != null) {
                                cateList.add(category);
                            }
                        }
                        CateMnAdapter adapter = new CateMnAdapter(cateList, supplierMap);
                        binding.cateList.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
                        binding.cateList.addItemDecoration(new SpaceItem(spaceInPx));
                        binding.cateList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    private void setVariable() {
        binding.cateBackBtn.setOnClickListener(v -> finish());

        binding.cateAddNew.setOnClickListener(v -> addCategory());

        binding.cateUpdate.setOnClickListener(v -> updateCategory());

        binding.cateDeleted.setOnClickListener(v -> new AlertDialog.Builder(CategoryActivity.this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xoá danh mục món ăn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    deleteCategory();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show());


    }

}

