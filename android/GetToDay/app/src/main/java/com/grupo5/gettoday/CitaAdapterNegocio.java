package com.grupo5.gettoday;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

public class CitaAdapterNegocio extends RecyclerView.Adapter<CitaAdapterNegocio.VH> {

    public interface OnCitaAccion {
        void onAccion(int idCita, String nuevoEstado, int position);
    }

    private final List<JsonObject> citas;
    private final boolean          mostrarBotones;
    private final OnCitaAccion     listener;

    public CitaAdapterNegocio(List<JsonObject> citas, boolean mostrarBotones, OnCitaAccion listener) {
        this.citas          = citas;
        this.mostrarBotones = mostrarBotones;
        this.listener       = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva_negocio, parent, false);
        return new VH(view, mostrarBotones);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        JsonObject cita  = citas.get(position);
        int        idCita = cita.has("idCita") ? cita.get("idCita").getAsInt() : -1;

        holder.tvNombreCliente.setText(str(cita, "nombreCliente", "—"));
        holder.tvServicio.setText(str(cita, "nombreServicio", "—"));

        // Fecha: el servidor devuelve "2025-05-19T00:00:00.000+..." → quedarse con los 10 primeros
        String fecha = str(cita, "fecha", "—");
        holder.tvFecha.setText(fecha.length() >= 10 ? fecha.substring(0, 10) : fecha);

        // Hora: "09:00:00" → mostrar "09:00"
        String hora = str(cita, "horaInicio", "—");
        holder.tvHora.setText(hora.length() >= 5 ? hora.substring(0, 5) : hora);

        if (mostrarBotones && holder.btnAceptar != null && holder.btnCancelar != null) {
            holder.btnAceptar.setOnClickListener(v -> {
                if (listener != null)
                    listener.onAccion(idCita, "confirmada", holder.getAdapterPosition());
            });
            holder.btnCancelar.setOnClickListener(v -> {
                if (listener != null)
                    listener.onAccion(idCita, "cancelada", holder.getAdapterPosition());
            });
        }
    }

    private String str(JsonObject obj, String key, String def) {
        return (obj.has(key) && !obj.get(key).isJsonNull()) ? obj.get(key).getAsString() : def;
    }

    @Override
    public int getItemCount() { return citas.size(); }

    // ── ViewHolder ────────────────────────────────────────────────

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombreCliente, tvServicio, tvFecha, tvHora;
        Button   btnAceptar, btnCancelar;

        VH(View itemView, boolean mostrarBotones) {
            super(itemView);
            tvNombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            tvServicio      = itemView.findViewById(R.id.tvServicio);
            tvFecha         = itemView.findViewById(R.id.tvFecha);
            tvHora          = itemView.findViewById(R.id.tvHora);

            if (mostrarBotones) {
                // La card tiene: MaterialCardView > LinearLayout (main)
                LinearLayout mainLayout = (LinearLayout) ((ViewGroup) itemView).getChildAt(0);
                int dp = (int) itemView.getContext().getResources().getDisplayMetrics().density;

                // Línea separadora antes de los botones
                View sep = new View(itemView.getContext());
                LinearLayout.LayoutParams sepParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                sepParams.setMargins(0, 12 * dp, 0, 12 * dp);
                sep.setLayoutParams(sepParams);
                sep.setBackgroundColor(Color.parseColor("#E0E0E0"));
                mainLayout.addView(sep);

                // Fila horizontal con los dos botones
                LinearLayout fila = new LinearLayout(itemView.getContext());
                fila.setOrientation(LinearLayout.HORIZONTAL);
                fila.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Botón ACEPTAR (verde)
                btnAceptar = new Button(itemView.getContext());
                LinearLayout.LayoutParams pAceptar = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                pAceptar.setMargins(0, 0, 6 * dp, 0);
                btnAceptar.setLayoutParams(pAceptar);
                btnAceptar.setText("Aceptar");
                btnAceptar.setBackgroundColor(Color.parseColor("#2E7D32"));
                btnAceptar.setTextColor(Color.WHITE);
                btnAceptar.setTextSize(13f);

                // Botón CANCELAR (rojo)
                btnCancelar = new Button(itemView.getContext());
                LinearLayout.LayoutParams pCancelar = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                pCancelar.setMargins(6 * dp, 0, 0, 0);
                btnCancelar.setLayoutParams(pCancelar);
                btnCancelar.setText("Cancelar");
                btnCancelar.setBackgroundColor(Color.parseColor("#C62828"));
                btnCancelar.setTextColor(Color.WHITE);
                btnCancelar.setTextSize(13f);

                fila.addView(btnAceptar);
                fila.addView(btnCancelar);
                mainLayout.addView(fila);
            }
        }
    }
}