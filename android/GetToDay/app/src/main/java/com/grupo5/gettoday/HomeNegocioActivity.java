package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.grupo5.gettoday.databinding.ActivityHomeNegocioBinding;

public class HomeNegocioActivity extends BaseActivity {

    private ActivityHomeNegocioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeNegocioBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuNegocio(R.id.navHome);

        configurarRecyclers();
        configurarBotones();
    }

    // onResume se ejecuta cada vez que esta pantalla vuelve a ser visible,
    // incluyendo cuando el usuario regresa desde PerfilNegocioActivity.
    // Así los datos siempre están actualizados.
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosNegocio();
    }

    private void cargarDatosNegocio() {
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        String nombre      = prefs.getString("negocio_nombre",       "");
        String direccion   = prefs.getString("negocio_direccion",    "");
        String descripcion = prefs.getString("negocio_descripcion",  "");

        binding.tvNombreNegocio.setText(nombre);
        binding.tvDireccionNegocio.setText(direccion);
        binding.tvDescripcionNegocio.setText(descripcion);
    }

    private void configurarRecyclers() {
        binding.recyclerReservasHoy.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.tvSinReservasHoy.setVisibility(View.VISIBLE);

        binding.recyclerReservasManana.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.tvSinReservasManana.setVisibility(View.VISIBLE);
    }

    private void configurarBotones() {
        binding.btnEditarNegocio.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilNegocioActivity.class))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}