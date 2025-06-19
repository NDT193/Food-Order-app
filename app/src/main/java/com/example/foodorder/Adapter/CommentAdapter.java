package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorder.Domain.Comment;
import com.example.foodorder.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewholder> {
    private ArrayList<Comment> list;
    private String currentUserId;
    private int selectedPosition = -1;
    private Map<Integer, String> commentKeyMap = new HashMap<>();
    private Set<String> selectedKeys = new HashSet<>();
    Context context;

    public CommentAdapter(ArrayList<Comment> list, String currentUserId) {
        this.list = list;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public CommentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Viewholder holder, int position) {
        holder.userNameTxt.setText(list.get(position).getUser());
        holder.commentTxt.setText(list.get(position).getValued());
        holder.starTxt.setText("" + list.get(position).getStar());


        // Show checkbox only if current user is the owner
        Comment comment = list.get(position);
        if (comment.getUid() != null && comment.getUid().equals(currentUserId)) {
            holder.checkBox.setVisibility(View.VISIBLE);
            String key = commentKeyMap.get(position);
            holder.checkBox.setChecked(selectedKeys.contains(key));
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (key != null) {
                    if (isChecked) {
                        selectedKeys.add(key);
                    } else {
                        selectedKeys.remove(key);
                    }
                }
            });
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setOnCheckedChangeListener(null);
        }
//        if (comment.getUid() != null && comment.getUid().equals(currentUserId)) {
//            holder.checkBox.setVisibility(View.VISIBLE);
//        } else {
//            holder.checkBox.setVisibility(View.GONE);
//        }
//
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                int oldPosition = selectedPosition;
//                selectedPosition = holder.getAdapterPosition();
//                notifyItemChanged(oldPosition);
//                notifyItemChanged(selectedPosition);
//            } else if (selectedPosition == holder.getAdapterPosition()) {
//                selectedPosition = -1;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setCommentKey(int position, String key) {
        commentKeyMap.put(position, key);
    }
    public Set<String> getSelectedKeys() {
        return selectedKeys;
    }

    public Comment getSelectedComment() {
        if (selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        }
        return null;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView userNameTxt, commentTxt, starTxt;
        CheckBox checkBox;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            userNameTxt = itemView.findViewById(R.id.userVhTxt);
            commentTxt = itemView.findViewById(R.id.comVhTxt);
            starTxt = itemView.findViewById(R.id.rateVhTxt);
            checkBox = itemView.findViewById(R.id.checkVh);
        }
    }
}
