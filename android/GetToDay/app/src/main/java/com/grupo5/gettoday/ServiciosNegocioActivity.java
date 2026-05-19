package com.grupo5.gettoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.grupo5.gettoday.databinding.ActivityServiciosNegocioBinding;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosNegocioActivity extends BaseActivity {

    private ActivityServiciosNegocioBinding binding;
    private ServicioAdapter adapter;
    private final List<Servicio> listaServicios = new ArrayList<>();

    private String emailUsuario;

    private boolean modoEdicion = false;
    private int idServicioEditando = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiciosNegocioBinding.inflate(getLayoutInflater());
        getContenedor().addView(binding.getRoot());

        mostrarMenuNegocio(R.id.navServicios);

        SharedPreferences prefs = getSharedPreferences("MiAppPrefs", MODE_PRIVATE);
        emailUsuario = prefs.getString("usuario_email", "");

        configurarRecycler();
        configurarBotones();
        cargarServicios();
    }

    // RECYCLER
    private void configurarRecycler() {
        adapter = new ServicioAdapter(listaServicios, new ServicioAdapter.OnServicioClickListener() {
            @Override
            public void onEditar(Servicio servicio) {
                modoEdicion = true;
                idServicioEditando = servicio.getIdServicio();
                binding.tvTituloFormServicio.setText("Editar servicio");
                binding.etNombreServicio.setText(servicio.getNombreServicio());
                binding.etDescripcionServicio.setText("");
                binding.etPrecioServicio.setText(String.valueOf((int) servicio.getPrecio()));
                mostrarFormulario();
            }

            @Override
            public void onEliminar(Servicio servicio) {
                confirmarEliminar(servicio);
            }
        });

        binding.recyclerServicios.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerServicios.setAdapter(adapter);
    }

    // BOTONES
    private void configurarBotones() {

        binding.btnAnadirServicio.setOnClickListener(v -> {
            modoEdicion = false;
            idServicioEditando = -1;
            limpiarFormulario();
            binding.tvTituloFormServicio.setText("Nuevo servicio");
            mostrarFormulario();
        });

        binding.btnCancelarServicio.setOnClickListener(v -> {
            binding.cardFormServicio.setVisibility(View.GONE);
            limpiarFormulario();
        });

        binding.btnGuardarServicio.setOnClickListener(v -> {
            if (!validarFormulario()) return;

            String nombre    = binding.etNombreServicio.getText().toString().trim();
            String precioStr = binding.etPrecioServicio.getText().toString().trim();
            int precio;
            try {
                precio = Integer.parseInt(precioStr);
            } catch (NumberFormatException e) {
                binding.layoutPrecioServicio.setError("El precio debe ser un número");
                return;
            }

            binding.btnGuardarServicio.setEnabled(false);

            if (modoEdicion) {
                modificarServicio(idServicioEditando, nombre, precio);
            } else {
                crearServicio(nombre, precio);
            }
        });
    }

    // LISTAR
    private void cargarServicios() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.listarServicios(new PeticionServicio(emailUsuario))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            runOnUiThread(() -> Toast.makeText(ServiciosNegocioActivity.this,
                                    "Error al obtener servicios (código " + response.code() + ")",
                                    Toast.LENGTH_SHORT).show());
                            return;
                        }

                        try {
                            String responseStr = response.body().string();

                            if (responseStr.isEmpty()) {
                                runOnUiThread(() -> Toast.makeText(ServiciosNegocioActivity.this,
                                        "El servidor no devolvió datos",
                                        Toast.LENGTH_SHORT).show());
                                return;
                            }

                            List<Servicio> lista = new ArrayList<>();
                            JsonReader reader = new JsonReader(new StringReader(responseStr));
                            reader.setLenient(true);

                            reader.beginObject();
                            while (reader.hasNext()) {
                                String field = reader.nextName();
                                if (field.equals("datos") && reader.peek() != JsonToken.NULL) {
                                    reader.beginArray();
                                    while (reader.hasNext()) {
                                        int id = 0;
                                        String nombre = "";
                                        double precio = 0;
                                        reader.beginObject();
                                        while (reader.hasNext()) {
                                            String f = reader.nextName();
                                            if (f.equals("idServicio")) {
                                                id = reader.nextInt();
                                            } else if (f.equals("nombreServicio")) {
                                                nombre = reader.nextString();
                                            } else if (f.equals("precio")) {
                                                precio = reader.nextDouble();
                                            } else {
                                                reader.skipValue();
                                            }
                                        }
                                        reader.endObject();
                                        lista.add(new Servicio(id, nombre, precio));
                                    }
                                    reader.endArray();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();

                            final List<Servicio> resultado = lista;
                            runOnUiThread(() -> mostrarLista(resultado));

                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(ServiciosNegocioActivity.this,
                                    "Error al parsear: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        runOnUiThread(() -> Toast.makeText(ServiciosNegocioActivity.this,
                                "Sin conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void mostrarLista(List<Servicio> lista) {
        listaServicios.clear();
        listaServicios.addAll(lista);
        adapter.notifyDataSetChanged();
        if (listaServicios.isEmpty()) {
            binding.tvSinServicios.setVisibility(View.VISIBLE);
        } else {
            binding.tvSinServicios.setVisibility(View.GONE);
        }
    }

    // CREAR
    private void crearServicio(String nombre, int precio) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.crearServicio(new PeticionServicio(emailUsuario, nombre, precio))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                        binding.btnGuardarServicio.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                            int nuevoId = 0;
                            Object datos = response.body().getDatos();
                            if (datos instanceof Number) {
                                nuevoId = ((Number) datos).intValue();
                            }
                            Servicio nuevo = new Servicio(nuevoId, nombre, precio);
                            listaServicios.add(nuevo);
                            adapter.notifyItemInserted(listaServicios.size() - 1);
                            binding.tvSinServicios.setVisibility(View.GONE);
                            binding.cardFormServicio.setVisibility(View.GONE);
                            limpiarFormulario();
                            Toast.makeText(ServiciosNegocioActivity.this,
                                    "Servicio añadido correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = "Error al crear";
                            if (response.body() != null) {
                                msg = response.body().getMensaje();
                            }
                            Toast.makeText(ServiciosNegocioActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        binding.btnGuardarServicio.setEnabled(true);
                        Toast.makeText(ServiciosNegocioActivity.this,
                                "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // MODIFICAR
    private void modificarServicio(int idServicio, String nombre, int precio) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.modificarServicio(new PeticionServicio(idServicio, nombre, precio))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                        binding.btnGuardarServicio.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                            // Buscar el servicio en la lista y actualizarlo
                            int posicion = -1;
                            for (int i = 0; i < listaServicios.size(); i++) {
                                if (listaServicios.get(i).getIdServicio() == idServicio) {
                                    posicion = i;
                                }
                            }
                            if (posicion != -1) {
                                listaServicios.get(posicion).setNombreServicio(nombre);
                                listaServicios.get(posicion).setPrecio(precio);
                                adapter.notifyItemChanged(posicion);
                            }
                            binding.cardFormServicio.setVisibility(View.GONE);
                            limpiarFormulario();
                            Toast.makeText(ServiciosNegocioActivity.this,
                                    "Servicio actualizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = "Error al modificar";
                            if (response.body() != null) {
                                msg = response.body().getMensaje();
                            }
                            Toast.makeText(ServiciosNegocioActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        binding.btnGuardarServicio.setEnabled(true);
                        Toast.makeText(ServiciosNegocioActivity.this,
                                "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ELIMINAR
    private void confirmarEliminar(Servicio servicio) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar servicio")
                .setMessage("¿Seguro que quieres eliminar \"" + servicio.getNombreServicio() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarServicio(servicio.getIdServicio()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarServicio(int idServicio) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.eliminarServicio(new PeticionServicio(idServicio))
                .enqueue(new Callback<RespuestaGeneral>() {
                    @Override
                    public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                            // Buscar y eliminar de la lista
                            int posicion = -1;
                            for (int i = 0; i < listaServicios.size(); i++) {
                                if (listaServicios.get(i).getIdServicio() == idServicio) {
                                    posicion = i;
                                }
                            }
                            if (posicion >= 0) {
                                listaServicios.remove(posicion);
                                adapter.notifyItemRemoved(posicion);
                            }
                            if (listaServicios.isEmpty()) {
                                binding.tvSinServicios.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(ServiciosNegocioActivity.this,
                                    "Servicio eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = "Error al eliminar";
                            if (response.body() != null) {
                                msg = response.body().getMensaje();
                            }
                            Toast.makeText(ServiciosNegocioActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                        Toast.makeText(ServiciosNegocioActivity.this,
                                "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // HELPERS FORMULARIO
    private boolean validarFormulario() {
        boolean valido = true;

        if (binding.etNombreServicio.getText().toString().trim().isEmpty()) {
            binding.layoutNombreServicio.setError("Introduce el nombre del servicio");
            valido = false;
        } else {
            binding.layoutNombreServicio.setError(null);
        }

        if (binding.etPrecioServicio.getText().toString().trim().isEmpty()) {
            binding.layoutPrecioServicio.setError("Introduce el precio");
            valido = false;
        } else {
            binding.layoutPrecioServicio.setError(null);
        }

        return valido;
    }

    private void limpiarFormulario() {
        binding.etNombreServicio.setText("");
        binding.etDescripcionServicio.setText("");
        binding.etPrecioServicio.setText("");
        binding.layoutNombreServicio.setError(null);
        binding.layoutPrecioServicio.setError(null);
    }

    private void mostrarFormulario() {
        binding.cardFormServicio.setVisibility(View.VISIBLE);
        binding.cardFormServicio.setAlpha(0f);
        binding.cardFormServicio.animate().alpha(1f).setDuration(300).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}