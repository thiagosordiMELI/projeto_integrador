package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.WarehouseRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.integration.listeners.ResetDatabase;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.payload.PathShortestTimeResponse;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import com.mercadolibre.bootcamp.projeto_integrador.service.IRouteService;
import com.mercadolibre.bootcamp.projeto_integrador.service.IWarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Array;
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
public class WarehouseControllerTest extends BaseControllerTest {
    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IRouteService routeService;

    private Manager manager;
    private Buyer buyer;
    private Buyer buyerUnauthorized;
    private PurchaseOrder purchaseOrder;

    private InboundOrderRequestDto validInboundOrderRequest;
    private InboundOrderRequestDto invalidInboundOrderRequest;

    private WarehouseRequestDto validWarehouseRequestDto;

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
        routeService.save(getValidRouteRequestDto(5L, 4L, 5), manager.getManagerId());

        Warehouse warehouse = warehouseRepository.findById(1L).get();

        buyer = getSavedBuyer();
        buyerUnauthorized = getSavedBuyer();

        Section section = getSavedFreshSection(warehouse, manager);

        Product product = getSavedFreshProduct();

        InboundOrder inboundOrder = getSavedInboundOrder(section);

        Batch batch = getSavedBatch(product, inboundOrder);

        purchaseOrder = getSavedPurchaseOrder(warehouseRepository.findById(5L).get(), buyer, Arrays.asList(batch));

        validWarehouseRequestDto = getValidWarehouseRequestDto("Belo Horizonte");
    }

    @Test
    void createWarehouse_returnsCreated_whenIsGivenAValidInput() throws Exception {

        mockMvc.perform(post("/api/v1/warehouse")
                        .content(asJsonString(validWarehouseRequestDto))
                        .header("Manager-Id", manager.getManagerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertThat(warehouseRepository.findAll().size()).isEqualTo(6);
        assertThat(warehouseRepository.findAll().get(5).getLocation()).isEqualTo(validWarehouseRequestDto.getLocation());
    }

    @Test
    void createWarehouse_returnsError_whenIsGivenAnInvalidInput() throws Exception {
        WarehouseRequestDto invalid = validWarehouseRequestDto;
        invalid.setLocation("");
        mockMvc.perform(post("/api/v1/warehouse")
                        .content(asJsonString(invalid))
                        .header("Manager-Id", manager.getManagerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Location cant be empty"));
    }

    @Test
    void createWarehouse_returnsError_whenIsNotGivenManagerIdHeader() throws Exception {

        mockMvc.perform(post("/api/v1/warehouse")
                        .content(asJsonString(validWarehouseRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Header Manager-Id is required"));
    }

    @Test
    void createWarehouse_returnsError_whenIsGivenNotExistentManagerIdHeader() throws Exception {

        mockMvc.perform(post("/api/v1/warehouse")
                .content(asJsonString(validWarehouseRequestDto))
                .header("Manager-Id", 400)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There is no manager with the specified id"));
    }

    @Test
    void getDeliveryEstimated_returnsOk_whenIsGivenBuyerId() throws Exception {

        mockMvc.perform(get("/api/v1/warehouse/"+purchaseOrder.getPurchaseId()+"/delivery-estimated")
                .header("Buyer-Id", buyer.getBuyerId()))
                .andExpect(status().isOk());

    }

    @Test
    void getDeliveryEstimated_returnsException_whenIsNotGivenBuyerId() throws Exception {

        mockMvc.perform(get("/api/v1/warehouse/"+purchaseOrder.getPurchaseId()+"/delivery-estimated"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Header Buyer-Id is required"));

    }

    @Test
    void getDeliveryEstimated_returnsException_whenBuyerIsNotAuthorized() throws Exception {

        mockMvc.perform(get("/api/v1/warehouse/"+purchaseOrder.getPurchaseId()+"/delivery-estimated")
                .header("Buyer-Id", buyerUnauthorized.getBuyerId()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("not authorized to perform actions")));

    }

    @Test
    void getDeliveryEstimated_returnsException_whenPurchaseOrderNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/warehouse/400/delivery-estimated")
                .header("Buyer-Id", buyer.getBuyerId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("There is no purchase order with the specified id"));

    }

}
