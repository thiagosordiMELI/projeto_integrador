package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.NotFoundException;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.UnauthorizedBuyerException;
import com.mercadolibre.bootcamp.projeto_integrador.mapper.WarehouseMapper;
import com.mercadolibre.bootcamp.projeto_integrador.model.PurchaseOrder;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestTimeResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.WarehouseResponse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IManagerRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IPurchaseOrderRepository;
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

    private final IPurchaseOrderRepository purchaseOrderRepository;

    private final IManagerRepository managerRepository;

    private final WarehouseMapper warehouseMapper;

    @Override
    public Flux<WarehouseResponse> save(WarehouseRequestDto warehouseRequestDto, long managerId) {

        managerRepository.findById(managerId).orElseThrow(() -> new NotFoundException("Manager"));

        Warehouse warehouse = new Warehouse(warehouseRequestDto.getLocation());
        warehouseRepository.save(warehouse);
        return warehouseNodeRepository.saveWarehouse(warehouse.getWarehouseCode(), warehouse.getLocation())
                .map(warehouseNode -> warehouseMapper.mapFromWarehouseToWarehouseResponse(warehouseNode));
    }

    @Override
    public Mono<PathShortestTimeResponse> getShortestPath(long purchaseId, long buyerId) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId).orElseThrow(() -> new NotFoundException("Purchase Order"));
        if(purchaseOrder.getBuyer().getBuyerId() != buyerId){
            throw new UnauthorizedBuyerException(buyerId, purchaseId);
        }

        final Flux<PathValue> rows = warehouseNodeRepository.shortestPath("São Paulo", purchaseOrder.getWarehouse().getLocation());
        return rows
                .map(it -> this.convertTimePath(it.asPath()))
                .take(1)
                .next()
                .switchIfEmpty(Mono.empty())
                .map(PathShortestTimeResponse::new)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Error")));
    }


    private PathShortestTimeResponse convertTimePath(org.neo4j.driver.types.Path connection) {

        String from = connection.start().get("location").asString();
        String to = connection.end().get("location").asString();
        Double totalInTime = StreamSupport.stream(connection.nodes().spliterator(), false)
                .filter(node -> node.hasLabel("Route"))
                .mapToDouble(route -> route.get("duration").asDouble()).sum();

        return new PathShortestTimeResponse(from, to, totalInTime);
    }
}
