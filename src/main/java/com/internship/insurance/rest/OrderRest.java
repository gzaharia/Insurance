package com.internship.insurance.rest;

import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Order;
import com.internship.insurance.model.Property;
import com.internship.insurance.repository.InsuranceRepo;
import com.internship.insurance.repository.OrderRepo;
import com.internship.insurance.repository.PropertyRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
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

    @PostMapping("/orders/price")
    public ResponseEntity<Double> getPrice(@RequestBody Order order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));

        Double basePrice = insuranceRepo.getByTitle("RCA").getBasePrice();
        for (Property property : properties) {
            basePrice *= property.getCoefficient();
        }

        return ResponseEntity.ok(
                Double.valueOf(
                        new DecimalFormat("#.##").format(basePrice)
                )
        );
    }

    @PostMapping("orders/add")
    public Order addOneOrder(@RequestBody Order order) {
        orderRepo.save(order);
        return order;
    }

    @PutMapping("orders/edit/{id}")
    public ResponseEntity<Order> editOneOrder(
            @PathVariable Long id,
            @RequestBody Order orderDetails
    ) throws NotFoundException{
        Optional<Order> orderFromDb = orderRepo.findById(id);
        if(orderFromDb.isPresent()) {
            BeanUtils.copyProperties(orderDetails, orderFromDb.get());
            orderFromDb.get().setId(id);
            orderFromDb.get().setPrice(orderDetails.getPrice());
            orderFromDb.get().setStatus(orderDetails.getStatus());
            orderFromDb.get().setTime_created(orderDetails.getTime_created());
            orderFromDb.get().setTime_updated(orderDetails.getTime_updated());
            orderFromDb.get().setProperties(orderDetails.getProperties());
            orderRepo.save(orderFromDb.get());
            return ResponseEntity.ok(orderFromDb.get());

        }
        else{
            throw new NotFoundException("Order not found !");
        }
    }
}
