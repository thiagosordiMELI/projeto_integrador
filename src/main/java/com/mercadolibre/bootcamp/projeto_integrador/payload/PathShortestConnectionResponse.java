package com.mercadolibre.bootcamp.projeto_integrador.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathShortestConnectionResponse {
    private String from;
    private String to;
    private Integer totalConnections;

    public PathShortestConnectionResponse(PathShortestConnectionResponse pathShortestConnectionResponse) {
        this.from = pathShortestConnectionResponse.getFrom();
        this.to = pathShortestConnectionResponse.getTo();
        this.totalConnections = pathShortestConnectionResponse.getTotalConnections();
    }
}
