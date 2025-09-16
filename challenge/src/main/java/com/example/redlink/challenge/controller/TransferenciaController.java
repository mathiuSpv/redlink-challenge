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

        if (transferenciaOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Transferencia transferencia = aplicarConversionIfHaveParam(
                transferenciaOpt.get(),
                moneda,
                tasa
        );
        return ResponseEntity.ok(transferencia);
    }

    // Caso c
    @GetMapping("/user/{userId}/aprobadas")
    public ResponseEntity<List<Transferencia>> listarAprobadas(
            @PathVariable String userId
    ) {
        if (!transferenciaService.existeUsuario(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Transferencia> aprobadas = transferenciaService.listarTransferenciaAprobadas(userId);

        return ResponseEntity.ok(aprobadas);
    }

    // Caso d
    @GetMapping("/user/{userId}/historial")
    public ResponseEntity<List<Transferencia>> historial(
            @PathVariable String userId
    ) {
        if (!transferenciaService.existeUsuario(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Transferencia> historial = transferenciaService.obtenerHistorialTransacciones(userId);

        return ResponseEntity.ok(historial);
    }

    private Transferencia aplicarConversionIfHaveParam(Transferencia t, String moneda, BigDecimal tasa) {
        // Si existen parametros de moneda y tasa entonces hace el cambio
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
