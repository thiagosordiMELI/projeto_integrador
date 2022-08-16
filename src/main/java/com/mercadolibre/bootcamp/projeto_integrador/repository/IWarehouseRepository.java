package com.mercadolibre.bootcamp.projeto_integrador.repository;

import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import org.neo4j.driver.internal.value.PathValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IWarehouseRepository extends JpaRepository<Warehouse, Long> {
}
