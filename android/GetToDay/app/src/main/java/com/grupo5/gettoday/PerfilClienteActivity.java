package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.grupo5.gettoday.databinding.ActivityPerfilClienteBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilClienteActivity extends BaseActivity {

    private ActivityPerfilClienteBinding binding;
    private String emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfilClienteBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuCliente(R.id.navPerfil);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailUsuario = prefs.getString("usuario_email", "");

        cargarDatosDesdePrefs(prefs);
        configurarBotones();
    }

    private void cargarDatosDesdePrefs(SharedPreferences prefs) {
        String nombre   = prefs.getString("usuario_nombre_"   + emailUsuario, "");
        String email    = prefs.getString("usuario_email",    "");
        String telefono = prefs.getString("usuario_telefono_" + emailUsuario, "");

        binding.tvNombrePerfil.setText(nombre);
        binding.tvEmailPerfil.setText(email);
        binding.tvTelefonoPerfil.setText(telefono);
    }

    private void configurarBotones() {

        binding.btnEditar.setOnClickListener(v -> {
            binding.etNombreEditar.setText(binding.tvNombrePerfil.getText().toString());
            binding.etTelefonoEditar.setText(binding.tvTelefonoPerfil.getText().toString());

            binding.cardVista.setVisibility(View.GONE);
            binding.cardEdicion.setVisibility(View.VISIBLE);
            binding.cardEdicion.setAlpha(0f);
            binding.cardEdicion.animate().alpha(1f).setDuration(300).start();
        });

        binding.btnCancelar.setOnClickListener(v -> {
            binding.cardEdicion.setVisibility(View.GONE);
            binding.cardVista.setVisibility(View.VISIBLE);
            binding.layoutNombreEditar.setError(null);
            binding.layoutTelefonoEditar.setError(null);
        });

        binding.btnGuardar.setOnClickListener(v -> {
            String nuevoNombre   = binding.etNombreEditar.getText().toString().trim();
            String nuevoTelefono = binding.etTelefonoEditar.getText().toString().trim();

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

            binding.btnGuardar.setEnabled(false);

            PeticionModificarUsuario peticion = new PeticionModificarUsuario(
                    nuevoNombre, emailUsuario, nuevoTelefono,
                    "",   // sin cambio de contraseña
                    0,    // rol CLIENTE
                    null, null, null
            );

            ApiService api = ApiClient.getClient().create(ApiService.class);
            api.modificarUsuario(peticion).enqueue(new Callback<RespuestaGeneral>() {

                @Override
                public void onResponse(Call<RespuestaGeneral> call,
                                       Response<RespuestaGeneral> response) {
                    binding.btnGuardar.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null
                            && response.body().isExito()) {

                        binding.tvNombrePerfil.setText(nuevoNombre);
                        binding.tvTelefonoPerfil.setText(nuevoTelefono);

                        getSharedPreferences("MiAppPrefs", MODE_PRIVATE).edit()
                                .putString("usuario_nombre_"   + emailUsuario, nuevoNombre)
                                .putString("usuario_telefono_" + emailUsuario, nuevoTelefono)
                                .apply();

                        binding.cardEdicion.setVisibility(View.GONE);
                        binding.cardVista.setVisibility(View.VISIBLE);

                        Toast.makeText(PerfilClienteActivity.this,
                                "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = response.body() != null
                                ? response.body().getMensaje() : "Error al guardar";
                        Toast.makeText(PerfilClienteActivity.this,
                                msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                    binding.btnGuardar.setEnabled(true);
                    Toast.makeText(PerfilClienteActivity.this,
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}