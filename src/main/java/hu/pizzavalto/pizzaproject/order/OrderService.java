package hu.pizzavalto.pizzaproject.order;

import hu.pizzavalto.pizzaproject.model.NewOrder;
import hu.pizzavalto.pizzaproject.model.Order;
import hu.pizzavalto.pizzaproject.model.Pizza;
import hu.pizzavalto.pizzaproject.repository.PizzaRepository;
import hu.pizzavalto.pizzaproject.repository.NewOrderRepository;
import hu.pizzavalto.pizzaproject.repository.OrderRepository;
import hu.pizzavalto.pizzaproject.user.UserService;
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
    private final UserService userService;
    private final PizzaRepository pizzaRepository;
    private final NewOrderRepository newOrderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, PizzaRepository pizzaRepository, NewOrderRepository newOrderRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.pizzaRepository = pizzaRepository;
        this.newOrderRepository = newOrderRepository;
    }

    public List<Order> getOrdersByIdsInNewOrders() {
        List<NewOrder> newOrders = newOrderRepository.findAll();
        List<Long> orderIds = newOrders.stream()
                .map(no -> no.getOrder().getId())
                .collect(Collectors.toList());
        return orderRepository.findAllById(orderIds);
    }

    @Transactional
    public void addNewOrder(Order order){
        Order savedOrder = orderRepository.save(order);
        NewOrder newOrder = new NewOrder();
        newOrder.setOrder(savedOrder);
        newOrderRepository.save(newOrder);
    }

    public int sumPrice(List<Long> pizzaIds){
        var sum = 0;
        for (Long pizzaId : pizzaIds){
            Pizza pizza = pizzaRepository.findById(pizzaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza nem található!"));
            sum += pizza.getPrice();
        }
        return sum;
    }

    public List<Order> getOrderById(String token) {
       return orderRepository.findOrdersByUserId(userService.getUserIdFromToken(token));
    }

    @Transactional
    public void updateOrder(Long orderId, Order updateOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Rendelés " + orderId + " azonosítóval nem létezik!"));

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
