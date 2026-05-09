/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.service;

import GetToDay.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import GetToDay.repository.UsuarioRepository;

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
    return usuarioRepository.findByEmail(email)
            .map(u -> User.withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles(u.getRol())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
}
    
}
