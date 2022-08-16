package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface IRouteService {
    Flux<List<RouteResponse>> listAllByWarehouseCode(long warehouseCode, long managerId);
    Flux<RouteResponse> getById(UUID routeId, long managerId);
    Flux<RouteResponse> save(RouteRequestDto routeDTO, long managerId);
    void delete(UUID routeId, long managerId);
}
