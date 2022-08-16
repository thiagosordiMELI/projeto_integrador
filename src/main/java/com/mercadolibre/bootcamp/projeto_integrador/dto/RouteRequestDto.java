package com.mercadolibre.bootcamp.projeto_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDto {

    @Min(value = 1, message = "From cant be 0")
    private long from;

    @Min(value = 1, message = "Destination cant be 0")
    private long destination;

    @NotNull(message = "Duration cant be null")
    private Double duration;
}
