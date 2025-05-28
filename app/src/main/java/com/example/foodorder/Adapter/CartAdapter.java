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
import com.example.foodorder.Helper.ChangeNumberItemsListener;
import com.example.foodorder.Helper.ManagmentCart;
import com.example.foodorder.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeListener;

    public CartAdapter(ArrayList<Foods> list, Context context , ChangeNumberItemsListener changeListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeListener = changeListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.titleTxt.setText(list.get(position).getTitle());
        holder.feeEachitemTxt.setText("$"+list.get(position).getNumberInCart()*list.get(position).getPrice());
        holder.totalEachItemTxt.setText(list.get(position).getNumberInCart()+" * $"+(
                list.get(position).getPrice()));
        holder.num.setText(list.get(position).getNumberInCart()+"");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> managmentCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeListener.change();
        }
        ));

        holder.minusItem.setOnClickListener(v -> managmentCart.minusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeListener.change();
        }
        ));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeEachitemTxt, plusItem, minusItem, totalEachItemTxt, num;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.tittleCartTxt);
            pic = itemView.findViewById(R.id.picCart);
            feeEachitemTxt = itemView.findViewById(R.id.feeEachItemTxt);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItemTxt = itemView.findViewById(R.id.totalEachItemTxt);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
