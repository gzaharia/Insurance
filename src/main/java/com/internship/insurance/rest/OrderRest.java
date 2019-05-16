package com.internship.insurance.rest;

import com.internship.insurance.model.Category;
import com.internship.insurance.model.Order;
import com.internship.insurance.repository.OrderRepo;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderRest {
    private final OrderRepo orderRepo;

    public OrderRest(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }
    @GetMapping("orders")
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
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
