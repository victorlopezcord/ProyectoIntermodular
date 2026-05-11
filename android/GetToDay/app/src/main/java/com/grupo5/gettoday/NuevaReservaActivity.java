package com.grupo5.gettoday;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.grupo5.gettoday.databinding.ActivityNuevaReservaBinding;

public class NuevaReservaActivity extends BaseActivity {

    /*
        NuevaReservaActivity extiende BaseActivity para
        obtener el menú inferior.

        FLUJO DE LA PANTALLA (3 pasos):
          1. Usuario elige negocio
             → se muestra info del negocio
             → se muestra desplegable de servicios
          2. Usuario elige servicio
             → se muestran horarios disponibles
          3. Usuario elige horario
             → aparece botón "Confirmar reserva"
             → la reserva queda AUTOMÁTICAMENTE confirmada
             → POST /api/citas con estado=confirmada

        CAMBIOS RESPECTO A LA VERSIÓN ANTERIOR:
          · Sin selector de empleado
          · Sin estado pendiente — la reserva se confirma
            directamente al crearla
          · Duración fija de 1 hora para todos los servicios

        TABLA QUE USA:
          CITAS (id_cliente, id_servicio,
                 fecha, hora_inicio, estado=confirmada)
    */

    private ActivityNuevaReservaBinding binding;

    // IDs seleccionados en cada paso
    private int idNegocioSeleccionado  = -1;
    private int idServicioSeleccionado = -1;
    private String fechaSeleccionada   = "";
    private String horaSeleccionada    = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        // Mostrar menú del cliente con "Reservar" seleccionado
        mostrarMenuCliente(R.id.navNuevaReserva);

        cargarNegocios();
        configurarSpinnerNegocio();
        configurarSpinnerServicio();
        configurarBtnReservar();
    }

    // ── PASO 1: NEGOCIO ───────────────────────────────────────────

    /**
     * Carga la lista de negocios en el desplegable.
     * TODO: GET /api/negocios
     * Devuelve: id_negocio, nombre_local
     */
    private void cargarNegocios() {
        String[] negociosPrueba = {
                "Barbería El Fígaro",
                "Peluquería Estilo",
                "Centro de Estética Bella"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                negociosPrueba
        );
        binding.spinnerNegocio.setAdapter(adapter);
    }

    /**
     * Cuando el usuario elige un negocio:
     *   · Muestra los datos del negocio
     *   · Carga y muestra el desplegable de servicios
     *   · Oculta horarios y botón si el usuario
     *     cambia de negocio después de haber avanzado
     */
    private void configurarSpinnerNegocio() {
        binding.spinnerNegocio.setOnItemClickListener(
                (parent, view, position, id) -> {

                    idNegocioSeleccionado = position;

                    // TODO: GET /api/negocios/{id_negocio}
                    mostrarInfoNegocio(
                            "Barbería El Fígaro",
                            "Calle Mayor, 10",
                            "La mejor barbería de la ciudad"
                    );

                    cargarServicios(idNegocioSeleccionado);

                    mostrarCard(binding.cardInfoNegocio);
                    mostrarCard(binding.cardServicio);

                    // Ocultar pasos siguientes si cambia de negocio
                    binding.cardHorarios.setVisibility(View.GONE);
                    binding.btnReservar.setVisibility(View.GONE);

                    // Resetear selecciones
                    idServicioSeleccionado = -1;
                    horaSeleccionada = "";
                });
    }

    private void mostrarInfoNegocio(String nombre,
                                    String direccion,
                                    String descripcion) {
        binding.tvNombreNegocio.setText(nombre);
        binding.tvDireccionNegocio.setText(direccion);
        binding.tvDescripcionNegocio.setText(descripcion);
    }

    // ── PASO 2: SERVICIO ──────────────────────────────────────────

    /**
     * Carga los servicios del negocio seleccionado.
     * TODO: GET /api/servicios/negocio/{id_negocio}
     * Devuelve: id_servicio, nombre_servicio
     * Duración: fija 1 hora para todos los servicios
     */
    private void cargarServicios(int idNegocio) {
        String[] serviciosPrueba = {
                "Corte de pelo",
                "Corte y barba",
                "Afeitado clásico"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                serviciosPrueba
        );
        binding.spinnerServicio.setAdapter(adapter);
    }

    /**
     * Cuando el usuario elige un servicio:
     *   · Carga los horarios disponibles
     *   · Muestra la card de horarios
     *   · Oculta el botón si el usuario cambia de servicio
     */
    private void configurarSpinnerServicio() {
        binding.spinnerServicio.setOnItemClickListener(
                (parent, view, position, id) -> {

                    idServicioSeleccionado = position;

                    cargarHorarios(idServicioSeleccionado);
                    mostrarCard(binding.cardHorarios);

                    // Ocultar botón si cambia de servicio
                    binding.btnReservar.setVisibility(View.GONE);
                    horaSeleccionada = "";
                });
    }

    // ── PASO 3: HORARIO ───────────────────────────────────────────

    /**
     * Carga los horarios disponibles para el servicio elegido.
     *
     * TODO: GET /api/citas/disponibilidad?
     *   id_negocio=X&id_servicio=Y&fecha=Z
     *
     * El backend debe:
     *   1. Obtener el horario del negocio ese día
     *      (tabla HORARIOS: dia_semana, hora_apertura, hora_cierre)
     *   2. Generar huecos de 1 hora en 1 hora
     *   3. Filtrar huecos donde ya haya una cita confirmada
     *      (tabla CITAS: estado=confirmada)
     *   4. Devolver solo los huecos libres
     *
     * EJEMPLO:
     *   Horario negocio lunes: 09:00 - 14:00
     *   Duración: 1 hora (fija)
     *   Huecos generados: 09:00, 10:00, 11:00, 12:00, 13:00
     *   Cita confirmada existente: 10:00
     *   Huecos disponibles: 09:00, 11:00, 12:00, 13:00
     */
    private void cargarHorarios(int idServicio) {
        binding.recyclerHorarios.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // TODO: conectar HorarioAdapter con huecos reales
        // Por ahora mostrar estado vacío
        binding.tvSinHorarios.setVisibility(View.VISIBLE);
        binding.recyclerHorarios.setVisibility(View.GONE);
    }

    /**
     * Se llama desde HorarioAdapter cuando el usuario
     * pulsa "Seleccionar" en un hueco horario.
     * Guarda la hora y muestra el botón de confirmar.
     *
     * TODO: llamar desde HorarioAdapter
     */
    public void seleccionarHorario(String fecha, String hora) {
        fechaSeleccionada = fecha;
        horaSeleccionada  = hora;

        // Mostrar botón con animación
        binding.btnReservar.setVisibility(View.VISIBLE);
        binding.btnReservar.setAlpha(0f);
        binding.btnReservar.animate()
                .alpha(1f).setDuration(300).start();
    }

    // ── CONFIRMACIÓN ──────────────────────────────────────────────

    /**
     * Crea la reserva directamente como confirmada.
     * No necesita aprobación del negocio.
     */
    private void configurarBtnReservar() {
        binding.btnReservar.setOnClickListener(v -> {

            if (idNegocioSeleccionado == -1) {
                Toast.makeText(this,
                        "Selecciona un negocio",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (idServicioSeleccionado == -1) {
                Toast.makeText(this,
                        "Selecciona un servicio",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (horaSeleccionada.isEmpty()) {
                Toast.makeText(this,
                        "Selecciona un horario",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: POST /api/citas con:
            //   · id_cliente  = id del usuario logueado
            //   · id_servicio = idServicioSeleccionado
            //   · fecha       = fechaSeleccionada
            //   · hora_inicio = horaSeleccionada
            //   · estado      = "confirmada"
            //   (la reserva se confirma directamente)
            Toast.makeText(this,
                    "¡Reserva confirmada correctamente!",
                    Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Muestra una card con animación de aparición suave.
     */
    private void mostrarCard(View card) {
        card.setVisibility(View.VISIBLE);
        card.setAlpha(0f);
        card.animate().alpha(1f).setDuration(300).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}