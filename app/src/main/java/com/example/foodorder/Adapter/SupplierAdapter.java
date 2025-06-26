package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorder.Domain.Supplier;
import com.example.foodorder.R;
import java.util.ArrayList;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.Viewholder> {

    private ArrayList<Supplier> list;
    Context context;

    private int selectedPosition = -1;
    public SupplierAdapter(ArrayList<Supplier> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SupplierAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_supplier, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.Viewholder holder, int position) {
        holder.supplierName.setText(list.get(position).getSupName());

        holder.checkBox.setOnCheckedChangeListener(null);

        // Set checked state based on selectedPosition
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
        return list.size();
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }
    public Supplier getSelectedSupplier() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        EditText supplierName;
        CheckBox checkBox;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            supplierName = itemView.findViewById(R.id.supNameTxt);
            checkBox = itemView.findViewById(R.id.supCheckBox);
        }
    }
}
