package com.example.redlink.challenge.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name="Currency")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Divisa {
    /*
    * Por lo que entendi es necesario tener una tabla de divisas (monedas) debido a que el cambio
    * de las mismas se van a generar en el endpoint (la tasa no es necesario porque se va a definir dentro del enpoint como parametro)
    * por ende esto seria una buena practica para verificar que existe el tipo de moneda que se busca cambiar.
    */

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="codigo", nullable = false, unique = true, length = 3)
    private String codigo;

    @Column(name="name", nullable = false)
    private String name;
}
