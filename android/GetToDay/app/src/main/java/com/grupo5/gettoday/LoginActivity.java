package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grupo5.gettoday.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // ViewBinding: nos permite acceder a todos los elementos del layout
    // sin necesidad de usar findViewById() cada vez
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar el binding con el layout activity_login.xml
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarBotones();
    }

    private void configurarBotones() {

        // ── BOTÓN ENTRAR ──────────────────────────────────────────
        binding.btnLogin.setOnClickListener(v -> {

            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            if (email.isEmpty()) {
                binding.layoutEmail.setError("Introduce tu correo electrónico");
                return;
            }
            if (password.isEmpty()) {
                binding.layoutPassword.setError("Introduce tu contraseña");
                return;
            }

            binding.layoutEmail.setError(null);
            binding.layoutPassword.setError(null);

            // TODO: conectar con POST /api/auth/login
            // Si rol = "cliente"  → HomeClienteActivity
            // Si rol = "negocio"  → HomeNegocioActivity

            // PRUEBA TEMPORAL: si el email contiene "negocio"
            // navega al panel del negocio, si no al del cliente
            if (email.contains("negocio")) {
                Intent intent = new Intent(this, HomeNegocioActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, HomeClienteActivity.class);
                startActivity(intent);
            }
            finish();
        });

        // ── BOTÓN OLVIDÉ CONTRASEÑA ───────────────────────────────
        binding.btnForgotPassword.setOnClickListener(v -> {

            String email = binding.etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                // Si el campo email está vacío, pedimos que lo rellene primero
                binding.layoutEmail.setError("Introduce tu email para recuperar la contraseña");
                return;
            }

            // TODO: conectar con POST /api/auth/recuperar
            // Enviar: email
            Toast.makeText(this,
                    "Se enviará un email de recuperación a " + email,
                    Toast.LENGTH_LONG).show();
        });

        // ── BOTÓN IR A REGISTRO ───────────────────────────────────
        binding.btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar el binding al destruir la Activity
        // Evita memory leaks (fugas de memoria)
        binding = null;
    }
}