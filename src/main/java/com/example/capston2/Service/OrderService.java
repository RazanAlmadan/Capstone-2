package com.example.capston2.Service;

import com.example.capston2.Model.Order;
import com.example.capston2.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public void addOrder(Order order){
        orderRepository.save(order);
    }

    public Boolean updateOrder(Integer id, Order order){
        Order oldOrder = orderRepository.findOrderById(id);
        if (oldOrder == null){
            return false;
        }
        oldOrder.setClientId(order.getClientId());
        oldOrder.setDesignerId(order.getDesignerId());
        oldOrder.setPrice(order.getPrice());
        oldOrder.setDeadLine(order.getDeadLine());
        oldOrder.setStatus(order.getStatus());
        orderRepository.save(oldOrder);
        return true;
    }

    public Boolean deleteOrder(Integer id){
        Order oldOrder = orderRepository.findOrderById(id);
        if (oldOrder == null){
            return false;
        }
        orderRepository.delete(oldOrder);
        return true;
    }
}