package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "NEW_ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    public Order getOrder() {
        return order;
    }
}
