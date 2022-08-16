package com.mercadolibre.bootcamp.projeto_integrador.repository;

import com.mercadolibre.bootcamp.projeto_integrador.model.Route;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IRouteRepository extends ReactiveNeo4jRepository<Route,UUID> {

    @Query("MATCH (warehouse:WarehouseNode {warehouseCode: $warehouseCode})-[:ROUTES]->(route:Route) RETURN route, collect(warehouse)")
    Flux<List<Route>> listAllByWarehouseCode(long warehouseCode);

    @Query("MATCH (route:Route {id: $routeId}) RETURN route")
    Flux<Route> getById(UUID routeId);

    @Query("MATCH (warehouse:WarehouseNode {warehouseCode: $warehouseCode}) " +
            "MATCH (destinationWarehouse:WarehouseNode {warehouseCode: $destinationWarehouseCode}) " +
            "MERGE (warehouse)-[:ROUTES]->(route:Route {id: randomUUID(), from: $from, destination: $destination, " +
            "duration: $duration}) " +
            "-[:ROUTES]->(destinationWarehouse)" +
            "RETURN route")
    Flux<Route> saveRoute(long warehouseCode, long destinationWarehouseCode, String from, String destination, double duration);

    @Query("MATCH (route:Route {id: $routeId}) " +
            "OPTIONAL MATCH (route)-[r:ROUTES]-(warehouse:WarehouseNode)" +
            "DELETE route, r")
    void deleteRoute(UUID routeId);

}
