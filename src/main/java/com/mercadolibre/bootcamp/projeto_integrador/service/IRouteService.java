package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface IRouteService {
    Flux<List<RouteResponse>> listAllByWarehouseCode(long warehouseCode);
    Flux<RouteResponse> getById(UUID routeId);
    Flux<RouteResponse> save(RouteRequestDto routeDTO);
    void delete(UUID routeId);
}
