package com.example.watchapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.example.watchapp.model.User;

import java.util.List;

public class UserListAdapter extends WearableRecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private static final String TAG = "UserListAdapter";
    private List<User> items;
    private boolean sendScreen;
    private String userId;

    public UserListAdapter(List<User> items, boolean send, String userId) {
        this.items = items;
        sendScreen = send;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User item = items.get(position);
        holder.titleTextView.setText(item.name);
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (sendScreen) {
                    intent = new Intent(holder.titleTextView.getContext(), TextMessageSend.class);
                } else {
                    intent = new Intent(holder.titleTextView.getContext(), TextMessageReceive.class);
                }
                intent.putExtra("name", item.name);
                intent.putExtra("id", item.id);
                intent.putExtra("userId", userId);
                holder.titleTextView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends WearableRecyclerView.ViewHolder {
        public TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}

