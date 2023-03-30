package com.example.pizzaproject.order;

import com.example.pizzaproject.pizza.Pizza;
import com.example.pizzaproject.pizza.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final NewOrderRepository newOrderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, PizzaRepository pizzaRepository, NewOrderRepository newOrderRepository) {
        this.orderRepository = orderRepository;
        this.pizzaRepository = pizzaRepository;
        this.newOrderRepository = newOrderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByIdsInNewOrders() {
        List<NewOrder> newOrders = newOrderRepository.findAll();
        List<Long> orderIds = newOrders.stream().map(NewOrder::getOrder_id).collect(Collectors.toList());
        return orderRepository.findAllById(orderIds);
    }


    public void addNewOrder(Order order){
        Order savedOrder = orderRepository.save(order);
        NewOrder newOrder = new NewOrder();
        newOrder.setOrder_id(savedOrder.getId());
        newOrderRepository.save(newOrder);
    }

    public int sumPrice(List<Long> pizzaIds){
        var sum = 0;
        for (Long pizzaId : pizzaIds){
            Pizza pizza = pizzaRepository.findById(pizzaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza not found"));
            sum += pizza.getPrice();
        }
        return sum;
    }

    public NewOrder findNewOrderById(Long id) {
        return newOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with id " + id + " does not exist"));
    }



    @Transactional
    public void updateOrder(Long orderId, Order updateOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));

        if (updateOrder.isReady()) {
            order.setReady(true);
        }
        if (!updateOrder.isReady()) {
            order.setReady(false);
        }

        Optional<NewOrder> optionalNewOrder = newOrderRepository.findNewOrderById(orderId);
        optionalNewOrder.ifPresent(newOrderRepository::delete);
    }
}
