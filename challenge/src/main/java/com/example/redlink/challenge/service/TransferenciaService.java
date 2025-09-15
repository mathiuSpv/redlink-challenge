package com.example.redlink.challenge.service;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.persistence.entity.TransferenciaStatus;
import com.example.redlink.challenge.persistence.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final Random random = new Random();

    public Transferencia crearTransferencia(Transferencia transferencia) {
        transferencia.setCreatedAt(OffsetDateTime.now());
        transferencia.setStatus(generarEstadoAleatorio());
        return transferenciaRepository.save(transferencia);
    }

    public Optional<Transferencia> consultarEstadoTransferencia(UUID transactionId) {
        return transferenciaRepository.findTransferenciaByTransactionId(transactionId);
    }

    public Optional<Transferencia> listarTransferenciaAprobadas(String userId) {
        return transferenciaRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                userId,
                TransferenciaStatus.APPROVED
        );
    }

    public Optional<Transferencia> obtenerHistorialTransacciones(String userId) {
        return transferenciaRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    private TransferenciaStatus generarEstadoAleatorio() {
        TransferenciaStatus[] estados = {
                TransferenciaStatus.APPROVED,
                TransferenciaStatus.REJECTED,
                TransferenciaStatus.PENDING
        };
        return estados[random.nextInt(estados.length)];
    }

    public boolean existeUsuario(String userId) {
        return transferenciaRepository.findByUserId(userId);
    }
}