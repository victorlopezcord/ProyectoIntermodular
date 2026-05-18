package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.grupo5.gettoday.databinding.ActivityHomeClienteBinding;

public class HomeClienteActivity extends BaseActivity {

    private ActivityHomeClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeClienteBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuCliente(R.id.navHome);

        configurarRecycler();
        configurarBotones();
    }

    // onResume se ejecuta cada vez que esta pantalla vuelve a ser visible,
    // incluyendo cuando el usuario regresa desde PerfilClienteActivity.
    // Así los datos siempre están actualizados.
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosCliente();
    }

    private void cargarDatosCliente() {
        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        String nombre   = prefs.getString("usuario_nombre", "");
        String email    = prefs.getString("usuario_email",  "");
        String telefono = prefs.getString("usuario_telefono", "");

        binding.tvNombreCliente.setText(nombre);
        binding.tvEmailCliente.setText(email);
        binding.tvTelefonoCliente.setText(telefono);
    }

    private void configurarRecycler() {
        binding.recyclerReservas.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.tvSinReservas.setVisibility(View.VISIBLE);
    }

    private void configurarBotones() {
        binding.btnEditarPerfil.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilClienteActivity.class))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}