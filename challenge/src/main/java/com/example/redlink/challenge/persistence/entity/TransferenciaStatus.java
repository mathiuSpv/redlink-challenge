package com.example.redlink.challenge.persistence.entity;

public enum TransferenciaStatus {
    /*
    * No esta explicito pero creo que es una buena practica debido a que por lo que estuve buscando
    * no existe una logica intrinseca en los estado de la transferencia solo pasa de
    *  (Decision propia, si bien en el docs aparece Aprobado y No Aprobado)
    *   pending -> aproved
    *   pending -> rejected
    */
    APPROVED,
    REJECTED,
    PENDING
}
