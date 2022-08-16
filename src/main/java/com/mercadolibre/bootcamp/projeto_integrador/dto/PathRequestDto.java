package com.mercadolibre.bootcamp.projeto_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathRequestDto {
    private String from;
    private String destination;
}
