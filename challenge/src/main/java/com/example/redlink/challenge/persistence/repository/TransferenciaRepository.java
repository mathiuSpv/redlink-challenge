package com.example.redlink.challenge.persistence.repository;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.persistence.entity.TransferenciaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, BigInteger> {
    /*
    * Los otros casos de uso a y b se generan automaticamente sus "querys"
    * en el caso del punto b con un findById (con la uuid de la transferencia como parametro)
    * Caso a Se genera en el service utilizando la funcion .save del TransferenciaRepository
    */

    // Caso de uso punto c
    Optional<Transferencia> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, TransferenciaStatus status );

    // Caso de uso punto d
    Optional<Transferencia> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Transferencia> findTransferenciaByTransactionId(UUID transactionId);
}
