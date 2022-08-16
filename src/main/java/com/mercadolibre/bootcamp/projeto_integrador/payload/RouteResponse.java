package com.mercadolibre.bootcamp.projeto_integrador.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private UUID id;
    private String from;
    private String destination;
    private Double duration;
}
