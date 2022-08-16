package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.bootcamp.projeto_integrador.dto.*;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.repository.*;
import com.mercadolibre.bootcamp.projeto_integrador.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class BaseControllerTest {
    protected final ObjectMapper objectMapper;
    protected final ModelMapper modelMapper;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ISectionRepository sectionRepository;
    @Autowired
    protected IWarehouseRepository warehouseRepository;
    @Autowired
    protected IManagerRepository managerRepository;
    @Autowired
    protected IProductRepository productRepository;
    @Autowired
    protected IBatchRepository batchRepository;
    @Autowired
    protected IInboundOrderRepository inboundOrderRepository;
    @Autowired
    protected IBatchPurchaseOrderRepository batchPurchaseOrderRepository;
    @Autowired
    protected IPurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    protected IBuyerRepository buyerRepository;

    public BaseControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        modelMapper = new ModelMapper();
    }

    protected InboundOrderRequestDto getValidInboundOrderRequestDto(Section section, BatchRequestDto... batchRequest) {
        InboundOrderRequestDto requestDto = new InboundOrderRequestDto();
        requestDto.setBatchStock(List.of(batchRequest));
        requestDto.setSectionCode(section.getSectionCode());
        return requestDto;
    }

    protected WarehouseRequestDto getValidWarehouseRequestDto(String location) {
        WarehouseRequestDto requestDto = new WarehouseRequestDto();
        requestDto.setLocation(location);
        return requestDto;
    }

    protected RouteRequestDto getValidRouteRequestDto(long from, long destination, double duration) {
        RouteRequestDto requestDto = new RouteRequestDto();
        requestDto.setFrom(from);
        requestDto.setDestination(destination);
        requestDto.setDuration(duration);
        return requestDto;
    }

    protected BatchRequestDto getInvalidBatchRequestDto(Product product) {
        BatchRequestDto batchRequest = new BatchRequestDto();
        batchRequest.setProductId(product.getProductId());

        // Valores inválidos.
        batchRequest.setProductPrice(new BigDecimal("-100.99"));
        batchRequest.setCurrentTemperature(-1);
        batchRequest.setMinimumTemperature(-1);
        batchRequest.setDueDate(LocalDate.now().minusWeeks(1));
        batchRequest.setManufacturingTime(LocalDateTime.now().plusDays(1));
        batchRequest.setManufacturingDate(LocalDate.now().plusDays(1));
        batchRequest.setInitialQuantity(-1);
        return batchRequest;
    }

    protected BatchRequestDto getValidBatchRequest(Product product) {
        BatchRequestDto batchRequest = BatchGenerator.newBatchRequestDTO();
        batchRequest.setProductId(product.getProductId());
        return batchRequest;
    }

    protected Batch getSavedBatch(BatchRequestDto batchRequest, InboundOrder inboundOrder) {
        Batch batch = BatchGenerator.mapBatchRequestDtoToBatch(batchRequest);
        batch.setInboundOrder(inboundOrder);
        return batchRepository.save(batch);
    }

    protected BatchRequestDto getBatchRequest(long productId) {
        BatchRequestDto batchRequest = BatchGenerator.newBatchRequestDTO();
        batchRequest.setProductId(productId);
        return batchRequest;
    }

    protected Buyer getSavedBuyer() {
        Buyer buyer = new Buyer();
        buyer.setUsername("Thiago");
        buyerRepository.save(buyer);
        return buyer;
    }

    protected Warehouse getSavedWarehouse() {
        Warehouse warehouse = WarehouseGenerator.newWarehouse();
        warehouseRepository.save(warehouse);
        return warehouse;
    }

    protected Warehouse getSavedWarehouseWithoutCode() {
        Warehouse warehouse = WarehouseGenerator.newWarehouseWithoutCode();
        warehouseRepository.save(warehouse);
        return warehouse;
    }

    protected Product getSavedFreshProduct() {
        return getSavedProduct(Section.Category.FRESH);
    }

    protected Product getSavedProduct(Section.Category category) {
        Product product = ProductsGenerator.newProductFresh();
        product.setCategory(category);
        productRepository.save(product);
        return product;
    }

    protected Section getSavedFreshSection(Warehouse warehouse, Manager manager) {
        return getSavedFreshSection(warehouse, manager, 5);
    }

    protected Section getSavedFreshSection(Warehouse warehouse, Manager manager, int maxBatches) {
        Section section = SectionGenerator.getFreshSection(warehouse, manager);
        section.setMaxBatches(maxBatches);
        sectionRepository.save(section);
        return section;
    }

    protected Section getSavedSection(Warehouse warehouse, Manager manager, Section.Category category) {
        Section section = SectionGenerator.getFreshSection(warehouse, manager);
        section.setCategory(category);
        sectionRepository.save(section);
        return section;
    }

    protected Manager getSavedManager() {
        Manager manager = ManagerGenerator.newManager();
        managerRepository.save(manager);
        return manager;
    }

    protected PurchaseOrder getSavedPurchaseOrder(Warehouse warehouse, Buyer buyer, List<Batch> batches) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouse(warehouse);
        purchaseOrder.setDate(LocalDate.now());
        purchaseOrder.setOrderStatus("Opened");
        purchaseOrder.setBuyer(buyer);
        purchaseOrderRepository.save(purchaseOrder);
        for(Batch batch : batches){
            getSavedBatchPurchaseOrder(batch, purchaseOrder);
        }
        return purchaseOrder;
    }

    protected BatchPurchaseOrder getSavedBatchPurchaseOrder(Batch batch, PurchaseOrder purchaseOrder) {
        BatchPurchaseOrder batchPurchaseOrder = new BatchPurchaseOrder();
        batchPurchaseOrder.setPurchaseOrder(purchaseOrder);
        batchPurchaseOrder.setBatch(batch);
        batchPurchaseOrder.setUnitPrice(new BigDecimal(12));
        batchPurchaseOrder.setQuantity(5);
        return batchPurchaseOrderRepository.save(batchPurchaseOrder);
    }

    protected InboundOrder getSavedInboundOrder(Section section) {
        InboundOrder inboundOrder = new InboundOrder();
        inboundOrder.setOrderDate(LocalDate.now());
        inboundOrder.setSection(section);
        return inboundOrderRepository.save(inboundOrder);
    }

    protected Batch getSavedBatch(Product product, InboundOrder order) {
        Batch batch = BatchGenerator.newBatch(product, order);
        batchRepository.save(batch);
        return batch;
    }

    protected Batch getSavedBatch(LocalDate dueDate, Product product, InboundOrder order) {
        Batch batch = BatchGenerator.newBatch(dueDate, product, order);
        batchRepository.save(batch);
        return batch;
    }

    protected String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected List<BatchRequestDto> getValidListBatchRequest(Product product) {
        List<BatchRequestDto> batches = BatchGenerator.newList2BatchRequestsDTO();
        batches.forEach(b -> b.setProductId(product.getProductId()));
        return batches;
    }

    protected InboundOrderRequestDto getValidInboundOrderRequestDtoWithBatchList(Section section, List<BatchRequestDto> batchRequest) {
        InboundOrderRequestDto requestDto = new InboundOrderRequestDto();
        requestDto.setBatchStock(batchRequest);
        requestDto.setSectionCode(section.getSectionCode());
        return requestDto;
    }
}
