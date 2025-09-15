package com.example.redlink.challenge.controller;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.service.DivisaServices;
import com.example.redlink.challenge.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transferencia")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;
    private final DivisaServices divisaServices;

    // Caso a
    @PostMapping
    public ResponseEntity<Transferencia> crearTransferencia(@RequestBody Transferencia transferencia) {
        if (!divisaServices.monedaCorrecta(transferencia.getCurrency())) {
            return ResponseEntity.badRequest().build();
        }
        Transferencia creada = transferenciaService.crearTransferencia(transferencia);
        return ResponseEntity.ok(creada);
    }

    // Caso b
    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> consultarEstado(
            @PathVariable UUID id,
            @RequestParam(required = false) String moneda,
            @RequestParam(required = false) BigDecimal tasa
    ) {
        Optional<Transferencia> transferenciaOpt = transferenciaService.consultarEstadoTransferencia(id);

        if (transferenciaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Transferencia transferencia = transferenciaOpt.get();

        if (moneda != null && tasa != null) {
            if (!divisaServices.monedaCorrecta(moneda)) {
                return ResponseEntity.badRequest().build();
            }
            transferencia.setAmount(transferencia.getAmount());
            transferencia.setCurrency(moneda);
        }

        return ResponseEntity.ok(transferencia);
    }

    // Caso c
    @GetMapping("/user/{userId}/aprobadas")
    public ResponseEntity<List<Transferencia>> listarAprobadas(
            @PathVariable String userId,
            @RequestParam(required = false) String moneda,
            @RequestParam(required = false) BigDecimal tasa
    ) {
        List<Transferencia> aprobadas = transferenciaService.listarTransferenciaAprobadas(userId)
                .stream()
                .map(t -> aplicarConversion(t, moneda, tasa))
                .collect(Collectors.toList());

        return ResponseEntity.ok(aprobadas);
    }

    // Caso d
    @GetMapping("/user/{userId}/historial")
    public ResponseEntity<List<Transferencia>> historial(
            @PathVariable String userId,
            @RequestParam(required = false) String moneda,
            @RequestParam(required = false) BigDecimal tasa
    ) {
        boolean existeUsuario = transferenciaService.existeUsuario(userId);
        if (existeUsuario) {
            List<Transferencia> historial;
            historial = transferenciaService.obtenerHistorialTransacciones(userId)
                    .stream()
                    .map(t -> aplicarConversion(t, moneda, tasa))
                    .toList();
            return ResponseEntity.ok(historial);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Transferencia aplicarConversion(Transferencia t, String moneda, BigDecimal tasa) {
        if (moneda != null && tasa != null) {

            if (tasa.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("La tasa de conversión debe ser mayor a 0");
            }

            if (divisaServices.monedaCorrecta(moneda)) {
                t.setAmount(convertirMonto(t.getAmount(), tasa));
                t.setCurrency(moneda);
            }
        }
        return t;
    }

    private BigDecimal convertirMonto(BigDecimal amount, BigDecimal tasa) {
        return amount.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }
}
