package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.grupo5.gettoday.databinding.ActivityPerfilNegocioBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilNegocioActivity extends BaseActivity {

    private ActivityPerfilNegocioBinding binding;
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfilNegocioBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuNegocio(R.id.navPerfil);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailUsuario = prefs.getString("usuario_email", "");

        cargarDatosDesdePrefs(prefs);
        configurarBotones();
    }

    private void cargarDatosDesdePrefs(SharedPreferences prefs) {
        binding.tvNombreNegocioPerfil.setText(prefs.getString("negocio_nombre_"      + emailUsuario, ""));
        binding.tvDireccionNegocioPerfil.setText(prefs.getString("negocio_direccion_"   + emailUsuario, ""));
        binding.tvDescripcionNegocioPerfil.setText(prefs.getString("negocio_descripcion_" + emailUsuario, ""));

        binding.tvNombreUsuario.setText(prefs.getString("usuario_nombre_"   + emailUsuario, ""));
        binding.tvEmailUsuario.setText(prefs.getString("usuario_email",     ""));
        binding.tvTelefonoUsuario.setText(prefs.getString("usuario_telefono_" + emailUsuario, ""));
    }

    private void configurarBotones() {

        binding.btnEditar.setOnClickListener(v -> {
            binding.etNombreNegocioEditar.setText(binding.tvNombreNegocioPerfil.getText().toString());
            binding.etDireccionEditar.setText(binding.tvDireccionNegocioPerfil.getText().toString());
            binding.etDescripcionEditar.setText(binding.tvDescripcionNegocioPerfil.getText().toString());
            binding.etNombreUsuarioEditar.setText(binding.tvNombreUsuario.getText().toString());
            binding.etTelefonoEditar.setText(binding.tvTelefonoUsuario.getText().toString());

            binding.cardVista.setVisibility(View.GONE);
            binding.cardEdicion.setVisibility(View.VISIBLE);
            binding.cardEdicion.setAlpha(0f);
            binding.cardEdicion.animate().alpha(1f).setDuration(300).start();
        });

        binding.btnCancelar.setOnClickListener(v -> {
            binding.cardEdicion.setVisibility(View.GONE);
            binding.cardVista.setVisibility(View.VISIBLE);
            binding.layoutNombreNegocioEditar.setError(null);
            binding.layoutDireccionEditar.setError(null);
            binding.layoutNombreUsuarioEditar.setError(null);
            binding.layoutTelefonoEditar.setError(null);
        });

        binding.btnGuardar.setOnClickListener(v -> {
            if (!validarFormulario()) return;

            String nuevoNombreNegocio = binding.etNombreNegocioEditar.getText().toString().trim();
            String nuevaDireccion     = binding.etDireccionEditar.getText().toString().trim();
            String nuevaDescripcion   = binding.etDescripcionEditar.getText().toString().trim();
            String nuevoNombreUsuario = binding.etNombreUsuarioEditar.getText().toString().trim();
            String nuevoTelefono      = binding.etTelefonoEditar.getText().toString().trim();

            binding.btnGuardar.setEnabled(false);

            PeticionModificarUsuario peticion = new PeticionModificarUsuario(
                    nuevoNombreUsuario, emailUsuario, nuevoTelefono,
                    "",  // sin cambio de contraseña
                    1,   // rol NEGOCIO
                    nuevoNombreNegocio, nuevaDireccion, nuevaDescripcion
            );

            ApiService api = ApiClient.getClient().create(ApiService.class);
            api.modificarUsuario(peticion).enqueue(new Callback<RespuestaGeneral>() {

                @Override
                public void onResponse(Call<RespuestaGeneral> call,
                                       Response<RespuestaGeneral> response) {
                    binding.btnGuardar.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null
                            && response.body().isExito()) {

                        binding.tvNombreNegocioPerfil.setText(nuevoNombreNegocio);
                        binding.tvDireccionNegocioPerfil.setText(nuevaDireccion);
                        binding.tvDescripcionNegocioPerfil.setText(nuevaDescripcion);
                        binding.tvNombreUsuario.setText(nuevoNombreUsuario);
                        binding.tvTelefonoUsuario.setText(nuevoTelefono);

                        getSharedPreferences("MiAppPrefs", MODE_PRIVATE).edit()
                                .putString("negocio_nombre_"      + emailUsuario, nuevoNombreNegocio)
                                .putString("negocio_direccion_"   + emailUsuario, nuevaDireccion)
                                .putString("negocio_descripcion_" + emailUsuario, nuevaDescripcion)
                                .putString("usuario_nombre_"      + emailUsuario, nuevoNombreUsuario)
                                .putString("usuario_telefono_"    + emailUsuario, nuevoTelefono)
                                .apply();

                        binding.cardEdicion.setVisibility(View.GONE);
                        binding.cardVista.setVisibility(View.VISIBLE);

                        Toast.makeText(PerfilNegocioActivity.this,
                                "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = response.body() != null
                                ? response.body().getMensaje() : "Error al guardar";
                        Toast.makeText(PerfilNegocioActivity.this,
                                msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                    binding.btnGuardar.setEnabled(true);
                    Toast.makeText(PerfilNegocioActivity.this,
                            "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // ── CERRAR SESIÓN: solo borra email y rol, los datos quedan ligados al correo ──
        binding.btnCerrarSesion.setOnClickListener(v -> {
            getSharedPreferences("MiAppPrefs", MODE_PRIVATE).edit()
                    .remove("usuario_email")
                    .remove("usuario_rol")
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

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