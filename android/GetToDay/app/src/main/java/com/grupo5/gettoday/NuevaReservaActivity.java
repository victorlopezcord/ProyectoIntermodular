package com.grupo5.gettoday;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.grupo5.gettoday.databinding.ActivityNuevaReservaBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaReservaActivity extends BaseActivity {

    private ActivityNuevaReservaBinding binding;
    private String emailUsuario;

    private final List<String>  nombresNegocios       = new ArrayList<>();
    private final List<Integer> idsNegocios           = new ArrayList<>();
    private final List<String>  direccionesNegocios   = new ArrayList<>();
    private final List<String>  descripcionesNegocios = new ArrayList<>();
    private final List<String>  nombresServicios      = new ArrayList<>();
    private final List<Integer> idsServicios          = new ArrayList<>();

    private int    idNegocioSeleccionado  = -1;
    private int    idServicioSeleccionado = -1;
    private String fechaSeleccionada      = "";
    private String horaSeleccionada       = "";

    private static final List<String> HUECOS_DIA = Arrays.asList(
            "09:00:00", "10:00:00", "11:00:00", "12:00:00", "13:00:00",
            "17:00:00", "18:00:00", "19:00:00"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());
        mostrarMenuCliente(R.id.navNuevaReserva);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailUsuario = prefs.getString("usuario_email", "");

        cargarNegocios();
        configurarSpinnerNegocio();
        configurarSpinnerServicio();
        configurarBtnReservar();
    }

    // PASO 1: NEGOCIO

    private void cargarNegocios() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.listarNegocios().enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isExito()) return;
                nombresNegocios.clear();
                idsNegocios.clear();
                direccionesNegocios.clear();
                descripcionesNegocios.clear();
                JsonElement datos = response.body().getDatos();
                if (datos != null && datos.isJsonArray()) {
                    for (JsonElement elem : datos.getAsJsonArray()) {
                        JsonObject obj = elem.getAsJsonObject();
                        idsNegocios.add(obj.get("idNegocio").getAsInt());
                        nombresNegocios.add(obj.get("nombreLocal").getAsString());

                        String direccion = "";
                        if (obj.has("direccion") && !obj.get("direccion").isJsonNull()) {
                            direccion = obj.get("direccion").getAsString();
                        }
                        direccionesNegocios.add(direccion);

                        String descripcion = "";
                        if (obj.has("descripcion") && !obj.get("descripcion").isJsonNull()) {
                            descripcion = obj.get("descripcion").getAsString();
                        }
                        descripcionesNegocios.add(descripcion);
                    }
                }
                binding.spinnerNegocio.setAdapter(new ArrayAdapter<>(
                        NuevaReservaActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresNegocios));
            }
            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                Toast.makeText(NuevaReservaActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarSpinnerNegocio() {
        binding.spinnerNegocio.setOnItemClickListener((parent, view, position, id) -> {
            idNegocioSeleccionado = idsNegocios.get(position);
            mostrarInfoNegocio(
                    nombresNegocios.get(position),
                    direccionesNegocios.get(position),
                    descripcionesNegocios.get(position));
            cargarServicios(idNegocioSeleccionado);
            mostrarCard(binding.cardInfoNegocio);
            mostrarCard(binding.cardServicio);
            binding.cardHorarios.setVisibility(View.GONE);
            binding.btnReservar.setVisibility(View.GONE);
            binding.spinnerServicio.setText("", false);
            idServicioSeleccionado = -1;
            fechaSeleccionada = "";
            horaSeleccionada = "";
        });
    }

    private void mostrarInfoNegocio(String nombre, String direccion, String descripcion) {
        binding.tvNombreNegocio.setText(nombre);
        if (direccion.isEmpty()) {
            binding.tvDireccionNegocio.setText("—");
        } else {
            binding.tvDireccionNegocio.setText(direccion);
        }
        if (descripcion.isEmpty()) {
            binding.tvDescripcionNegocio.setText("—");
        } else {
            binding.tvDescripcionNegocio.setText(descripcion);
        }
    }

    // PASO 2: SERVICIO

    private void cargarServicios(int idNegocio) {
        nombresServicios.clear();
        idsServicios.clear();
        binding.spinnerServicio.setText("", false);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.listarServiciosPorNegocio(new PeticionServicio(idNegocio, true))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                        if (!response.isSuccessful() || response.body() == null || !response.body().isExito()) return;
                        JsonElement datos = response.body().getDatos();
                        if (datos != null && datos.isJsonArray()) {
                            for (JsonElement elem : datos.getAsJsonArray()) {
                                JsonObject obj = elem.getAsJsonObject();
                                idsServicios.add(obj.get("idServicio").getAsInt());
                                nombresServicios.add(obj.get("nombreServicio").getAsString());
                            }
                        }
                        binding.spinnerServicio.setAdapter(new ArrayAdapter<>(
                                NuevaReservaActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                nombresServicios));
                    }
                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(NuevaReservaActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configurarSpinnerServicio() {
        binding.spinnerServicio.setOnItemClickListener((parent, view, position, id) -> {
            idServicioSeleccionado = idsServicios.get(position);
            abrirDatePicker();
            binding.btnReservar.setVisibility(View.GONE);
            horaSeleccionada = "";
        });
    }

    // PASO 3: FECHA

    private void abrirDatePicker() {
        Calendar hoy = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, day);
            cargarHorasDisponibles(idServicioSeleccionado, fechaSeleccionada);
            mostrarCard(binding.cardHorarios);
            binding.btnReservar.setVisibility(View.GONE);
            horaSeleccionada = "";
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH))
        {{ getDatePicker().setMinDate(hoy.getTimeInMillis()); }}
                .show();
    }

    // PASO 4: HORARIOS DISPONIBLES

    private void cargarHorasDisponibles(int idServicio, String fecha) {
        binding.recyclerHorarios.setVisibility(View.GONE);
        binding.tvSinHorarios.setVisibility(View.GONE);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.horasOcupadas(new PeticionCita(idServicio, fecha))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                        List<String> ocupadas = new ArrayList<>();
                        if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                            JsonElement datos = response.body().getDatos();
                            if (datos != null && datos.isJsonArray()) {
                                for (JsonElement e : datos.getAsJsonArray()) {
                                    ocupadas.add(e.getAsString());
                                }
                            }
                        }
                        List<String> disponibles = new ArrayList<>();
                        for (String hueco : HUECOS_DIA) {
                            if (!ocupadas.contains(hueco)) {
                                disponibles.add(hueco);
                            }
                        }
                        mostrarHoras(disponibles);
                    }
                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(NuevaReservaActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarHoras(List<String> disponibles) {
        if (disponibles.isEmpty()) {
            binding.tvSinHorarios.setVisibility(View.VISIBLE);
            binding.recyclerHorarios.setVisibility(View.GONE);
            return;
        }
        binding.tvSinHorarios.setVisibility(View.GONE);
        binding.recyclerHorarios.setVisibility(View.VISIBLE);
        binding.recyclerHorarios.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerHorarios.setAdapter(new HoraAdapter(disponibles, hora -> {
            horaSeleccionada = hora;
            mostrarCard(binding.btnReservar);
        }));
    }

    // CONFIRMAR RESERVA

    private void configurarBtnReservar() {
        binding.btnReservar.setOnClickListener(v -> {
            if (idServicioSeleccionado == -1 || fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Completa todos los pasos", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.btnReservar.setEnabled(false);
            ApiService api = ApiClient.getClient().create(ApiService.class);
            api.crearCita(new PeticionCita(emailUsuario, idServicioSeleccionado, fechaSeleccionada, horaSeleccionada))
                    .enqueue(new Callback<RespuestaGeneral>() {
                        @Override
                        public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                            binding.btnReservar.setEnabled(true);
                            if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                                Toast.makeText(NuevaReservaActivity.this, "¡Reserva confirmada!", Toast.LENGTH_LONG).show();
                                resetearPantalla();
                            } else {
                                String msg = "Error al reservar";
                                if (response.body() != null) {
                                    msg = response.body().getMensaje();
                                }
                                Toast.makeText(NuevaReservaActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                            binding.btnReservar.setEnabled(true);
                            Toast.makeText(NuevaReservaActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void resetearPantalla() {
        binding.spinnerNegocio.setText("", false);
        binding.spinnerServicio.setText("", false);
        binding.cardInfoNegocio.setVisibility(View.GONE);
        binding.cardServicio.setVisibility(View.GONE);
        binding.cardHorarios.setVisibility(View.GONE);
        binding.btnReservar.setVisibility(View.GONE);
        idNegocioSeleccionado  = -1;
        idServicioSeleccionado = -1;
        fechaSeleccionada = "";
        horaSeleccionada  = "";
    }

    interface OnHoraClickListener {
        void onClick(String hora);
    }

    static class HoraAdapter extends RecyclerView.Adapter<HoraAdapter.VH> {
        private final List<String> horas;
        private final OnHoraClickListener listener;
        private int seleccionado = -1;

        HoraAdapter(List<String> horas, OnHoraClickListener listener) {
            this.horas = horas;
            this.listener = listener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Button btn = new Button(parent.getContext());
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);
            return new VH(btn);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            String hora = horas.get(position);
            if (hora.length() >= 5) {
                holder.btn.setText(hora.substring(0, 5));
            } else {
                holder.btn.setText(hora);
            }
            if (seleccionado == position) {
                holder.btn.setBackgroundColor(0xFF1565C0);
                holder.btn.setTextColor(0xFFFFFFFF);
            } else {
                holder.btn.setBackgroundColor(0xFFEEEEEE);
                holder.btn.setTextColor(0xFF212121);
            }
            holder.btn.setOnClickListener(v -> {
                seleccionado = holder.getAdapterPosition();
                notifyDataSetChanged();
                listener.onClick(hora);
            });
        }

        @Override
        public int getItemCount() {
            return horas.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            Button btn;
            VH(Button b) {
                super(b);
                btn = b;
            }
        }
    }

    private void mostrarCard(View card) {
        card.setVisibility(View.VISIBLE);
        card.setAlpha(0f);
        card.animate().alpha(1f).setDuration(300).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}