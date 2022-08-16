package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.mapper.WarehouseMapper;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestConnectionResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestTimeResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.WarehouseResponse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IWarehouseNodeRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.internal.value.PathValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService{

    private final IWarehouseRepository warehouseRepository;

    private final IWarehouseNodeRepository warehouseNodeRepository;

    private final WarehouseMapper warehouseMapper;

    @Override
    public Flux<WarehouseResponse> save(WarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = new Warehouse(warehouseRequestDto.getLocation());
        warehouseRepository.save(warehouse);
        return warehouseNodeRepository.saveWarehouse(warehouse.getWarehouseCode(), warehouse.getLocation())
                .map(warehouseNode -> warehouseMapper.mapFromWarehouseToWarehouseResponse(warehouseNode));
    }


    @Override
    public Mono<PathShortestConnectionResponse> getShortestPath(String from, String to) {

        final Flux<PathValue> rows = warehouseNodeRepository.shortestPath(from, to);
        return rows
                .map(it -> this.convert(it.asPath()))
                .take(1)
                .next()
                .switchIfEmpty(Mono.empty())
                .map(PathShortestConnectionResponse::new)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Error")));

    }

    @Override
    public Mono<PathShortestTimeResponse> getShortestPathInTime(String from, String to) {

        final Flux<PathValue> rows = warehouseNodeRepository.shortestPathInTime(from, to);
        return rows
                .map(it -> this.convertTimePath(it.asPath()))
                .take(1)
                .next()
                .switchIfEmpty(Mono.empty())
                .map(PathShortestTimeResponse::new)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Error")));
    }


    private PathShortestConnectionResponse convert(org.neo4j.driver.types.Path connection) {

        String from = connection.start().get("name").asString();
        String to = connection.end().get("name").asString();
        int length = connection.length() - 1;

        return new PathShortestConnectionResponse(from, to, length);
    }

    private PathShortestTimeResponse convertTimePath(org.neo4j.driver.types.Path connection) {

        String from = connection.start().get("name").asString();
        String to = connection.end().get("name").asString();
        Double totalInTime = StreamSupport.stream(connection.nodes().spliterator(), false)
                .filter(node -> node.hasLabel("Route"))
                .mapToDouble(route -> route.get("duration").asDouble()).sum();

        return new PathShortestTimeResponse(from, to, totalInTime);
    }
}
