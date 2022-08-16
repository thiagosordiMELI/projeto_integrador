package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestTimeResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.WarehouseResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWarehouseService {

    Flux<WarehouseResponse> save(WarehouseRequestDto warehouseRequestDto);
    Mono<PathShortestTimeResponse> getShortestPath(long purchaseId);
}
