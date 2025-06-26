package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Domain.Category;
import com.example.foodorder.Domain.Supplier;
import com.example.foodorder.R;

import java.util.ArrayList;
import java.util.Map;

public class CateMnAdapter extends RecyclerView.Adapter<CateMnAdapter.ViewHolder> {

    private ArrayList<Category> cateList;
    private Map<Integer, Supplier> supplierMap;

    private int selectedPosition = -1;
    Context context;

    public CateMnAdapter(ArrayList<Category> cateList, Map<Integer, Supplier> supplierMap) {
        this.cateList = cateList;
        this.supplierMap = supplierMap;
    }

    @NonNull
    @Override
    public CateMnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_catewithsup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateMnAdapter.ViewHolder holder, int position) {
        Category category = cateList.get(position);
        holder.tvCategoryName.setText(category.getName());

        // Lấy Supplier dựa trên IdSupp
        Supplier supplier = supplierMap.get(category.getIdSup());
        if (supplier != null) {
            holder.tvSupplierName.setText(supplier.getSupName());
        } else {
            holder.tvSupplierName.setText("Unknown Supplier");
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(position == selectedPosition);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int oldPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
            } else if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
            }
        });

    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
    public Category getSelectedSupplier() {
        if (selectedPosition >= 0 && selectedPosition < cateList.size()) {
            return cateList.get(selectedPosition);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvSupplierName;
        ImageView pic;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.cateNameVh);
            tvSupplierName = itemView.findViewById(R.id.supNameVh);
            pic = itemView.findViewById(R.id.cwsImg);
            checkBox = itemView.findViewById(R.id.cwsCheckbox);
        }
    }
}


