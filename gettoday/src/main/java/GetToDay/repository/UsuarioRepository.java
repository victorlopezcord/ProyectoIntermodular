/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import GetToDay.entity.Usuarios;
/**
 *
 * @author kiwitox
 */
public interface UsuarioRepository extends JpaRepository<Usuarios, Integer>{
    Optional<Usuarios> findByEmail(String email);
   
}
