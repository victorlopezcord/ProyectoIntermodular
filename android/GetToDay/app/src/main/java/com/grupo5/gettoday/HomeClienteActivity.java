package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.grupo5.gettoday.databinding.ActivityHomeClienteBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClienteActivity extends BaseActivity {

    private ActivityHomeClienteBinding binding;
    private String emailCliente;

    private final List<JsonObject> reservas = new ArrayList<>();
    private ReservaClienteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeClienteBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuCliente(R.id.navHome);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailCliente = prefs.getString("usuario_email", "");

        configurarRecycler();
        configurarBotones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosCliente();
        cargarReservas();
    }

    private void cargarDatosCliente() {
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        binding.tvNombreCliente.setText(prefs.getString("usuario_nombre_" + emailCliente, ""));
        binding.tvEmailCliente.setText(prefs.getString("usuario_email", ""));
        binding.tvTelefonoCliente.setText(prefs.getString("usuario_telefono_" + emailCliente, ""));
    }

    private void configurarRecycler() {
        adapter = new ReservaClienteAdapter(reservas);
        binding.recyclerReservas.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerReservas.setAdapter(adapter);
    }

    private void cargarReservas() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.citasCliente(new PeticionCita(emailCliente))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call,
                                           Response<RespuestaGeneral> response) {
                        reservas.clear();
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            JsonElement datos = response.body().getDatos();
                            if (datos != null && datos.isJsonArray()) {
                                for (JsonElement elem : datos.getAsJsonArray()) {
                                    reservas.add(elem.getAsJsonObject());
                                }
                            }
                        }

                        if (reservas.isEmpty()) {
                            binding.tvSinReservas.setVisibility(View.VISIBLE);
                            binding.recyclerReservas.setVisibility(View.GONE);
                        } else {
                            binding.tvSinReservas.setVisibility(View.GONE);
                            binding.recyclerReservas.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(HomeClienteActivity.this,
                                "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configurarBotones() {
        binding.btnEditarPerfil.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilClienteActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    class ReservaClienteAdapter extends RecyclerView.Adapter<ReservaClienteAdapter.VH> {

        private final List<JsonObject> lista;

        ReservaClienteAdapter(List<JsonObject> lista) {
            this.lista = lista;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reserva_cliente, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            JsonObject cita = lista.get(position);

            holder.tvNombreNegocio.setText(str(cita, "nombreLocal", "—"));
            holder.tvServicio.setText(str(cita, "nombreServicio", "—"));
            holder.tvFecha.setText(formatFecha(str(cita, "fecha", "—")));
            holder.tvHora.setText(formatHora(str(cita, "horaInicio", "—")));

            String estado = str(cita, "estado", "pendiente");
            holder.tvEstado.setText(capitalize(estado));
            aplicarColorEstado(holder.tvEstado, estado);
        }

        @Override
        public int getItemCount() {
            return lista.size();
        }

        class VH extends RecyclerView.ViewHolder {
            TextView tvNombreNegocio, tvEstado, tvServicio, tvFecha, tvHora;

            VH(View v) {
                super(v);
                tvNombreNegocio = v.findViewById(R.id.tvNombreNegocio);
                tvEstado        = v.findViewById(R.id.tvEstado);
                tvServicio      = v.findViewById(R.id.tvServicio);
                tvFecha         = v.findViewById(R.id.tvFecha);
                tvHora          = v.findViewById(R.id.tvHora);

                View rowEmpleado = v.findViewById(R.id.tvEmpleado);
                if (rowEmpleado != null) {
                    if (rowEmpleado.getParent() instanceof View) {
                        ((View) rowEmpleado.getParent()).setVisibility(View.GONE);
                    }
                }
            }
        }

        private String str(JsonObject obj, String key, String def) {
            if (obj.has(key) && !obj.get(key).isJsonNull()) {
                return obj.get(key).getAsString();
            }
            return def;
        }

        private String formatFecha(String f) {
            if (f.length() >= 10) {
                return f.substring(0, 10);
            }
            return f;
        }

        private String formatHora(String h) {
            if (h.length() >= 5) {
                return h.substring(0, 5);
            }
            return h;
        }

        private String capitalize(String s) {
            if (s == null || s.isEmpty()) {
                return s;
            }
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }

        private void aplicarColorEstado(TextView tv, String estado) {
            int texto;
            int fondo;
            if (estado.equals("confirmada")) {
                texto = getColor(R.color.estado_confirmada);
                fondo = getColor(R.color.estado_confirmada_bg);
            } else if (estado.equals("cancelada")) {
                texto = getColor(R.color.estado_cancelada);
                fondo = getColor(R.color.estado_cancelada_bg);
            } else {
                // pendiente
                texto = getColor(R.color.estado_pendiente);
                fondo = getColor(R.color.estado_pendiente_bg);
            }
            tv.setTextColor(texto);
            tv.setBackgroundTintList(ColorStateList.valueOf(fondo));
        }
    }
}