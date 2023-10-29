package com.educando.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder> {
    private List<Contacto> mensajes;

    public MensajesAdapter(List<Contacto> mensajes) {
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        Contacto mensaje = mensajes.get(position);

        holder.tituloTextView.setText(mensaje.getTitulo());
        holder.mensajeTextView.setText(mensaje.getMensaje());
        holder.fechaTextView.setText(mensaje.getFecha());
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    // Esta función te permitirá actualizar los mensajes en el adaptador
    public void setMensajes(List<Contacto> nuevosMensajes) {
        mensajes.clear();
        mensajes.addAll(nuevosMensajes);
        notifyDataSetChanged();
    }

    public static class MensajeViewHolder extends RecyclerView.ViewHolder {
        public TextView tituloTextView;
        public TextView mensajeTextView;
        public TextView fechaTextView;

        public MensajeViewHolder(View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            mensajeTextView = itemView.findViewById(R.id.mensajeTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
        }
    }
}

