package com.mercadolibre.bootcamp.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purchaseId;
    private LocalDate date;
    private String orderStatus;
    @ManyToOne
    @JoinColumn(name="warehouse_id", nullable = false)
    @JsonIgnore
    private Warehouse warehouse;
    @ManyToOne
    @JoinColumn(name="buyer_id", nullable = false)
    @JsonIgnore
    private Buyer buyer;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<BatchPurchaseOrder> batchPurchaseOrders;
    
}
