package com.mercadolibre.bootcamp.projeto_integrador.controller;

import com.mercadolibre.bootcamp.projeto_integrador.dto.PathRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestConnectionResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestTimeResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.WarehouseResponse;
import com.mercadolibre.bootcamp.projeto_integrador.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final IWarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<Flux<WarehouseResponse>> createWarehouse(@RequestBody WarehouseRequestDto warehouseRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.save(warehouseRequestDto));
    }


    @GetMapping("/shortest-path")
    public Mono<PathShortestConnectionResponse> getShortestPath(@RequestBody PathRequestDto pathRequest) {

        return warehouseService.getShortestPath(pathRequest.getFrom(), pathRequest.getDestination());
    }

    @GetMapping("/shortest-path-in-time")
    public Mono<PathShortestTimeResponse>  getShortestPathInTime(@RequestBody PathRequestDto pathRequest) {

        return warehouseService.getShortestPathInTime(pathRequest.getFrom(), pathRequest.getDestination());
    }

    @ResponseStatus(
            value = HttpStatus.NOT_FOUND,
            reason = "Illegal arguments")
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentHandler() {

    }
}
