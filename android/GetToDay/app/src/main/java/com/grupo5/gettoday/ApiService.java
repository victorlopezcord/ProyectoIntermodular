package com.grupo5.gettoday;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    // ── AUTH ──────────────────────────────────────────────────────
    @POST("api/auth/login")
    Call<RespuestaGeneral> login(@Body PeticionLogin peticion);

    @POST("api/auth/register")
    Call<RespuestaGeneral> register(@Body Object peticionRegistro);

    @POST("api/auth/usuario")
    Call<RespuestaGeneral> obtenerUsuario(@Body PeticionEmail peticion);

    @PUT("api/auth/usuario/modificar")
    Call<RespuestaGeneral> modificarUsuario(@Body PeticionModificarUsuario peticion);

    // ── NEGOCIOS ──────────────────────────────────────────────────
    @POST("api/negocios/listar")
    Call<RespuestaGeneral> listarNegocios();

    // ── SERVICIOS ─────────────────────────────────────────────────
    @POST("api/servicios/listar")
    Call<ResponseBody> listarServicios(@Body PeticionServicio peticion);

    @POST("api/servicios/listarPorNegocio")
    Call<RespuestaGeneral> listarServiciosPorNegocio(@Body PeticionServicio peticion);

    @POST("api/servicios/crear")
    Call<RespuestaGeneral> crearServicio(@Body PeticionServicio peticion);

    @PUT("api/servicios/modificar")
    Call<RespuestaGeneral> modificarServicio(@Body PeticionServicio peticion);

    @HTTP(method = "DELETE", path = "api/servicios/eliminar", hasBody = true)
    Call<RespuestaGeneral> eliminarServicio(@Body PeticionServicio peticion);

    // ── CITAS ─────────────────────────────────────────────────────
    @POST("api/citas/horasOcupadas")
    Call<RespuestaGeneral> horasOcupadas(@Body PeticionCita peticion);

    @POST("api/citas/crear")
    Call<RespuestaGeneral> crearCita(@Body PeticionCita peticion);

    @POST("api/citas/cliente")
    Call<RespuestaGeneral> citasCliente(@Body PeticionCita peticion);

    @POST("api/citas/negocio")
    Call<RespuestaGeneral> citasNegocio(@Body PeticionCita peticion);

    @PUT("api/citas/modificar")
    Call<RespuestaGeneral> modificarCita(@Body PeticionCita peticion);
}