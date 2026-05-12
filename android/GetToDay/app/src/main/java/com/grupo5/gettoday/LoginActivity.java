package com.grupo5.gettoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail         = findViewById(R.id.etEmail);
        etPassword      = findViewById(R.id.etPassword);
        layoutEmail     = findViewById(R.id.layoutEmail);
        layoutPassword  = findViewById(R.id.layoutPassword);
        btnLogin        = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> intentarLogin());
        btnGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
    }

    private void intentarLogin() {
        layoutEmail.setError(null);
        layoutPassword.setError(null);

        String email = "";
        if (etEmail.getText() != null) {
            email = etEmail.getText().toString().trim();
        }

        String password = "";
        if (etPassword.getText() != null) {
            password = etPassword.getText().toString().trim();
        }

        if (email.isEmpty()) {
            layoutEmail.setError("Introduce tu email");
            return;
        }
        if (password.isEmpty()) {
            layoutPassword.setError("Introduce tu contraseña");
            return;
        }

        hacerLogin(email, password);
    }

    private void hacerLogin(String email, String password) {
        btnLogin.setEnabled(false);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.login(new PeticionLogin(email, password)).enqueue(new Callback<RespuestaGeneral>() {

            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                btnLogin.setEnabled(true);

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LoginActivity.this,
                            "Error del servidor (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                RespuestaGeneral res = response.body();
                if (!res.isExito()) {
                    Toast.makeText(LoginActivity.this, res.getMensaje(), Toast.LENGTH_SHORT).show();
                    return;
                }

                int rol = ((Number) res.getDatos()).intValue();
                SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);

                prefs.edit()
                        .putString("usuario_email", email)
                        .putInt("usuario_rol", rol)
                        .apply();

                // Cargar los datos del perfil del servidor
                cargarDatosUsuario(email, rol, prefs);
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,
                        "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosUsuario(String email, int rol, SharedPreferences prefs) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.obtenerUsuario(new PeticionEmail(email)).enqueue(new Callback<RespuestaGeneral>() {

            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {

                    // Convertimos los datos de la respuesta a DatosUsuario
                    Gson gson = new Gson();
                    DatosUsuario datos = gson.fromJson(gson.toJson(response.body().getDatos()), DatosUsuario.class);

                    if (datos != null) {
                        String nombre = "";
                        if (datos.getNombre() != null) {
                            nombre = datos.getNombre();
                        }

                        String telefono = "";
                        if (datos.getTelefono() != null) {
                            telefono = datos.getTelefono();
                        }

                        String negocioNombre = "";
                        if (datos.getNombreLocal() != null) {
                            negocioNombre = datos.getNombreLocal();
                        }

                        String negocioDireccion = "";
                        if (datos.getDireccion() != null) {
                            negocioDireccion = datos.getDireccion();
                        }

                        String negocioDescripcion = "";
                        if (datos.getDescripcion() != null) {
                            negocioDescripcion = datos.getDescripcion();
                        }

                        prefs.edit()
                                .putString("usuario_nombre",      nombre)
                                .putString("usuario_telefono",    telefono)
                                .putString("negocio_nombre",      negocioNombre)
                                .putString("negocio_direccion",   negocioDireccion)
                                .putString("negocio_descripcion", negocioDescripcion)
                                .apply();
                    }
                }
                navegarSegunRol(rol);
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                navegarSegunRol(rol);
            }
        });
    }

    private void navegarSegunRol(int rol) {
        Intent intent;
        if (rol == 1) {
            intent = new Intent(LoginActivity.this, HomeNegocioActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, HomeClienteActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}