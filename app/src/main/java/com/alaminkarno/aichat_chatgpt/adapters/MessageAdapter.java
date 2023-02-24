package com.alaminkarno.aichat_chatgpt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alaminkarno.aichat_chatgpt.R;
import com.alaminkarno.aichat_chatgpt.model.Message;
import com.alaminkarno.aichat_chatgpt.utils.AppConstants;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Message message = messages.get(position);

        if(message.getSendBy().equals(AppConstants.SEND_BY_ME)){
            holder.botLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.senderTV.setText(message.getMessage());
        }else{
            holder.senderLayout.setVisibility(View.GONE);
            holder.botLayout.setVisibility(View.VISIBLE);
            holder.botTV.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderTV,botTV;
        LinearLayout senderLayout,botLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderTV = itemView.findViewById(R.id.sender_messageTV);
            botTV = itemView.findViewById(R.id.bot_messageTV);
            senderLayout = itemView.findViewById(R.id.senderLayout);
            botLayout = itemView.findViewById(R.id.botLayout);
        }
    }
}
