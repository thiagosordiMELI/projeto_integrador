package com.mercadolibre.bootcamp.projeto_integrador.controller;

import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.service.IRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/route")
@Validated
public class RouteController {

    private final IRouteService routeService;

    @GetMapping("/{routeId}")
    public ResponseEntity<Flux<RouteResponse>> getByRouteId(@PathVariable(value = "routeId") UUID routeId,
                                                            @RequestHeader("Manager-Id") long managerId) {
        return ResponseEntity.ok(routeService.getById(routeId, managerId));
    }

    @GetMapping("/{warehouseCode}/routes")
    public ResponseEntity<Flux<List<RouteResponse>>> getAllRoutes(@PathVariable(value = "warehouseCode") long warehouseCode,
                                                                  @RequestHeader("Manager-Id") long managerId){
        return ResponseEntity.ok(routeService.listAllByWarehouseCode(warehouseCode, managerId));
    }

    @PostMapping(value = "/create-route")
    public ResponseEntity<Flux<RouteResponse>> createRoute(@RequestBody @Valid RouteRequestDto routeDTO,
                                                           @RequestHeader("Manager-Id") long managerId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.save(routeDTO, managerId));
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<String> deleteRoute(@PathVariable(value = "routeId") UUID routeId,
                                              @RequestHeader("Manager-Id") long managerId) {
        routeService.delete(routeId, managerId);
        return ResponseEntity.ok("Route is deleted");
    }
}
