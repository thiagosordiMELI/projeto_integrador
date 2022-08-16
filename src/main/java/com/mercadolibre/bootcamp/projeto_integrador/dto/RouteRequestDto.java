package com.mercadolibre.bootcamp.projeto_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDto {

    @NotNull(message = "From cant be null")
    private long from;

    @NotNull(message = "Destination cant be null")
    private long destination;

    @NotNull(message = "Duration cant be null")
    private Double duration;
}
