package com.grupo5.gettoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grupo5.gettoday.databinding.ActivityRegistroBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarToggleRol();
        configurarBotones();
    }

    private void configurarToggleRol() {
        binding.toggleRol.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnRolNegocio) {
                binding.cardDatosNegocio.setVisibility(View.VISIBLE);
                binding.cardDatosNegocio.setAlpha(0f);
                binding.cardDatosNegocio.animate().alpha(1f).setDuration(300).start();
            } else if (checkedId == R.id.btnRolCliente) {
                binding.cardDatosNegocio.setVisibility(View.GONE);
            }
        });
    }

    private void configurarBotones() {

        // ── BOTÓN VOLVER ──────────────────────────────────────────
        binding.btnVolver.setOnClickListener(v -> finish());

        // ── BOTÓN CREAR CUENTA ────────────────────────────────────
        binding.btnCrearCuenta.setOnClickListener(v -> {
            if (!validarFormulario()) return;

            // Recoger valores del formulario
            String nombre   = binding.etNombre.getText().toString().trim();
            String email    = binding.etEmail.getText().toString().trim();
            String telefono = binding.etTelefono.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            // Determinar rol: 1 = NEGOCIO, 0 = CLIENTE
            int rol = (binding.toggleRol.getCheckedButtonId() == R.id.btnRolNegocio) ? 1 : 0;

            // Datos de negocio solo si es negocio
            String nombreLocal = null;
            String direccion   = null;
            String descripcion = null;
            if (rol == 1) {
                nombreLocal = binding.etNombreLocal.getText().toString().trim();
                direccion   = binding.etDireccion.getText().toString().trim();
                descripcion = binding.etDescripcion.getText().toString().trim();
            }

            // Guardamos referencias finales para usarlas dentro del callback
            final String fNombre      = nombre;
            final String fEmail       = email;
            final String fTelefono    = telefono;
            final int    fRol         = rol;
            final String fNombreLocal = nombreLocal;
            final String fDireccion   = direccion;
            final String fDescripcion = descripcion;

            PeticionRegistro peticion = new PeticionRegistro(
                    nombre, email, telefono, password, rol,
                    nombreLocal, direccion, descripcion
            );

            binding.btnCrearCuenta.setEnabled(false);

            ApiService api = ApiClient.getClient().create(ApiService.class);
            api.register(peticion).enqueue(new Callback<RespuestaGeneral>() {

                @Override
                public void onResponse(Call<RespuestaGeneral> call,
                                       Response<RespuestaGeneral> response) {
                    binding.btnCrearCuenta.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        RespuestaGeneral res = response.body();
                        if (res.isExito()) {

                            // ── GUARDAR DATOS EN SHAREDPREFERENCES ──────────
                            // Así cuando el usuario haga login, los datos ya
                            // están disponibles sin necesitar otra llamada al servidor.
                            SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("usuario_email",       fEmail)
                                    .putInt(   "usuario_rol",         fRol)
                                    .putString("usuario_nombre",      fNombre)
                                    .putString("usuario_telefono",    fTelefono)
                                    .putString("negocio_nombre",      fNombreLocal != null ? fNombreLocal : "")
                                    .putString("negocio_direccion",   fDireccion   != null ? fDireccion   : "")
                                    .putString("negocio_descripcion", fDescripcion != null ? fDescripcion : "")
                                    .apply();
                            // ────────────────────────────────────────────────

                            Toast.makeText(RegistroActivity.this,
                                    "Cuenta creada correctamente. ¡Ya puedes iniciar sesión!",
                                    Toast.LENGTH_LONG).show();
                            finish(); // Volver a la pantalla de Login
                        } else {
                            Toast.makeText(RegistroActivity.this,
                                    res.getMensaje(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this,
                                "Error del servidor (" + response.code() + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                    binding.btnCrearCuenta.setEnabled(true);
                    Toast.makeText(RegistroActivity.this,
                            "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validarFormulario() {

        String nombre          = binding.etNombre.getText().toString().trim();
        String email           = binding.etEmail.getText().toString().trim();
        String telefono        = binding.etTelefono.getText().toString().trim();
        String password        = binding.etPassword.getText().toString();
        String passwordConfirm = binding.etPasswordConfirm.getText().toString();

        boolean valido = true;

        if (nombre.isEmpty()) {
            binding.layoutNombre.setError("Introduce tu nombre completo");
            valido = false;
        } else {
            binding.layoutNombre.setError(null);
        }

        if (email.isEmpty()) {
            binding.layoutEmail.setError("Introduce tu correo electrónico");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.setError("El formato del email no es válido");
            valido = false;
        } else {
            binding.layoutEmail.setError(null);
        }

        if (telefono.isEmpty()) {
            binding.layoutTelefono.setError("Introduce tu teléfono");
            valido = false;
        } else {
            binding.layoutTelefono.setError(null);
        }

        if (password.isEmpty()) {
            binding.layoutPassword.setError("Introduce una contraseña");
            valido = false;
        } else if (password.length() < 6) {
            binding.layoutPassword.setError("La contraseña debe tener al menos 6 caracteres");
            valido = false;
        } else {
            binding.layoutPassword.setError(null);
        }

        if (passwordConfirm.isEmpty()) {
            binding.layoutPasswordConfirm.setError("Confirma tu contraseña");
            valido = false;
        } else if (!password.equals(passwordConfirm)) {
            binding.layoutPasswordConfirm.setError("Las contraseñas no coinciden");
            valido = false;
        } else {
            binding.layoutPasswordConfirm.setError(null);
        }

        if (binding.toggleRol.getCheckedButtonId() == View.NO_ID) {
            Toast.makeText(this,
                    "Selecciona si eres cliente o negocio",
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (binding.cardDatosNegocio.getVisibility() == View.VISIBLE) {
            String nombreLocal = binding.etNombreLocal.getText().toString().trim();
            if (nombreLocal.isEmpty()) {
                binding.layoutNombreLocal.setError("Introduce el nombre de tu negocio");
                valido = false;
            } else {
                binding.layoutNombreLocal.setError(null);
            }
        }

        return valido;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}