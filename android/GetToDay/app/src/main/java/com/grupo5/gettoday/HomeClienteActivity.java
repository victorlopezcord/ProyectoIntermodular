package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.grupo5.gettoday.databinding.ActivityHomeClienteBinding;

public class HomeClienteActivity extends BaseActivity {

    /*
        HomeClienteActivity extiende BaseActivity para
        obtener automáticamente el toolbar y el menú inferior.

        RESPONSABILIDAD:
          · Mostrar los datos del cliente logueado
          · Mostrar la lista de reservas aceptadas
          · Botón editar → navega a PerfilClienteActivity
    */

    private ActivityHomeClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el layout de esta pantalla dentro del
        // contenedor del layout base (activity_base.xml)
        binding = ActivityHomeClienteBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        // Mostrar el menú del cliente con "Inicio" seleccionado
        mostrarMenuCliente(R.id.navHome);


        configurarDatosCliente();
        configurarRecycler();
        configurarBotones();
    }

    /**
     * Rellena los datos del cliente en pantalla.
     * TODO: sustituir los datos de prueba por la respuesta
     * de GET /api/clientes/{id}
     */
    private void configurarDatosCliente() {
        // TODO: obtener datos reales del cliente desde la API
        binding.tvNombreCliente.setText("Nombre del cliente");
        binding.tvEmailCliente.setText("cliente@email.com");
        binding.tvTelefonoCliente.setText("600 000 000");
    }

    /**
     * Configura el RecyclerView de reservas aceptadas.
     * TODO: conectar con GET /api/reservas/cliente/{id}
     * y crear el adaptador con la lista de reservas.
     */
    private void configurarRecycler() {
        binding.recyclerReservas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // TODO: cuando tengas el adaptador, conectarlo así:
        // ReservaClienteAdapter adapter = new ReservaClienteAdapter(listaReservas);
        // binding.recyclerReservas.setAdapter(adapter);

        // Por ahora mostrar el estado vacío
        binding.tvSinReservas.setVisibility(View.VISIBLE);
    }

    private void configurarBotones() {

        // ── BOTÓN EDITAR PERFIL ───────────────────────────────────
        binding.btnEditarPerfil.setOnClickListener(v -> {

            Intent intent = new Intent(this, PerfilClienteActivity.class);
            startActivity(intent);

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}