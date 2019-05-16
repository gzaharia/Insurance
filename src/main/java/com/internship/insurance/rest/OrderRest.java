package com.internship.insurance.rest;

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

    @PutMapping("orders/edit/{id}")
    public ResponseEntity<Order> editOneOrder(
            @PathVariable Long id,
            @RequestBody Order orderDetails
    ) throws NotFoundException{
        Optional<Order> orderFromDb = orderRepo.findById(id);
        if(orderFromDb.isPresent()) {
            BeanUtils.copyProperties(orderDetails, orderFromDb.get());
            orderFromDb.get().setId(id);
            orderRepo.save(orderFromDb.get());
            return ResponseEntity.ok(orderFromDb.get());

        }
        else{
            throw new NotFoundException("Order not found !");
        }
    }
}
