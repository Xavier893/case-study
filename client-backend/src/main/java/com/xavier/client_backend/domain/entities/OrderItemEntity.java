package com.xavier.client_backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private int quantity;

    /**
     * This is the hashCode() method for the OrderItemEntity class.
     * It is used to generate a hash code for the object.
     *
     * @return the hash code for the object
     */
    @Override
    public int hashCode() {
        // Initialize the hash code to 7
        int hash = 7;

        // Multiply the hash code by 31 and add the hash code of the id
        // If the id is null, add 0 instead
        hash = 31 * hash + (id == null ? 0 : id.hashCode());

        // Multiply the hash code by 31 and add the quantity
        hash = 31 * hash + quantity;

        // Do not include the collection fields in the hash code
        // This is because the collections are not used in the equals() method
        // and including them in the hash code could lead to unexpected behavior

        // Return the final hash code
        return hash;
    }
}
