package com.example.pizzaproject.order;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "pizza_id")
    private Long pizza_id;

    @Column(name = "location_id")
    private Long location_id;

    @Column(name = "order_date")
    private Date order_date;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;
}