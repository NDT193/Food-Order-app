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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;
import java.util.ArrayList;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.Viewholder> {
    ArrayList<Foods> list;
    Context context;
    private int selectedPosition = -1;

    public FavListAdapter(ArrayList<Foods> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FavListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_fav_list, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavListAdapter.Viewholder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText("VND " + list.get(position).getPrice());
        holder.star.setText("" + list.get(position).getStar());

        Glide.with(context)
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.checkbox.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Foods getSelectedFood() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, price, star;
        ImageView pic;
        CheckBox checkbox;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleFavTxt);
            price = itemView.findViewById(R.id.priceFavTxt);
            pic = itemView.findViewById(R.id.avatarFav);
            checkbox = itemView.findViewById(R.id.checkBoxFav);
            star = itemView.findViewById(R.id.starFavTxt);
        }
    }
}
