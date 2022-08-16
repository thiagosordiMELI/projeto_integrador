package com.mercadolibre.bootcamp.projeto_integrador.controller;

import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.service.IRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/route")
public class RouteController {

    private final IRouteService routeService;

    @GetMapping("/{routeId}")
    public ResponseEntity<Flux<RouteResponse>> getByRouteId(@PathVariable(value = "routeId") UUID routeId) {
        return ResponseEntity.ok(routeService.getById(routeId));
    }

    @GetMapping("/{warehouseCode}/routes")
    public ResponseEntity<Flux<List<RouteResponse>>> getAllRoutes(@PathVariable(value = "warehouseCode") long warehouseCode){
        return ResponseEntity.ok(routeService.listAllByWarehouseCode(warehouseCode));
    }

    @PostMapping(value = "/create-route")
    public ResponseEntity<Flux<RouteResponse>> createRoute(@RequestBody RouteRequestDto routeDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.save(routeDTO));
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<String> deleteRoute(@PathVariable(value = "routeId") UUID routeId) {
        routeService.delete(routeId);
        return ResponseEntity.ok("Route is deleted");
    }
}
