package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.grupo5.gettoday.databinding.ActivityHomeNegocioBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeNegocioActivity extends BaseActivity {

    private ActivityHomeNegocioBinding binding;
    private String emailNegocio;

    private RecyclerView recyclerPendientes;
    private TextView tvSinPendientes;
    private boolean seccionCreada = false;

    private final List<JsonObject> citasPendientes = new ArrayList<>();
    private final List<JsonObject> citasHoy        = new ArrayList<>();
    private final List<JsonObject> citasManana     = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeNegocioBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuNegocio(R.id.navHome);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailNegocio = prefs.getString("usuario_email", "");

        agregarSeccionPendientes();
        configurarRecyclers();
        configurarBotones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosNegocio();
        cargarCitas();
    }

    private void cargarDatosNegocio() {
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        binding.tvNombreNegocio.setText(prefs.getString("negocio_nombre_" + emailNegocio, ""));
        binding.tvDireccionNegocio.setText(prefs.getString("negocio_direccion_" + emailNegocio, ""));
        binding.tvDescripcionNegocio.setText(prefs.getString("negocio_descripcion_" + emailNegocio, ""));
    }

    private void agregarSeccionPendientes() {
        if (seccionCreada) return;
        seccionCreada = true;

        LinearLayout container = (LinearLayout) ((ScrollView) binding.getRoot()).getChildAt(0);
        int dp = (int) getResources().getDisplayMetrics().density;

        TextView tvTitulo = new TextView(this);
        tvTitulo.setText("Solicitudes pendientes");
        tvTitulo.setTextSize(18);
        tvTitulo.setTextColor(getColor(R.color.azul_oscuro));
        tvTitulo.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams pTitulo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pTitulo.setMargins(0, 0, 0, 12 * dp);
        tvTitulo.setLayoutParams(pTitulo);

        tvSinPendientes = new TextView(this);
        tvSinPendientes.setText("No hay solicitudes pendientes");
        tvSinPendientes.setTextSize(14);
        tvSinPendientes.setTextColor(getColor(R.color.texto_secundario));
        tvSinPendientes.setGravity(Gravity.CENTER);
        tvSinPendientes.setPadding(0, 16 * dp, 0, 16 * dp);
        LinearLayout.LayoutParams pSin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pSin.setMargins(0, 0, 0, 24 * dp);
        tvSinPendientes.setLayoutParams(pSin);
        tvSinPendientes.setVisibility(View.GONE);

        recyclerPendientes = new RecyclerView(this);
        LinearLayout.LayoutParams pRecycler = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pRecycler.setMargins(0, 0, 0, 24 * dp);
        recyclerPendientes.setLayoutParams(pRecycler);
        recyclerPendientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerPendientes.setNestedScrollingEnabled(false);

        container.addView(tvTitulo, 1);
        container.addView(tvSinPendientes, 2);
        container.addView(recyclerPendientes, 3);
    }

    private void configurarRecyclers() {
        binding.recyclerReservasHoy.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerReservasManana.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarCitas() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.citasNegocio(PeticionCita.paraNegocio(emailNegocio))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call,
                                           Response<RespuestaGeneral> response) {
                        citasPendientes.clear();
                        citasHoy.clear();
                        citasManana.clear();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Calendar cal = Calendar.getInstance();
                            String hoy    = sdf.format(cal.getTime());
                            cal.add(Calendar.DAY_OF_YEAR, 1);
                            String manana = sdf.format(cal.getTime());

                            JsonElement datos = response.body().getDatos();
                            if (datos != null && datos.isJsonArray()) {
                                for (JsonElement elem : datos.getAsJsonArray()) {
                                    JsonObject cita   = elem.getAsJsonObject();
                                    String estado = str(cita, "estado", "");
                                    String fechaCompleta = str(cita, "fecha", "");
                                    String fecha = "";
                                    if (fechaCompleta.length() >= 10) {
                                        fecha = fechaCompleta.substring(0, 10);
                                    }

                                    if (estado.equals("pendiente")) {
                                        citasPendientes.add(cita);
                                    } else if (estado.equals("confirmada")) {
                                        if (hoy.equals(fecha)) {
                                            citasHoy.add(cita);
                                        } else if (manana.equals(fecha)) {
                                            citasManana.add(cita);
                                        }
                                    }
                                }
                            }
                        }
                        mostrarCitas();
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(HomeNegocioActivity.this,
                                "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarCitas() {
        // Pendientes
        if (citasPendientes.isEmpty()) {
            tvSinPendientes.setVisibility(View.VISIBLE);
            recyclerPendientes.setVisibility(View.GONE);
        } else {
            tvSinPendientes.setVisibility(View.GONE);
            recyclerPendientes.setVisibility(View.VISIBLE);
            recyclerPendientes.setAdapter(
                    new CitaAdapterNegocio(citasPendientes, true, this::onAccionCita));
        }

        // Confirmadas HOY
        if (citasHoy.isEmpty()) {
            binding.tvSinReservasHoy.setVisibility(View.VISIBLE);
            binding.recyclerReservasHoy.setVisibility(View.GONE);
        } else {
            binding.tvSinReservasHoy.setVisibility(View.GONE);
            binding.recyclerReservasHoy.setVisibility(View.VISIBLE);
            binding.recyclerReservasHoy.setAdapter(
                    new CitaAdapterNegocio(citasHoy, false, null));
        }

        // Confirmadas MAÑANA
        if (citasManana.isEmpty()) {
            binding.tvSinReservasManana.setVisibility(View.VISIBLE);
            binding.recyclerReservasManana.setVisibility(View.GONE);
        } else {
            binding.tvSinReservasManana.setVisibility(View.GONE);
            binding.recyclerReservasManana.setVisibility(View.VISIBLE);
            binding.recyclerReservasManana.setAdapter(
                    new CitaAdapterNegocio(citasManana, false, null));
        }
    }

    private void onAccionCita(int idCita, String nuevoEstado, int position) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.modificarCita(PeticionCita.paraModificar(idCita, nuevoEstado))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call,
                                           Response<RespuestaGeneral> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isExito()) {

                            String msg;
                            if (nuevoEstado.equals("confirmada")) {
                                msg = "Cita aceptada";
                            } else {
                                msg = "Cita cancelada";
                            }
                            Toast.makeText(HomeNegocioActivity.this,
                                    msg, Toast.LENGTH_SHORT).show();
                            cargarCitas();
                        } else {
                            Toast.makeText(HomeNegocioActivity.this,
                                    "Error al actualizar la cita", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(HomeNegocioActivity.this,
                                "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String str(JsonObject obj, String key, String def) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return def;
    }

    private void configurarBotones() {
        binding.btnEditarNegocio.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilNegocioActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}