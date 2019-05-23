package com.internship.insurance.rest;

import com.internship.insurance.dto.OrderDto;
import com.internship.insurance.model.*;
import com.internship.insurance.repository.InsuranceRepo;
import com.internship.insurance.repository.OrderRepo;
import com.internship.insurance.repository.PropertyRepo;
import com.internship.insurance.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderRest {
    private final OrderRepo orderRepo;
    private final PropertyRepo propertyRepo;
    private final InsuranceRepo insuranceRepo;

    public OrderRest(OrderRepo orderRepo, PropertyRepo propertyRepo, InsuranceRepo insuranceRepo) {
        this.orderRepo = orderRepo;
        this.propertyRepo = propertyRepo;
        this.insuranceRepo = insuranceRepo;
    }

    @GetMapping("orders")
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    @GetMapping("orders/pending")
    public List<Order> getPendingOrders() {
        return orderRepo.findAllByStatus(OrderStatus.PENDING);
    }

    @GetMapping("orders/approved")
    public List<Order> getApprovedOrders() {
        return orderRepo.findAllByStatus(OrderStatus.APPROVED);
    }

    @GetMapping("orders/declined")
    public List<Order> getDeclinedOrders() {
        return orderRepo.findAllByStatus(OrderStatus.DECLINED);
    }

    @GetMapping("orders/{id}")
    public Order getOneOrder(@PathVariable Long id) throws NotFoundException {
        Optional<Order> orderFromDb = orderRepo.findById(id);
        if (orderFromDb.isPresent()) {
            return orderFromDb.get();
        } else {
            throw new NotFoundException("Order not found");
        }
    }

    @GetMapping("/orders/insurance/{orderId}")
    public InsuranceOffer getInsuranceFromOrder(@PathVariable Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);

        if (order.isPresent()) {
            Optional<InsuranceOffer> insuranceOffer = insuranceRepo.findById(order.get().getInsurance().getId());
            return insuranceOffer.orElse(null);
        }

        return null;
    }

    private Double computePrice(OrderDto order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));

        Double basePrice = insuranceRepo.getByTitle(order.getInsurance().getTitle()).getBasePrice();
        for (Property property : properties) {
            basePrice *= property.getCoefficient();
        }

        return Double.valueOf(new DecimalFormat("#.##").format(basePrice));
    }

    @PostMapping("/orders/price")
    public ResponseEntity<Double> getPrice(@RequestBody OrderDto order) {
        return ResponseEntity.ok(computePrice(order));
    }

    @PostMapping("orders/add")
    public Order addOneOrder(@RequestBody Order order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));
        order.setInsurance(insuranceRepo.findById(order.getInsurance().getId()).orElse(null));
        order.setProperties(properties);
        order.setPrice(computePrice(OrderService.toDto(order)));
        order.setStatus(OrderStatus.PENDING);
        orderRepo.save(order);
        return order;
    }

    @PutMapping("orders/edit/status/{id}")
    public ResponseEntity<Order> editOrderStatus(
            @PathVariable Long id,
            @RequestBody String status
    ) throws NotFoundException {
        Optional<Order> orderFromDb = orderRepo.findById(id);
        OrderStatus orderStatus;
        if(orderFromDb.isPresent()) {
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                orderStatus = OrderStatus.PENDING;
            }
            orderFromDb.get().setStatus(orderStatus);
            orderRepo.save(orderFromDb.get());
            return ResponseEntity.ok(orderFromDb.get());
        }
        else {
            throw new NotFoundException("Order not found!");
        }
    }

    @PutMapping("orders/edit/{id}")
    public ResponseEntity<Order> editOneOrder(
            @PathVariable Long id,
            @RequestBody Order orderDetails
    ) throws NotFoundException {
        Optional<Order> orderFromDb = orderRepo.findById(id);
        if(orderFromDb.isPresent()) {
            BeanUtils.copyProperties(orderDetails, orderFromDb.get());
            orderRepo.save(orderFromDb.get());
            return ResponseEntity.ok(orderFromDb.get());

        }
        else{
            throw new NotFoundException("Order not found !");
        }
    }
}
