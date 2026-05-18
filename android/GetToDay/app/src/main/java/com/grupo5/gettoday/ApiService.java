package com.grupo5.gettoday;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    @POST("api/auth/login")
    Call<RespuestaGeneral> login(@Body PeticionLogin peticion);

    @POST("api/auth/register")
    Call<RespuestaGeneral> register(@Body Object peticionRegistro);

    @POST("api/auth/usuario")
    Call<RespuestaGeneral> obtenerUsuario(@Body PeticionEmail peticion);

    @PUT("api/auth/usuario/modificar")
    Call<RespuestaGeneral> modificarUsuario(@Body PeticionModificarUsuario peticion);
}