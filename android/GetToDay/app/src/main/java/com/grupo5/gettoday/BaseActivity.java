package com.grupo5.gettoday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    /*
        BaseActivity es la clase PADRE de todas las pantallas
        principales del cliente y del negocio.

        Proporciona automáticamente:
          · El menú inferior correcto (cliente o negocio)
          · La navegación entre pantallas configurada

        MENÚ CLIENTE (3 pestañas):
          · Inicio → HomeClienteActivity
          · Reservar → NuevaReservaActivity
          · Perfil → PerfilClienteActivity

        MENÚ NEGOCIO (3 pestañas):
          · Inicio → HomeNegocioActivity
          · Servicios → ServiciosNegocioActivity
          · Perfil → PerfilNegocioActivity
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void mostrarMenuCliente(int itemSeleccionado) {
        BottomNavigationView menu = findViewById(R.id.menuCliente);
        menu.setVisibility(View.VISIBLE);
        menu.setSelectedItemId(itemSeleccionado);

        menu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {
                if (!(this instanceof HomeClienteActivity)) {
                    startActivity(new Intent(this,
                            HomeClienteActivity.class));
                    finish();
                }

            } else if (id == R.id.navNuevaReserva) {
                if (!(this instanceof NuevaReservaActivity)) {
                    startActivity(new Intent(this,
                            NuevaReservaActivity.class));
                    finish();
                }

            } else if (id == R.id.navPerfil) {
                if (!(this instanceof PerfilClienteActivity)) {
                    startActivity(new Intent(this,
                            PerfilClienteActivity.class));
                    finish();
                }
            }
            return true;
        });
    }

    protected void mostrarMenuNegocio(int itemSeleccionado) {
        BottomNavigationView menu = findViewById(R.id.menuNegocio);
        menu.setVisibility(View.VISIBLE);
        menu.setSelectedItemId(itemSeleccionado);

        menu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {
                if (!(this instanceof HomeNegocioActivity)) {
                    startActivity(new Intent(this,
                            HomeNegocioActivity.class));
                    finish();
                }

            } else if (id == R.id.navServicios) {
                if (!(this instanceof ServiciosNegocioActivity)) {
                    startActivity(new Intent(this,
                            ServiciosNegocioActivity.class));
                    finish();
                }

            } else if (id == R.id.navPerfil) {
                if (!(this instanceof PerfilNegocioActivity)) {
                    startActivity(new Intent(this,
                            PerfilNegocioActivity.class));
                    finish();
                }
            }
            return true;
        });
    }

    protected FrameLayout getContenedor() {
        return findViewById(R.id.contenedor);
    }
}