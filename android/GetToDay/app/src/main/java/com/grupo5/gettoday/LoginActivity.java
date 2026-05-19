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
import com.google.gson.JsonElement;

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
        String password = "";

        if (etEmail.getText() != null) {
            email = etEmail.getText().toString().trim();
        }
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

        btnLogin.setEnabled(false);

        final String emailFinal = email;
        final String passwordFinal = password;

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.login(new PeticionLogin(emailFinal, passwordFinal)).enqueue(new Callback<RespuestaGeneral>() {
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

                int rol = ((JsonElement) res.getDatos()).getAsInt();

                SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
                prefs.edit()
                        .putString("usuario_email", emailFinal)
                        .putInt("usuario_rol", rol)
                        .apply();

                cargarDatosUsuario(emailFinal, rol, prefs);
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
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getDatos());
                    DatosUsuario datos = gson.fromJson(json, DatosUsuario.class);

                    if (datos != null) {
                        String nombre = datos.getNombre() != null ? datos.getNombre() : "";
                        String telefono = datos.getTelefono() != null ? datos.getTelefono() : "";
                        String nombreLocal = datos.getNombreLocal() != null ? datos.getNombreLocal() : "";
                        String direccion = datos.getDireccion() != null ? datos.getDireccion() : "";
                        String descripcion = datos.getDescripcion() != null ? datos.getDescripcion() : "";

                        prefs.edit()
                                .putString("usuario_nombre_"      + email, nombre)
                                .putString("usuario_telefono_"    + email, telefono)
                                .putString("negocio_nombre_"      + email, nombreLocal)
                                .putString("negocio_direccion_"   + email, direccion)
                                .putString("negocio_descripcion_" + email, descripcion)
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