package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Esperar 2 segundos y luego ir a la pantalla de Login
        // Handler: permite ejecutar código después de un tiempo de espera
        // 2000 = 2000 milisegundos = 2 segundos
        new Handler().postDelayed(() -> {


            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cierra el splash para que no se pueda volver atrás

        }, 2000);
    }
}