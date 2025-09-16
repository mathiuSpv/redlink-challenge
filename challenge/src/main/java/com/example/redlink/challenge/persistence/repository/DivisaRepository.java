package com.example.redlink.challenge.persistence.repository;

import com.example.redlink.challenge.persistence.entity.Divisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisaRepository extends JpaRepository<Divisa, Integer> {
    /*
    * La idea de DivisaRepository es crear una validacion existsByCodigo,
    * esto tambien lo podriamos utilizar para tener el nombre completo de la divisa.
     */

    boolean existsByCodigo(String codigo);
}
