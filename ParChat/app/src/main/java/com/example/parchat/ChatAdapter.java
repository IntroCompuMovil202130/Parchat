package com.example.parchat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parchat.databinding.MensajeEmisorBinding;
import com.example.parchat.databinding.MensajeReceptorBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MensajeChat> mensajesChats;
    private final String emisorId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<MensajeChat> mensajesChats, String emisorId) {
        this.mensajesChats = mensajesChats;
        this.emisorId = emisorId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( viewType == VIEW_TYPE_SENT ){
            return new SentMessageViewHolder(
                    MensajeEmisorBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    )
            );
        }else{
            return new ReceivedMessageViewHolder(
                    MensajeReceptorBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            (( SentMessageViewHolder) holder).setData(mensajesChats.get(position));
        }else{
            (( ReceivedMessageViewHolder ) holder).setData(mensajesChats.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mensajesChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if( mensajesChats.get(position).emisorId.equals(emisorId) ){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final MensajeEmisorBinding binding;

        public SentMessageViewHolder(MensajeEmisorBinding mensajeEmisorBinding) {
            super(mensajeEmisorBinding.getRoot());
            binding = mensajeEmisorBinding;
        }

        void setData(MensajeChat mensajeChat){
            binding.mensajeEmisor.setText(mensajeChat.mensaje);
            binding.textDateTime.setText(mensajeChat.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final MensajeReceptorBinding binding;

        public ReceivedMessageViewHolder(MensajeReceptorBinding mensajeReceptorBinding) {
            super(mensajeReceptorBinding.getRoot());
            binding = mensajeReceptorBinding;
        }

        void setData(MensajeChat mensajeChat){
            binding.mensajeReceptor.setText(mensajeChat.mensaje);
            binding.textDateTime.setText(mensajeChat.dateTime);
        }
    }
}