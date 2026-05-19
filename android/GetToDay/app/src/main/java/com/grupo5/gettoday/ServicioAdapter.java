package com.grupo5.gettoday;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grupo5.gettoday.databinding.ItemServicioNegocioBinding;
import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    public interface OnServicioClickListener {
        void onEditar(Servicio servicio);
        void onEliminar(Servicio servicio);
    }

    private List<Servicio> lista;
    private final OnServicioClickListener listener;

    public ServicioAdapter(List<Servicio> lista, OnServicioClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<Servicio> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicioNegocioBinding binding = ItemServicioNegocioBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ServicioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio s = lista.get(position);

        holder.binding.tvNombreServicio.setText(s.getNombreServicio());
        holder.binding.tvPrecioServicio.setText(String.format("%.0f€", s.getPrecio()));

        // tvDescripcionServicio existe en el item pero el servidor no lo devuelve,
        // lo ocultamos para que no quede raro con el placeholder
        holder.binding.tvDescripcionServicio.setText("");

        holder.binding.btnEditarServicio.setOnClickListener(v -> listener.onEditar(s));
        holder.binding.btnEliminarServicio.setOnClickListener(v -> listener.onEliminar(s));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        final ItemServicioNegocioBinding binding;

        ServicioViewHolder(ItemServicioNegocioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}