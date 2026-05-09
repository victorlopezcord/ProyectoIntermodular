/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.controller;

import GetToDay.dto.PeticionRegistro;
import GetToDay.dto.RespuestaGeneral;
import GetToDay.entity.Usuarios;
import GetToDay.entity.Negocios; 
import GetToDay.repository.UsuarioRepository;
import GetToDay.repository.NegocioRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author kiwitox
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RespuestaGeneral> registrar(@RequestBody PeticionRegistro peticion) {
        try {
            // 1. Verificar si el email ya existe
            if (usuarioRepository.findByEmail(peticion.getEmail()).isPresent()) {
                return ResponseEntity.ok(new RespuestaGeneral(false, "El email ya está registrado"));
            }

            // 2. Crear y guardar el Usuario
            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombreCompleto(peticion.getNombre());
            nuevoUsuario.setEmail(peticion.getEmail());
            nuevoUsuario.setTelefono(peticion.getTelefono());
            nuevoUsuario.setRol(peticion.getRol());
            
            // Ciframos la contraseña antes de guardar
            nuevoUsuario.setPasswordHash(passwordEncoder.encode(peticion.getPassword()));
            
            Usuarios usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            // 3. Si el rol es "negocio", creamos también la entrada en la tabla Negocios
            if ("negocio".equalsIgnoreCase(peticion.getRol())) {
                Negocios nuevoNegocio = new Negocios();
                nuevoNegocio.setNombreLocal(peticion.getNombreLocal());
                nuevoNegocio.setDireccion(peticion.getDireccion());
                nuevoNegocio.setDescripcion(peticion.getDescripcion());
                // Relacionamos el negocio con el usuario recién creado
                nuevoNegocio.setIdUsuario(usuarioGuardado); 
                
                negocioRepository.save(nuevoNegocio);
            }

            return ResponseEntity.ok(new RespuestaGeneral(true, "Registro completado con éxito"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RespuestaGeneral(false, "Error en el servidor: " + e.getMessage()));
        }
    }
}