/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.controller;

import GetToDay.dto.PeticionRegistro;
import GetToDay.dto.PeticionUsuario;
import GetToDay.dto.RespuestaGeneral;
import GetToDay.entity.Usuarios;
import GetToDay.entity.Negocios; 
import GetToDay.repository.UsuarioRepository;
import GetToDay.repository.NegocioRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RespuestaGeneral> registrar(@RequestBody PeticionRegistro peticion) {
        try {
            if (usuarioRepository.findByEmail(peticion.getEmail()).isPresent()) {
                return ResponseEntity.ok(new RespuestaGeneral(false, "El email ya está registrado"));
            }
            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombreCompleto(peticion.getNombre());
            nuevoUsuario.setEmail(peticion.getEmail());
            nuevoUsuario.setTelefono(peticion.getTelefono());
            nuevoUsuario.setRol(peticion.getRol());
            
            nuevoUsuario.setPasswordHash(passwordEncoder.encode(peticion.getPassword()));
            Usuarios usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            if (peticion.getRol() == 1) {
                Negocios nuevoNegocio = new Negocios();
                nuevoNegocio.setNombreLocal(peticion.getNombreLocal());
                nuevoNegocio.setDireccion(peticion.getDireccion());
                nuevoNegocio.setDescripcion(peticion.getDescripcion());
 
                nuevoNegocio.setIdUsuario(usuarioGuardado); 
                negocioRepository.save(nuevoNegocio);
            }
            return ResponseEntity.ok(new RespuestaGeneral(true, "Registro completado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RespuestaGeneral(false, "Error en el servidor: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneral> login(@RequestBody GetToDay.dto.PeticionLogin peticion) {
        try {
            
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    peticion.getEmail(),
                    peticion.getPassword()
                )
            );
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
            Object principal = authentication.getPrincipal();      
            Integer rol = null;
            if (principal instanceof GetToDay.entity.Usuarios) {
                rol = ((GetToDay.entity.Usuarios) principal).getRol();
            } else {
                rol = usuarioRepository.findByEmail(peticion.getEmail())
                        .map(u -> u.getRol())
                        .orElse(0);
            }
            return ResponseEntity.ok(new RespuestaGeneral(true, "Login exitoso", rol));
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity.ok(new RespuestaGeneral(false, "Email o contraseña incorrectos"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RespuestaGeneral(false, "Error en el servidor: " + e.getMessage()));
        }
    }
    
    @PostMapping("/usuario")
    public ResponseEntity<RespuestaGeneral> obtenerUsuario(@RequestBody GetToDay.dto.PeticionUsuario peticion) {
        return usuarioRepository.findByEmail(peticion.getEmail())
            .map(userEntity -> {
                PeticionUsuario dto = new PeticionUsuario(
                    userEntity.getRol(),
                    userEntity.getNombreCompleto(),
                    userEntity.getEmail(),
                    userEntity.getTelefono(),
                    null, null, null
                );
                if (userEntity.getRol() == 1) {
                    negocioRepository.findByIdUsuario(userEntity).ifPresent(negocio -> {
                        dto.setNombreLocal(negocio.getNombreLocal());
                        dto.setDireccion(negocio.getDireccion());
                        dto.setDescripcion(negocio.getDescripcion());
                    });
                }
                return ResponseEntity.ok(new RespuestaGeneral(true, "Datos recuperados", dto));
            })
            .orElseGet(() -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaGeneral(false, "Usuario no encontrado", null));
            });
    }
    
    @PutMapping("/usuario/modificar")
    public ResponseEntity<RespuestaGeneral> modificarUsuario(@RequestBody GetToDay.dto.PeticionModificarUsuario peticion) {
        try {
            return usuarioRepository.findByEmail(peticion.getEmail())
                .map(usuario -> {
                    usuario.setNombreCompleto(peticion.getNombre());
                    usuario.setTelefono(peticion.getTelefono());
                    if (peticion.getPassword() != null && !peticion.getPassword().isEmpty()) {
                        usuario.setPasswordHash(passwordEncoder.encode(peticion.getPassword()));
                    }

                    usuarioRepository.save(usuario);
                    if (usuario.getRol() == 1) {
                        negocioRepository.findByIdUsuario(usuario).ifPresent(negocio -> {
                            negocio.setNombreLocal(peticion.getNombreLocal());
                            negocio.setDireccion(peticion.getDireccion());
                            negocio.setDescripcion(peticion.getDescripcion());
                            negocioRepository.save(negocio);
                        });
                    }
                    return ResponseEntity.ok(new RespuestaGeneral(true, "Usuario actualizado con éxito", usuario.getRol()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaGeneral(false, "No se encontró el usuario para modificar")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RespuestaGeneral(false, "Error al modificar: " + e.getMessage()));
        }
    }
}