package com.example.redlink.challenge.controller;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.service.DivisaServices;
import com.example.redlink.challenge.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transferencia")
@RequiredArgsConstructor
@Tag(name = "Transferencias", description = "Operaciones para manejar transferencias bancarias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;
    private final DivisaServices divisaServices;


    // Caso a
    @Operation(summary = "Crear una transferencia")
    @ApiResponse(responseCode = "200", description = "Transferencia exitosa")
    @ApiResponse(responseCode = "400", description = "Moneda inválida")
    @PostMapping
    public ResponseEntity<Transferencia> crearTransferencia(@RequestBody Transferencia transferencia) {
        if (!divisaServices.monedaCorrecta(transferencia.getCurrency())) {
            return ResponseEntity.badRequest().build();
        }
        Transferencia creada = transferenciaService.crearTransferencia(transferencia);
        return ResponseEntity.ok(creada);
    }

    // Caso b
    @Operation(summary = "Consultar estado transferencia", description = "Consulta el estado de una transferencia por UUID. Permite conversión de moneda")
    @ApiResponse(responseCode = "200", description = "Transferencia encontrada")
    @ApiResponse(responseCode = "404", description = "Transferencia no encontrada")
    @ApiResponse(responseCode = "500", description = "Moneda o Tasa invalida")
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
    @Operation(summary = "Listar aprobadas", description = "Lista todas las transferencias aprobadas de un usuario")
    @ApiResponse(responseCode = "200", description = "Transferencias encontradas")
    @ApiResponse(responseCode = "404", description = "Usuario no existe")
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
    @Operation(summary = "Historial de transferencias", description = "Devuelve todo el historial de transacciones de un usuario")
    @ApiResponse(responseCode = "200", description = "Historial encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no existe")
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
        if (moneda == null && tasa == null)
            return t;

        assert moneda != null;
        if (Objects.equals(moneda.toUpperCase(), t.getCurrency()))
            return t; // Seria mejor volver un error? No sabria como encarar esto.

        if (tasa.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("La tasa de conversión debe ser mayor a 0");

        if (!divisaServices.monedaCorrecta(moneda.toUpperCase()))
            throw new IllegalArgumentException("Tipo de moneda incorrecta");

        t.setAmount(convertirMonto(t.getAmount(), tasa));
        t.setCurrency(moneda.toUpperCase());
        return t;
    }

    private BigDecimal convertirMonto(BigDecimal amount, BigDecimal tasa) {
        return amount.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }
}
