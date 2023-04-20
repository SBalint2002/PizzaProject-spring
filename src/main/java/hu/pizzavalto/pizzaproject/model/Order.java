package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "location")
    private String location;

    @Column(name = "order_date")
    private Date order_date;

    @Column(name = "price")
    private int price;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "ready")
    private boolean ready;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderPizza> orderPizzas;

    public Order(Long userId, String location, Date order_date, int price, String phone_number, boolean ready){
        this.userId = userId;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
        this.orderPizzas = new ArrayList<>();
    }

    public void addPizzas(List<Pizza> pizzas) {
        for (Pizza pizza : pizzas) {
            orderPizzas.add(new OrderPizza(this, pizza));
        }
    }
}