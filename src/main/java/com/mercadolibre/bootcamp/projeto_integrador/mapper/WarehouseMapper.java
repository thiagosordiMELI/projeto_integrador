package com.mercadolibre.bootcamp.projeto_integrador.mapper;

import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.model.WarehouseNode;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.WarehouseResponse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WarehouseMapper {

    private final IWarehouseRepository warehouseRepository;

    public WarehouseResponse mapFromWarehouseToWarehouseResponse(WarehouseNode warehouseNode){

        WarehouseResponse warehouseResponse = new WarehouseResponse();
        Warehouse warehouse = warehouseRepository.findById(warehouseNode.getWarehouseCode()).get();
        warehouseResponse.setWarehouseCode(warehouse.getWarehouseCode() != 0 ? warehouse.getWarehouseCode() : 0 );
        warehouseResponse.setLocation(warehouseNode.getLocation());

        List<RouteResponse> routeResponseList = new RouteMapper()
                .mapRouteListToRouteResponseList(new ArrayList<>(warehouseNode.getRoutes()));

        warehouseResponse.setRoutes(routeResponseList);

        return warehouseResponse;
    }

    /*public List<WarehouseResponse> mapWarehouseListToWarehouseResponseList(final List<Mono<WarehouseNode>> warehouseList) {
        return warehouseList != null
                ? warehouseList
                .stream()
                .map(this::mapFromWarehouseToWarehouseResponse)
                .collect(Collectors.toList())
                : null;
    }*/
}
