package com.example.redlink.challenge.controller;

import com.example.redlink.challenge.persistence.entity.Transferencia;
import com.example.redlink.challenge.service.DivisaServices;
import com.example.redlink.challenge.service.TransferenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenciaControllerTest {

    @Mock
    private TransferenciaService transferenciaService;

    @Mock
    private DivisaServices divisaServices;

    @InjectMocks
    private TransferenciaController controller;

    private Transferencia transferencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transferencia = Transferencia.builder()
                .transactionId(UUID.randomUUID())
                .userId("1234")
                .amount(BigDecimal.valueOf(100))
                .currency("ARS")
                .createdAt(OffsetDateTime.now())
                .build();
    }

    // Caso a: Crear transferencia
    @Test
    void testCrearTransferenciaMonedaValida() {
        when(divisaServices.monedaCorrecta("ARS")).thenReturn(true);
        when(transferenciaService.crearTransferencia(transferencia)).thenReturn(transferencia);

        ResponseEntity<Transferencia> response = controller.crearTransferencia(transferencia);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ARS", response.getBody().getCurrency());
    }

    @Test
    void testCrearTransferenciaMonedaInvalida() {
        transferencia.setCurrency("ARS_INVALID");
        when(divisaServices.monedaCorrecta("ARS_INVALID")).thenReturn(false);

        ResponseEntity<Transferencia> response = controller.crearTransferencia(transferencia);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Caso b: Consultar estado
    @Test
    void testConsultarEstadoEncontrada() {
        UUID id = transferencia.getTransactionId();
        when(transferenciaService.consultarEstadoTransferencia(id)).thenReturn(Optional.of(transferencia));

        ResponseEntity<Transferencia> response = controller.consultarEstado(id, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ARS", response.getBody().getCurrency());
    }

    @Test
    void testConsultarEstadoNotFound() {
        UUID id = UUID.randomUUID();
        when(transferenciaService.consultarEstadoTransferencia(id)).thenReturn(Optional.empty());

        ResponseEntity<Transferencia> response = controller.consultarEstado(id, null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Caso c: Listar aprobadas
    @Test
    void testListarAprobadasUsuarioExiste() {
        when(transferenciaService.existeUsuario("1234")).thenReturn(true);
        when(transferenciaService.listarTransferenciaAprobadas("1234")).thenReturn(List.of(transferencia));

        ResponseEntity<List<Transferencia>> response = controller.listarAprobadas("1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testListarAprobadasUsuarioNoExiste() {
        when(transferenciaService.existeUsuario("9999")).thenReturn(false);

        ResponseEntity<List<Transferencia>> response = controller.listarAprobadas("9999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Caso d: Historial
    @Test
    void testHistorialUsuarioExiste() {
        when(transferenciaService.existeUsuario("1234")).thenReturn(true);
        when(transferenciaService.obtenerHistorialTransacciones("1234")).thenReturn(List.of(transferencia));

        ResponseEntity<List<Transferencia>> response = controller.historial("1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testHistorialUsuarioNoExiste() {
        when(transferenciaService.existeUsuario("4321")).thenReturn(false);

        ResponseEntity<List<Transferencia>> response = controller.historial("4321");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}