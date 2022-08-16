package com.mercadolibre.bootcamp.projeto_integrador.repository;

import com.mercadolibre.bootcamp.projeto_integrador.model.WarehouseNode;
import org.neo4j.driver.internal.value.PathValue;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface IWarehouseNodeRepository extends ReactiveNeo4jRepository<WarehouseNode, Long> {

    @Query("CREATE (warehouse:WarehouseNode {warehouseCode: $warehouseCode, location: $location}) RETURN warehouse")
    Flux<WarehouseNode> saveWarehouse(long warehouseCode, String location);


    @Query("MATCH p=shortestPath((a:Warehouse {name:$from})-[*..20]->(b:Warehouse {name:$to})) RETURN p")
    Flux<PathValue> shortestPath(@Param("from") String from, @Param("to") String to);

    @Query("MATCH (a:Warehouse {name: $from})\n"
            + "MATCH (b:Warehouse {name: $to})\n"
            + "CALL apoc.algo.dijkstra(a, b, 'ROUTES', 'duration')\n"
            + "YIELD path, weight\n"
            + "RETURN path\n"
            + "ORDER BY weight ASC LIMIT 1")
    Flux<PathValue> shortestPathInTime(@Param("from") String from, @Param("to") String to);


}
