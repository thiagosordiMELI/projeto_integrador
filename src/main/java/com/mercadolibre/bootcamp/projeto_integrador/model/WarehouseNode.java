package com.mercadolibre.bootcamp.projeto_integrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.driver.internal.value.PathValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Node
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseNode {
    @Id
    @Property
    private long warehouseCode;

    @Property
    private String location;

    @Relationship(type="ROUTES", direction = Relationship.Direction.OUTGOING)
    private Set<Route> routes = new HashSet<>();

    public WarehouseNode(long warehouseCode, String location) {
        this.warehouseCode = warehouseCode;
        this.location = location;
    }
}
