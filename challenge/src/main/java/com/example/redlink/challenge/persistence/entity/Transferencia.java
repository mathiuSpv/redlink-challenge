package com.example.redlink.challenge.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name="Transactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Transferencia {
    /*
    Tenemos en cuenta el ejemplo que aparece para estructurar
    Aclaramos, no entiendo como funciona un banco por pero intuyo que los datos
    dentro de la base de datos al momento de generar una transferencia necesita
    la transferencia_id que es un hash, en este caso usare uuid

    {
        "transaction_id": "def456",
        "user_id": "113411",
        "amount": "250.000,00",
        "currency": "EUR",
        "status": "PENDING",
        "created_at": "2024-10-14T08:45:30Z",
        "bank_code": "BANK123",
        "recipient_account": "DE89370400440532013000"
    }
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", unique = true, nullable = false)
    private UUID transactionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Positive
    @Column(name = "amount", nullable = false, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferenciaStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @Column(name = "recipient_account", nullable = false)
    private String recipientAccount;

}
