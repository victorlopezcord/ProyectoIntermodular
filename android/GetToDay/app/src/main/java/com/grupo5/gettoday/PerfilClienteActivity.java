package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.grupo5.gettoday.databinding.ActivityPerfilClienteBinding;

public class PerfilClienteActivity extends BaseActivity {

    /*
        PerfilClienteActivity extiende BaseActivity para
        obtener el toolbar y el menú inferior.

        RESPONSABILIDAD:
          · Mostrar datos del cliente en modo vista
          · Permitir editar nombre y teléfono
          · Cerrar sesión y volver al Login
    */

    private ActivityPerfilClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfilClienteBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        // Mostrar menú del cliente con "Perfil" seleccionado
        mostrarMenuCliente(R.id.navPerfil);


        cargarDatosCliente();
        configurarBotones();
    }

    /**
     * Carga los datos del cliente en modo vista.
     * TODO: sustituir por GET /api/clientes/{id}
     */
    private void cargarDatosCliente() {
        // TODO: obtener datos reales del cliente desde la API
        binding.tvNombrePerfil.setText("Nombre del cliente");
        binding.tvEmailPerfil.setText("cliente@email.com");
        binding.tvTelefonoPerfil.setText("600 000 000");
    }

    private void configurarBotones() {

        // ── BOTÓN EDITAR ──────────────────────────────────────────
        binding.btnEditar.setOnClickListener(v -> {

            // Rellenar los campos editables con los datos actuales
            binding.etNombreEditar.setText(
                    binding.tvNombrePerfil.getText().toString()
            );
            binding.etTelefonoEditar.setText(
                    binding.tvTelefonoPerfil.getText().toString()
            );

            // Ocultar card de vista y mostrar card de edición
            binding.cardVista.setVisibility(View.GONE);
            binding.cardEdicion.setVisibility(View.VISIBLE);
            binding.cardEdicion.setAlpha(0f);
            binding.cardEdicion.animate().alpha(1f).setDuration(300).start();
        });

        // ── BOTÓN CANCELAR ────────────────────────────────────────
        binding.btnCancelar.setOnClickListener(v -> {

            // Volver al modo vista sin guardar cambios
            binding.cardEdicion.setVisibility(View.GONE);
            binding.cardVista.setVisibility(View.VISIBLE);

            // Limpiar errores si los había
            binding.layoutNombreEditar.setError(null);
            binding.layoutTelefonoEditar.setError(null);
        });

        // ── BOTÓN GUARDAR ─────────────────────────────────────────
        binding.btnGuardar.setOnClickListener(v -> {

            String nuevoNombre = binding.etNombreEditar
                    .getText().toString().trim();
            String nuevoTelefono = binding.etTelefonoEditar
                    .getText().toString().trim();

            // Validar campos
            if (nuevoNombre.isEmpty()) {
                binding.layoutNombreEditar.setError("Introduce tu nombre");
                return;
            }
            if (nuevoTelefono.isEmpty()) {
                binding.layoutTelefonoEditar.setError("Introduce tu teléfono");
                return;
            }

            binding.layoutNombreEditar.setError(null);
            binding.layoutTelefonoEditar.setError(null);

            // TODO: llamar a PUT /api/clientes/{id} con:
            //   · nombre = nuevoNombre
            //   · telefono = nuevoTelefono

            // Actualizar los datos en modo vista
            binding.tvNombrePerfil.setText(nuevoNombre);
            binding.tvTelefonoPerfil.setText(nuevoTelefono);

            // Volver al modo vista
            binding.cardEdicion.setVisibility(View.GONE);
            binding.cardVista.setVisibility(View.VISIBLE);

            Toast.makeText(this,
                    "Datos actualizados correctamente",
                    Toast.LENGTH_SHORT).show();
        });

        // ── BOTÓN CERRAR SESIÓN ───────────────────────────────────
        binding.btnCerrarSesion.setOnClickListener(v -> {

            // TODO: borrar token de sesión de SharedPreferences

            // Navegar al Login limpiando toda la pila de Activities
            // FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK:
            // el usuario no puede volver atrás pulsando Back
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}