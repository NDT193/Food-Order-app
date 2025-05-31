package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private ArrayList<Foods> item;
    Context context;

    public PaymentAdapter(ArrayList<Foods> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_payment, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {

        holder.titleTxt.setText(item.get(position).getTitle());
        holder.eachItemTxt.setText("VND"+item.get(position).getNumberInCart()*item.get(position).getPrice());
        holder.totalPayTxt.setText(item.get(position).getNumberInCart()+" * VND"+(
                item.get(position).getPrice()));

        Glide.with(context)
                .load(item.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, totalPayTxt, eachItemTxt;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.tittleholderPaymentTxt);
            totalPayTxt = itemView.findViewById(R.id.feeEachItemholderPaymentTxt);
            eachItemTxt = itemView.findViewById(R.id.totalEachItemholderPaymentTxt);
            pic = itemView.findViewById(R.id.imgholderPayment);
        }
    }
}
