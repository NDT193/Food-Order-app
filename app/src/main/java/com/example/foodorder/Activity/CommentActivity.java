package com.example.foodorder.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.Adapter.CommentAdapter;
import com.example.foodorder.Domain.Comment;
import com.example.foodorder.Helper1.SpaceItem;
import com.example.foodorder.databinding.ActivityCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class CommentActivity extends BaseActivity {
    private String FoodId;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        FoodId = getIntent().getStringExtra("FoodId");
        binding.comView.setLayoutManager(new LinearLayoutManager(this));

        initList();
        loadUser();
        setVariable();
    }

    private void setVariable() {
        binding.comBackBtn.setOnClickListener(v -> finish());

        binding.comPushBtn.setOnClickListener(v -> addNewComment());

        binding.comDeleteBtn.setOnClickListener(v -> deleteComment());
    }

    private void addNewComment() {
        String commentText = binding.comTxt.getText().toString();
        String StarTxt = binding.comStarTxt.getText().toString();
        String name = binding.comUserName.getText().toString();
        String userId = mAuth.getUid();

        if (commentText.isEmpty() || StarTxt.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!isValidStarRating(StarTxt)) {
            Toast.makeText(this, "Please enter a rating between 1 and 5", Toast.LENGTH_SHORT).show();
        } else {
            Comment newComment = new Comment(userId, commentText, Double.parseDouble(StarTxt), Integer.parseInt(FoodId), name);
            database.getReference("Comment")
                    .child(FoodId)
                    .child(userId)
                    .push()
                    .setValue(newComment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                        binding.comTxt.setText("");
                        binding.comStarTxt.setText("");
                        calculateAverageStar();
                        initList();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to add comment", Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteComment() {
        Set<String> selectedKeys = commentAdapter.getSelectedKeys();
        String userId = mAuth.getUid();

        if (selectedKeys != null && !selectedKeys.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete the selected comment ?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        for (String key : selectedKeys) {
                            database.getReference("Comment")
                                    .child(FoodId)
                                    .child(userId)
                                    .child(key)
                                    .removeValue();
                        }
                        Toast.makeText(this, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                        calculateAverageStar();
                        initList();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Toast.makeText(this, "Please select at least one comment to delete", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidStarRating(String rating) {
        try {
            double stars = Double.parseDouble(rating);
            return stars >= 1.0 && stars <= 5.0 && (stars * 2) % 1 == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void loadUser() {
        String email = mAuth.getCurrentUser().getEmail();
        if (email != null) {
            String emailKey = email.replace(".", ",");
            database.getReference("Account").child(emailKey).child("Name")
                    .get().addOnSuccessListener(dataSnapshot -> {
                        String name = dataSnapshot.getValue(String.class);
                        if (name != null) {
                            binding.comUserName.setText(name);
                        }
                    });
        }
    }

    private void initList() {
        final int[] pos = {0};
        int spaceInDp = 3;
        float scale = getResources().getDisplayMetrics().density;
        int spaceInPx = (int) (spaceInDp * scale + 0.5f);

        commentList.clear();
        commentAdapter = new CommentAdapter(commentList, mAuth.getUid());
        binding.comView.addItemDecoration(new SpaceItem(spaceInPx));
        binding.comView.setAdapter(commentAdapter);

        database.getReference("Comment")
                .child(FoodId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot commentSnap : userSnapshot.getChildren()) {
                                Comment comment = commentSnap.getValue(Comment.class);
                                if (comment != null) {
                                    commentList.add(comment);
                                    commentAdapter.setCommentKey(pos[0], commentSnap.getKey());
                                    pos[0]++;
                                }

                            }
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    private void calculateAverageStar() {
        FirebaseDatabase.getInstance().getReference("Comment")
                .child(FoodId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double totalStars = 0;
                        int count = 0;
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot commentSnap : userSnapshot.getChildren()) {
                                Comment comment = commentSnap.getValue(Comment.class);
                                if (comment != null) {
                                    totalStars += comment.getStar();
                                    count++;
                                }
                            }
                        }
                        double average = count > 0 ? totalStars / count : 0;
                        Log.d("AverageStar", "Average star for food " + FoodId + ": " + average);

                        FirebaseDatabase.getInstance().getReference("Foods")
                                .child(FoodId)
                                .child("Star")
                                .setValue(average);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

}