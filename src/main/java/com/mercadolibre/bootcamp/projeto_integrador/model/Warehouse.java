package com.mercadolibre.bootcamp.projeto_integrador.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long warehouseCode;

    @Column(length = 50)
    private String location;

    public Warehouse(String location){
        this.location = location;
    }
}
