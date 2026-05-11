/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.service;

import GetToDay.entity.Usuarios;
import GetToDay.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



/**
 *
 * @author kiwitox
 */
@Service
public class DetallesUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<Usuarios> oUsuario = usuarioRepository.findByEmail(email);

        // 2. Comprobamos si el usuario existe
        if (oUsuario.isPresent()) {
            Usuarios u = oUsuario.get();

            return User.withUsername(u.getEmail())
                       .password(u.getPasswordHash())
                       .roles(String.valueOf(u.getRol()))
                       .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
    

