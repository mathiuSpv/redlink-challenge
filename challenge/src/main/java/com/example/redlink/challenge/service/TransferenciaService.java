package com.example.redlink.challenge.service;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.persistence.entity.TransferenciaStatus;
import com.example.redlink.challenge.persistence.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferenciaService {
    /*
    * El service se encarga en armar la logica para la base de datos, aqui solo necesitamos 4 y otros
    * de caracter utilitario en tanto el el propio service como el controller.
     */
    private final TransferenciaRepository transferenciaRepository;
    private final Random random = new Random();

    // Caso a
    public Transferencia crearTransferencia(Transferencia transferencia) {
        transferencia.setCreatedAt(OffsetDateTime.now());
        transferencia.setStatus(generarEstadoAleatorio());
        return transferenciaRepository.save(transferencia);
    }

    // Caso b
    public Optional<Transferencia> consultarEstadoTransferencia(UUID transactionId) {
        return transferenciaRepository.findTransferenciaByTransactionId(transactionId);
    }

    // Caso c
    public List<Transferencia> listarTransferenciaAprobadas(String userId) {
        return transferenciaRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                userId,
                TransferenciaStatus.APPROVED
        );
    }

    // Caso d
    public List<Transferencia> obtenerHistorialTransacciones(String userId) {
        return transferenciaRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /*
     * Esta funcion en un entorno real no deberia existir, solo es para el caso de prueba de generacion de random status
     * Aparte de su nula escalabidad (Si se necesita modificar o agregar estados nuevos hay que cambiar codigo)
     */
    private TransferenciaStatus generarEstadoAleatorio() {
        TransferenciaStatus[] estados = {
                TransferenciaStatus.APPROVED,
                TransferenciaStatus.REJECTED,
        };
        return estados[random.nextInt(estados.length)];
    }

    public boolean existeUsuario(String userId) {
        return transferenciaRepository.existsByUserId(userId);
    }
}