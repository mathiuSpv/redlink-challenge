package com.example.redlink.challenge.service;

import com.example.redlink.challenge.persistence.repository.DivisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DivisaServices {
    /*
    * Redudante en este challenge, pero es una capa de seguridad, para el controller.
    */


    private final DivisaRepository divisaRepository;

    public boolean monedaCorrecta(String codigo){
        return divisaRepository.existsByCodigo(codigo);
    }
}
