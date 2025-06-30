package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Domain.GeneralBill;
import com.example.foodorder.R;
import java.util.ArrayList;

public class OrderMnAdapter extends RecyclerView.Adapter<OrderMnAdapter.Viewholder> {
    private ArrayList<GeneralBill> list;
    Context context;
    private int selectedPosition = -1;

    public OrderMnAdapter(ArrayList<GeneralBill> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderMnAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_order_mn, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderMnAdapter.Viewholder holder, int position) {
        holder.dateTxt.setText(list.get(position).getDate());
        holder.totalTxt.setText(String.valueOf(list.get(position).getPrice()));

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
        return list.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
    public GeneralBill selectedBill() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView dateTxt, totalTxt;
        CheckBox checkBox;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            dateTxt= itemView.findViewById(R.id.dateOrderVh);
            totalTxt= itemView.findViewById(R.id.totalOrderVh);
            checkBox= itemView.findViewById(R.id.checkboxOrderVh);
        }
    }
}
