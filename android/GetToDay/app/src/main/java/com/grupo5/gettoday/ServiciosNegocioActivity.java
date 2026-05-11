package com.grupo5.gettoday;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.grupo5.gettoday.databinding.ActivityServiciosNegocioBinding;

public class ServiciosNegocioActivity extends BaseActivity {

    /*
        ServiciosNegocioActivity extiende BaseActivity para
        obtener el menú inferior.

        RESPONSABILIDAD:
          · Mostrar lista de servicios del negocio
          · Añadir nuevo servicio
          · Editar servicio existente
          · Eliminar servicio

        CAMBIOS:
          · Sin campo de duración editable
          · Duración fija de 1 hora para todos los servicios
          · Se muestra la duración como texto informativo

        TABLA QUE USA:
          SERVICIOS (id_servicio, id_negocio, nombre_servicio,
                     precio, duracion_minutos=60)
    */

    private ActivityServiciosNegocioBinding binding;

    // Controla si el formulario está en modo edición o creación
    private boolean modoEdicion = false;

    // ID del servicio que se está editando
    private int idServicioEditando = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiciosNegocioBinding.inflate(
                getLayoutInflater()
        );
        getContenedor().addView(binding.getRoot());

        // Mostrar menú del negocio con "Servicios" seleccionado
        mostrarMenuNegocio(R.id.navServicios);

        configurarRecycler();
        configurarBotones();
    }

    /**
     * Configura el RecyclerView de servicios.
     * TODO: GET /api/servicios/negocio/{id_negocio}
     */
    private void configurarRecycler() {
        binding.recyclerServicios.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // TODO: ServicioAdapter con la lista real
        // El adapter tiene dos listeners:
        //
        // onEditar(servicio):
        //   · Rellenar formulario con datos del servicio
        //   · modoEdicion = true
        //   · idServicioEditando = servicio.getId()
        //   · Mostrar formulario
        //
        // onEliminar(idServicio):
        //   · DELETE /api/servicios/{id_servicio}
        //   · Eliminar item de la lista

        // Por ahora mostrar estado vacío
        binding.tvSinServicios.setVisibility(View.VISIBLE);
    }

    private void configurarBotones() {

        // ── BOTÓN AÑADIR SERVICIO ─────────────────────────────────
        binding.btnAnadirServicio.setOnClickListener(v -> {
            modoEdicion = false;
            idServicioEditando = -1;
            limpiarFormulario();
            binding.tvTituloFormServicio.setText("Nuevo servicio");
            mostrarFormulario();
        });

        // ── BOTÓN CANCELAR ────────────────────────────────────────
        binding.btnCancelarServicio.setOnClickListener(v -> {
            binding.cardFormServicio.setVisibility(View.GONE);
            limpiarFormulario();
        });

        // ── BOTÓN GUARDAR ─────────────────────────────────────────
        binding.btnGuardarServicio.setOnClickListener(v -> {
            if (validarFormulario()) {

                String nombre      = binding.etNombreServicio
                        .getText().toString().trim();
                String descripcion = binding.etDescripcionServicio
                        .getText().toString().trim();
                String precio      = binding.etPrecioServicio
                        .getText().toString().trim();

                // La duración siempre es 60 minutos (1 hora)
                // No se envía como campo editable
                int duracionFija = 60;

                if (modoEdicion) {
                    // TODO: PUT /api/servicios/{idServicioEditando}
                    // Enviar: nombre, descripcion, precio
                    // duracion_minutos = 60 (siempre)
                    Toast.makeText(this,
                            "Servicio actualizado correctamente",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: POST /api/servicios
                    // Enviar: nombre, descripcion, precio,
                    //         duracion_minutos=60, id_negocio
                    Toast.makeText(this,
                            "Servicio añadido correctamente",
                            Toast.LENGTH_SHORT).show();
                }

                binding.cardFormServicio.setVisibility(View.GONE);
                limpiarFormulario();

                // TODO: recargar lista de servicios
            }
        });
    }

    /**
     * Valida los campos obligatorios del formulario.
     * La duración no se valida porque es fija (1 hora).
     */
    private boolean validarFormulario() {
        boolean valido = true;

        if (binding.etNombreServicio
                .getText().toString().trim().isEmpty()) {
            binding.layoutNombreServicio.setError(
                    "Introduce el nombre del servicio"
            );
            valido = false;
        } else {
            binding.layoutNombreServicio.setError(null);
        }

        if (binding.etPrecioServicio
                .getText().toString().trim().isEmpty()) {
            binding.layoutPrecioServicio.setError(
                    "Introduce el precio"
            );
            valido = false;
        } else {
            binding.layoutPrecioServicio.setError(null);
        }

        return valido;
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        binding.etNombreServicio.setText("");
        binding.etDescripcionServicio.setText("");
        binding.etPrecioServicio.setText("");
        binding.layoutNombreServicio.setError(null);
        binding.layoutPrecioServicio.setError(null);
    }

    /**
     * Muestra el formulario con animación suave.
     */
    private void mostrarFormulario() {
        binding.cardFormServicio.setVisibility(View.VISIBLE);
        binding.cardFormServicio.setAlpha(0f);
        binding.cardFormServicio.animate()
                .alpha(1f).setDuration(300).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}