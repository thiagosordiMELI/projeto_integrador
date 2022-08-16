package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.RouteRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.integration.listeners.ResetDatabase;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.service.IRouteService;
import com.mercadolibre.bootcamp.projeto_integrador.service.IWarehouseService;
import org.junit.jupiter.api.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
@AutoConfigureMockMvc
@ResetDatabase
public class RouteControllerTest extends BaseControllerTest {
    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IRouteService routeService;

    private Manager manager;

    private RouteRequestDto validRouteRequestDto;
    private Flux<RouteResponse> routeResponse;



    @BeforeEach
    void setup() {
        manager = getSavedManager();
        warehouseService.save(getValidWarehouseRequestDto("Salvador"), manager.getManagerId());
        warehouseService.save(getValidWarehouseRequestDto("São Paulo"), manager.getManagerId());
        warehouseService.save(getValidWarehouseRequestDto("Rio de Janeiro"), manager.getManagerId());
        warehouseService.save(getValidWarehouseRequestDto("Porto Alegre"), manager.getManagerId());
        warehouseService.save(getValidWarehouseRequestDto("Santa Maria"), manager.getManagerId());

        routeService.save(getValidRouteRequestDto(1L, 2L, 24), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(1L, 3L, 20), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(2L, 1L, 24), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(2L, 3L, 5), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(2L, 4L, 24), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(3L, 2L, 5), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(4L, 2L, 24), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(4L, 3L, 26), manager.getManagerId());
        routeService.save(getValidRouteRequestDto(4L, 5L, 5), manager.getManagerId());
        routeResponse = routeService.save(getValidRouteRequestDto(5L, 4L, 5), manager.getManagerId());

        validRouteRequestDto = getValidRouteRequestDto(3L, 5L, 26);
    }

    @Test
    void createRoute_returnsCreated_whenIsGivenAValidInput() throws Exception {

        mockMvc.perform(post("/api/v1/route/create-route")
                        .content(asJsonString(validRouteRequestDto))
                        .header("Manager-Id", manager.getManagerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void createWarehouse_returnsError_whenIsGivenAnInvalidInput() throws Exception {
        RouteRequestDto invalid = validRouteRequestDto;
        invalid.setFrom(0);
        mockMvc.perform(post("/api/v1/route/create-route")
                        .content(asJsonString(invalid))
                        .header("Manager-Id", manager.getManagerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("From cant be 0"));
    }

    @Test
    void getRoute_returnsOk_whenIsGivenRouteId() throws Exception {

        mockMvc.perform(get("/api/v1/route/"+routeResponse.blockFirst().getId())
                .header("Manager-Id", manager.getManagerId()))
                .andExpect(status().isOk());

    }

}
