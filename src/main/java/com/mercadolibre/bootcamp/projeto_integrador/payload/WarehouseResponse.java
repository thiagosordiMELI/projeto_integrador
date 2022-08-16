package com.mercadolibre.bootcamp.projeto_integrador.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponse {
    private long warehouseCode;
    private String location;
    private List<RouteResponse> routes;
}
