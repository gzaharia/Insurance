package com.internship.insurance.service;

import com.internship.insurance.dto.OrderDto;
import com.internship.insurance.model.Order;
import org.springframework.beans.BeanUtils;

public class OrderService {

    public static OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }
}
