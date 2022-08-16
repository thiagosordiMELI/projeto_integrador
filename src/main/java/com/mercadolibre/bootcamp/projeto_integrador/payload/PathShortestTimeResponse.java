package com.mercadolibre.bootcamp.projeto_integrador.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathShortestTimeResponse {

    private String from;
    private String to;
    private Double totalInTime;

    public PathShortestTimeResponse(PathShortestTimeResponse pathShortestTimeResponse) {
        this.from = pathShortestTimeResponse.getFrom();
        this.to = pathShortestTimeResponse.getTo();
        this.totalInTime = pathShortestTimeResponse.getTotalInTime();
    }

}
