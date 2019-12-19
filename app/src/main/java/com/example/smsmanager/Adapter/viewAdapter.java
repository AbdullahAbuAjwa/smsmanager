package com.example.smsmanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsmanager.R;
import com.example.smsmanager.model.Message;
import com.example.smsmanager.model.ViewMessage;

import java.util.ArrayList;

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.ViewViewHolder> {

    ArrayList<ViewMessage> viewMessages;

    public viewAdapter(ArrayList<ViewMessage> viewMessages) {
        this.viewMessages = viewMessages;
    }

    @NonNull
    @Override
    public ViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cusomview, null, false);
        return new viewAdapter.ViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewViewHolder holder, int position) {
        ViewMessage viewMessage = viewMessages.get(position);
        holder.tv_message.setText(viewMessage.getTextMessage());
    }

    @Override
    public int getItemCount() {
        return viewMessages.size();
    }

    public void searchList(ArrayList<ViewMessage> newList) {
        viewMessages = new ArrayList<>();
        viewMessages.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;

        public ViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.custom_view_sms);

        }
    }
}

