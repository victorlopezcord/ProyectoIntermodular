package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.grupo5.gettoday.databinding.ActivityPerfilNegocioBinding;

public class PerfilNegocioActivity extends BaseActivity {

    /*
        PerfilNegocioActivity extiende BaseActivity para
        obtener el toolbar y el menú inferior.

        RESPONSABILIDAD:
          · Mostrar datos del negocio y propietario en modo vista
          · Permitir editar nombre, dirección, descripción del negocio
          · Permitir editar nombre y teléfono del propietario
          · Cerrar sesión y volver al Login
    */

    private ActivityPerfilNegocioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfilNegocioBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        // Mostrar menú del negocio con "Perfil" seleccionado
        mostrarMenuNegocio(R.id.navPerfil);


        cargarDatos();
        configurarBotones();
    }

    /**
     * Carga los datos del negocio y propietario en modo vista.
     * TODO: sustituir por GET /api/negocios/{id}
     */
    private void cargarDatos() {
        // TODO: obtener datos reales desde la API
        binding.tvNombreNegocioPerfil.setText("Nombre del negocio");
        binding.tvDireccionNegocioPerfil.setText("Calle Ejemplo, 10");
        binding.tvDescripcionNegocioPerfil.setText("Descripción del negocio");
        binding.tvNombreUsuario.setText("Nombre del propietario");
        binding.tvEmailUsuario.setText("negocio@email.com");
        binding.tvTelefonoUsuario.setText("600 000 000");
    }

    private void configurarBotones() {

        // ── BOTÓN EDITAR ──────────────────────────────────────────
        binding.btnEditar.setOnClickListener(v -> {

            // Rellenar campos editables con datos actuales
            binding.etNombreNegocioEditar.setText(
                    binding.tvNombreNegocioPerfil.getText().toString()
            );
            binding.etDireccionEditar.setText(
                    binding.tvDireccionNegocioPerfil.getText().toString()
            );
            binding.etDescripcionEditar.setText(
                    binding.tvDescripcionNegocioPerfil.getText().toString()
            );
            binding.etNombreUsuarioEditar.setText(
                    binding.tvNombreUsuario.getText().toString()
            );
            binding.etTelefonoEditar.setText(
                    binding.tvTelefonoUsuario.getText().toString()
            );

            // Ocultar vista y mostrar edición con animación
            binding.cardVista.setVisibility(View.GONE);
            binding.cardEdicion.setVisibility(View.VISIBLE);
            binding.cardEdicion.setAlpha(0f);
            binding.cardEdicion.animate().alpha(1f).setDuration(300).start();
        });

        // ── BOTÓN CANCELAR ────────────────────────────────────────
        binding.btnCancelar.setOnClickListener(v -> {

            // Volver al modo vista sin guardar
            binding.cardEdicion.setVisibility(View.GONE);
            binding.cardVista.setVisibility(View.VISIBLE);

            // Limpiar errores
            binding.layoutNombreNegocioEditar.setError(null);
            binding.layoutDireccionEditar.setError(null);
            binding.layoutNombreUsuarioEditar.setError(null);
            binding.layoutTelefonoEditar.setError(null);
        });

        // ── BOTÓN GUARDAR ─────────────────────────────────────────
        binding.btnGuardar.setOnClickListener(v -> {

            if (validarFormulario()) {

                String nuevoNombreNegocio = binding.etNombreNegocioEditar
                        .getText().toString().trim();
                String nuevaDireccion = binding.etDireccionEditar
                        .getText().toString().trim();
                String nuevaDescripcion = binding.etDescripcionEditar
                        .getText().toString().trim();
                String nuevoNombreUsuario = binding.etNombreUsuarioEditar
                        .getText().toString().trim();
                String nuevoTelefono = binding.etTelefonoEditar
                        .getText().toString().trim();

                // TODO: llamar a PUT /api/negocios/{id} con:
                //   · nombreLocal = nuevoNombreNegocio
                //   · direccion   = nuevaDireccion
                //   · descripcion = nuevaDescripcion

                // TODO: llamar a PUT /api/usuarios/{id} con:
                //   · nombre   = nuevoNombreUsuario
                //   · telefono = nuevoTelefono

                // Actualizar datos en modo vista
                binding.tvNombreNegocioPerfil.setText(nuevoNombreNegocio);
                binding.tvDireccionNegocioPerfil.setText(nuevaDireccion);
                binding.tvDescripcionNegocioPerfil.setText(nuevaDescripcion);
                binding.tvNombreUsuario.setText(nuevoNombreUsuario);
                binding.tvTelefonoUsuario.setText(nuevoTelefono);

                // Volver al modo vista
                binding.cardEdicion.setVisibility(View.GONE);
                binding.cardVista.setVisibility(View.VISIBLE);

                Toast.makeText(this,
                        "Datos actualizados correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ── BOTÓN CERRAR SESIÓN ───────────────────────────────────
        binding.btnCerrarSesion.setOnClickListener(v -> {

            // TODO: borrar token de sesión de SharedPreferences

            // Navegar al Login limpiando toda la pila de Activities
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        });
    }

    /**
     * Valida que los campos obligatorios estén rellenos.
     */
    private boolean validarFormulario() {
        boolean valido = true;

        if (binding.etNombreNegocioEditar.getText().toString().trim().isEmpty()) {
            binding.layoutNombreNegocioEditar.setError("Introduce el nombre del negocio");
            valido = false;
        } else {
            binding.layoutNombreNegocioEditar.setError(null);
        }

        if (binding.etDireccionEditar.getText().toString().trim().isEmpty()) {
            binding.layoutDireccionEditar.setError("Introduce la dirección");
            valido = false;
        } else {
            binding.layoutDireccionEditar.setError(null);
        }

        if (binding.etNombreUsuarioEditar.getText().toString().trim().isEmpty()) {
            binding.layoutNombreUsuarioEditar.setError("Introduce tu nombre");
            valido = false;
        } else {
            binding.layoutNombreUsuarioEditar.setError(null);
        }

        if (binding.etTelefonoEditar.getText().toString().trim().isEmpty()) {
            binding.layoutTelefonoEditar.setError("Introduce tu teléfono");
            valido = false;
        } else {
            binding.layoutTelefonoEditar.setError(null);
        }

        return valido;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}