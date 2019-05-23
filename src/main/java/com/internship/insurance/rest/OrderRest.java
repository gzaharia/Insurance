package com.internship.insurance.rest;

import com.internship.insurance.config.EmailConfig;
import com.internship.insurance.model.*;
import com.internship.insurance.repository.InsuranceRepo;
import com.internship.insurance.repository.OrderRepo;
import com.internship.insurance.repository.PropertyRepo;
import com.internship.insurance.service.EmailService;
import freemarker.template.Configuration;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

    @Autowired
    EmailService emailService;

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

    private Double computePrice(Order order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));

        Double basePrice = insuranceRepo.getByTitle("RCA").getBasePrice();
        for (Property property : properties) {
            basePrice *= property.getCoefficient();
        }

        return Double.valueOf(new DecimalFormat("#.##").format(basePrice));
    }

    @PostMapping("/orders/price")
    public ResponseEntity<Double> getPrice(@RequestBody Order order) {
        return ResponseEntity.ok(
                computePrice(order)
                );
    }

    @PostMapping("orders/add")
    public Order addOneOrder(@RequestBody Order order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));
        order.setProperties(properties);
        order.setPrice(computePrice(order));
        order.setStatus(OrderStatus.PENDING);
        try {
            emailService.sendEmail(order, order.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            try {
                emailService.sendEmail(orderFromDb.get(), orderFromDb.get().getStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
