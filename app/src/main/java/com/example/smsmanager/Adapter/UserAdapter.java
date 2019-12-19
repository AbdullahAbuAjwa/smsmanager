package com.example.smsmanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsmanager.model.Message;
import com.example.smsmanager.R;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    ArrayList<Message> messages;
    private OnMessageListener mOnMessageListener;

    public UserAdapter(ArrayList<Message> messages, OnMessageListener onMessageListener) {
        this.messages = messages;
        this.mOnMessageListener = onMessageListener;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message, null, false);
        return new UserViewHolder(v, mOnMessageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tv_name.setText(message.getName());
        holder.tv_text.setText(message.getText());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void searchList(ArrayList<Message> newList) {
        messages = new ArrayList<>();
        messages.addAll(newList);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_text;
        OnMessageListener messageListener;

        public UserViewHolder(@NonNull View itemView, OnMessageListener messageListener) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.custom_tv_name);
            tv_text = itemView.findViewById(R.id.custom_tv_text);
            this.messageListener = messageListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            messageListener.onMessageClick(getAdapterPosition());
        }
    }

    public interface OnMessageListener {
        void onMessageClick(int position);
    }
}


