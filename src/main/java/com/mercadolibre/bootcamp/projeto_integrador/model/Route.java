package com.mercadolibre.bootcamp.projeto_integrador.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;

@Node
@Getter
@Setter
@NoArgsConstructor
public class Route {
    @Id
    @Property
    private UUID id;

    @Property
    private String from;

    @Property
    private String destination;

    @Property
    private Double duration;

    @Relationship("ROUTES")
    private Warehouse destinationWarehouse;


}
