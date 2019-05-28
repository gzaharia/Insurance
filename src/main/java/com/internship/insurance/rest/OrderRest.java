package com.internship.insurance.rest;

import com.internship.insurance.dto.OrderDto;
import com.internship.insurance.model.InsuranceOffer;
import com.internship.insurance.model.Order;
import com.internship.insurance.model.OrderStatus;
import com.internship.insurance.model.Property;
import com.internship.insurance.repository.InsuranceRepo;
import com.internship.insurance.repository.OrderRepo;
import com.internship.insurance.repository.PropertyRepo;
import com.internship.insurance.service.EmailService;
import com.internship.insurance.service.OrderService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderRest {
    private final OrderRepo orderRepo;
    private final PropertyRepo propertyRepo;
    private final InsuranceRepo insuranceRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmailService emailService;

    public OrderRest(OrderRepo orderRepo, PropertyRepo propertyRepo, InsuranceRepo insuranceRepo, EmailService emailService) {

        this.orderRepo = orderRepo;
        this.propertyRepo = propertyRepo;
        this.insuranceRepo = insuranceRepo;
        this.emailService = emailService;
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
    public Order addOneOrder(@Valid @RequestBody Order order) {
        Set<Long> ids = order.getProperties().stream()
                .map(Property::getId)
                .collect(Collectors.toSet());

        Set<Property> properties = new HashSet<>(propertyRepo.findAllById(ids));
        order.setInsurance(insuranceRepo.findById(order.getInsurance().getId()).orElse(null));
        order.setProperties(properties);
        order.setPrice(computePrice(OrderService.toDto(order)));
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepo.save(order);
        try {
            emailService.sendEmail(savedOrder);
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
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
                emailService.sendEmail(orderFromDb.get());
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
            }
            orderRepo.save(orderFromDb.get());
            return ResponseEntity.ok(orderFromDb.get());
        }
        else {
            logger.error("Error: ", new NotFoundException("Order not found!").getMessage());
            return ResponseEntity.notFound().build();
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
            logger.error("Error: ", new NotFoundException("Order not found!").getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
