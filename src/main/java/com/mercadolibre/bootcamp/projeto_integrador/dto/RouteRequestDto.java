package com.mercadolibre.bootcamp.projeto_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDto {

    private long from;
    private long destination;
    private Double duration;
}
