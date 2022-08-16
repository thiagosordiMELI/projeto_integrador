package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.NotFoundException;
import com.mercadolibre.bootcamp.projeto_integrador.mapper.RouteMapper;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IRouteRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService implements IRouteService{

    private final IRouteRepository routeRepository;

    private final IWarehouseRepository warehouseRepository;

    private final RouteMapper routeMapper;

    @Override
    public Flux<List<RouteResponse>> listAllByWarehouseCode(long warehouseCode) {
        return routeRepository.listAllByWarehouseCode(warehouseCode)
                .map(routes -> routeMapper.mapRouteListToRouteResponseList(routes));
    }

    @Override
    public Flux<RouteResponse> getById(UUID routeId) {
        return routeRepository.getById(routeId)
                .map(route -> routeMapper.mapFromRouteToRouteResponse(route));
    }

    @Override
    public Flux<RouteResponse> save(RouteRequestDto routeDTO) {
        String from = warehouseRepository.findById(routeDTO.getFrom()).orElseThrow(() -> new NotFoundException("Warehouse")).getLocation();
        String destination = warehouseRepository.findById(routeDTO.getDestination()).orElseThrow(() -> new NotFoundException("Warehouse")).getLocation();
        double duration = routeDTO.getDuration();

        return routeRepository.saveRoute(routeDTO.getFrom(),routeDTO.getDestination(),from,destination,duration)
                .map(route -> routeMapper.mapFromRouteToRouteResponse(route));
    }

    @Override
    public void delete(UUID routeId) {
        routeRepository.deleteRoute(routeId);
    }
}
