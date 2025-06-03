package com.example.foodorder.Adapter;

import static android.view.View.GONE;

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
import com.example.foodorder.AdminAct.FoodActivity;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;

import java.util.ArrayList;

public class FoodAdapter  extends RecyclerView.Adapter<FoodAdapter.Viewholder> {
    private ArrayList<Foods> list = new ArrayList<>();
    Context context;

    public FoodAdapter(ArrayList<Foods> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FoodAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_payment,parent,false);
        return new Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.Viewholder holder, int position) {
        holder.titleTxt.setText(list.get(position).getTitle());
        holder.eachItemTxt.setText(list.get(position).getPrice()+"VND");
        holder.totalPayTxt.setVisibility(GONE);
        Glide.with(context)
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, totalPayTxt, eachItemTxt;
        ImageView pic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.tittleholderPaymentTxt);
            totalPayTxt = itemView.findViewById(R.id.feeEachItemholderPaymentTxt);
            eachItemTxt = itemView.findViewById(R.id.totalEachItemholderPaymentTxt);
            pic = itemView.findViewById(R.id.avatarImg);
        }
    }
}
