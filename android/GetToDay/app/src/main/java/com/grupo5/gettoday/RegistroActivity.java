package com.grupo5.gettoday;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grupo5.gettoday.databinding.ActivityRegistroBinding;

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

        // Listener que detecta cuando el usuario pulsa Cliente o Negocio
        binding.toggleRol.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

            // Solo actuamos cuando un botón SE ACTIVA, no cuando se desactiva
            if (!isChecked) return;

            if (checkedId == R.id.btnRolNegocio) {
                // Mostrar la card de datos del negocio con animación
                binding.cardDatosNegocio.setVisibility(View.VISIBLE);
                binding.cardDatosNegocio.setAlpha(0f);
                binding.cardDatosNegocio.animate().alpha(1f).setDuration(300).start();

            } else if (checkedId == R.id.btnRolCliente) {
                // Ocultar la card de datos del negocio
                binding.cardDatosNegocio.setVisibility(View.GONE);
            }
        });
    }

    private void configurarBotones() {

        // ── BOTÓN VOLVER ──────────────────────────────────────────
        binding.btnVolver.setOnClickListener(v -> {
            // Cierra esta Activity y vuelve al Login
            finish();
        });

        // ── BOTÓN CREAR CUENTA ────────────────────────────────────
        binding.btnCrearCuenta.setOnClickListener(v -> {
            if (validarFormulario()) {
                // TODO: conectar con POST /api/auth/register
                // Enviar: nombre, email, telefono, password, rol
                // Si rol = "negocio" enviar también: nombreLocal, direccion, descripcion
                Toast.makeText(this,
                        "Cuenta creada correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarFormulario() {

        // Recoger los valores de todos los campos
        String nombre   = binding.etNombre.getText().toString().trim();
        String email    = binding.etEmail.getText().toString().trim();
        String telefono = binding.etTelefono.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String passwordConfirm = binding.etPasswordConfirm.getText().toString();

        // Variable que controla si el formulario es válido
        boolean valido = true;

        // ── VALIDAR NOMBRE ────────────────────────────────────────
        if (nombre.isEmpty()) {
            binding.layoutNombre.setError("Introduce tu nombre completo");
            valido = false;
        } else {
            binding.layoutNombre.setError(null);
        }

        // ── VALIDAR EMAIL ─────────────────────────────────────────
        if (email.isEmpty()) {
            binding.layoutEmail.setError("Introduce tu correo electrónico");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutEmail.setError("El formato del email no es válido");
            valido = false;
        } else {
            binding.layoutEmail.setError(null);
        }

        // ── VALIDAR TELÉFONO ──────────────────────────────────────
        if (telefono.isEmpty()) {
            binding.layoutTelefono.setError("Introduce tu teléfono");
            valido = false;
        } else {
            binding.layoutTelefono.setError(null);
        }

        // ── VALIDAR CONTRASEÑA ────────────────────────────────────
        if (password.isEmpty()) {
            binding.layoutPassword.setError("Introduce una contraseña");
            valido = false;
        } else if (password.length() < 6) {
            binding.layoutPassword.setError("La contraseña debe tener al menos 6 caracteres");
            valido = false;
        } else {
            binding.layoutPassword.setError(null);
        }

        // ── VALIDAR CONFIRMAR CONTRASEÑA ──────────────────────────
        if (passwordConfirm.isEmpty()) {
            binding.layoutPasswordConfirm.setError("Confirma tu contraseña");
            valido = false;
        } else if (!password.equals(passwordConfirm)) {
            binding.layoutPasswordConfirm.setError("Las contraseñas no coinciden");
            valido = false;
        } else {
            binding.layoutPasswordConfirm.setError(null);
        }

        // ── VALIDAR ROL SELECCIONADO ──────────────────────────────
        if (binding.toggleRol.getCheckedButtonId() == View.NO_ID) {
            Toast.makeText(this,
                    "Selecciona si eres cliente o negocio",
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        // ── VALIDAR DATOS DEL NEGOCIO (solo si es negocio) ────────
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