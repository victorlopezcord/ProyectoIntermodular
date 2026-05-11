package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.grupo5.gettoday.databinding.ActivityHomeNegocioBinding;

public class HomeNegocioActivity extends BaseActivity {

    /*
        HomeNegocioActivity extiende BaseActivity para
        obtener el menú inferior.

        RESPONSABILIDAD:
          · Mostrar datos del negocio
          · Mostrar citas confirmadas de HOY
          · Mostrar citas confirmadas de MAÑANA

        LLAMADAS AL BACKEND:
          · GET /api/negocios/{id_negocio}
          · GET /api/citas/negocio/{id_negocio}
              ?estado=confirmada&fecha=hoy
          · GET /api/citas/negocio/{id_negocio}
              ?estado=confirmada&fecha=mañana
    */

    private ActivityHomeNegocioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeNegocioBinding.inflate(
                getLayoutInflater()
        );
        getContenedor().addView(binding.getRoot());

        // Mostrar menú del negocio con "Inicio" seleccionado
        mostrarMenuNegocio(R.id.navHome);

        cargarDatosNegocio();
        configurarRecyclers();
        configurarBotones();
    }

    /**
     * Carga los datos del negocio.
     * TODO: GET /api/negocios/{id_negocio}
     */
    private void cargarDatosNegocio() {
        binding.tvNombreNegocio.setText("Nombre del negocio");
        binding.tvDireccionNegocio.setText("Calle Ejemplo, 10");
        binding.tvDescripcionNegocio.setText("Descripción del negocio");
    }

    /**
     * Configura los dos RecyclerViews:
     * uno para hoy y otro para mañana.
     *
     * TODO: conectar con la API:
     *   · GET /api/citas/negocio/{id}?estado=confirmada&fecha=hoy
     *   · GET /api/citas/negocio/{id}?estado=confirmada&fecha=mañana
     */
    private void configurarRecyclers() {

        // RecyclerView de hoy
        binding.recyclerReservasHoy.setLayoutManager(
                new LinearLayoutManager(this)
        );
        // TODO: ReservaNegocioAdapter con citas de hoy
        // Por ahora mostrar estado vacío
        binding.tvSinReservasHoy.setVisibility(View.VISIBLE);

        // RecyclerView de mañana
        binding.recyclerReservasManana.setLayoutManager(
                new LinearLayoutManager(this)
        );
        // TODO: ReservaNegocioAdapter con citas de mañana
        // Por ahora mostrar estado vacío
        binding.tvSinReservasManana.setVisibility(View.VISIBLE);
    }

    private void configurarBotones() {

        // ── BOTÓN EDITAR NEGOCIO ──────────────────────────────────
        binding.btnEditarNegocio.setOnClickListener(v -> {
            Intent intent = new Intent(this,
                    PerfilNegocioActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}