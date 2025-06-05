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
import com.example.foodorder.AdminAct.FoodActivity;
import com.example.foodorder.Domain.Account;
import com.example.foodorder.Domain.Foods;
import com.example.foodorder.R;

import java.util.ArrayList;

public class UserMnAdapter extends RecyclerView.Adapter<UserMnAdapter.Viewholer> {
    private ArrayList<Account> list = new ArrayList<>();
    private Context context;
    private int selectedPosition = -1;

    public UserMnAdapter(ArrayList<Account> list) {
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public UserMnAdapter.Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user_mn, parent, false);
        return new Viewholer(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMnAdapter.Viewholer holder, int position) {

//        holder.nameTxt.setText(list.get(position).getName());
//        holder.emailTxt.setText(list.get(position).getEmail());
//        holder.checkbox.setChecked(position == selectedPosition);

        try {
            if (position >= 0 && position < list.size()) {
                Account account = list.get(position);
                if (account != null) {
                    holder.nameTxt.setText(account.getName() != null ? account.getName() : "");
                    holder.emailTxt.setText(account.getEmail() != null ? account.getEmail() : "");
                    holder.checkbox.setChecked(position == selectedPosition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.checkbox.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
        });

    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Account getSelectedAccount() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholer extends RecyclerView.ViewHolder {
        TextView nameTxt, emailTxt;
        CheckBox checkbox;
        public Viewholer(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.userTitleTxt);
            emailTxt = itemView.findViewById(R.id.userEmailTxt);
            checkbox = itemView.findViewById(R.id.usercheckBox);
        }
    }
}

