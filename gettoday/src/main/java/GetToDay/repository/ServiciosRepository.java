/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.repository;


import GetToDay.entity.Servicios;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kiwitox
 */
@Repository
public interface ServiciosRepository extends JpaRepository<Servicios, Integer>{
    Optional<Servicios> findByIdServicio(Integer idServicio);
}
