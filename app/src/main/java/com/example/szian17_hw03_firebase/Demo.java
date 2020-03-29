package com.example.szian17_hw03_firebase;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Demo {

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    });

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
            .setQuery(mDatabase,User.class)
            .build();

FirebaseRecyclerAdapter<User, UserActivity.UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserActivity.UserViewHolder>(options) {
    @Override
    protected void onBindViewHolder(@NonNull UserActivity.UserViewHolder holder, int position, @NonNull User model) {

    }

    @NonNull
    @Override
    public UserActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
};
}
