package com.food_delivery.javaeats.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @ToString.Exclude
    private Cart cart;

    @Column(name = "menu_item_id", nullable = false)
    private Long menuItemId;

    @Column(name = "cart_item_quantity", nullable = false)
    private Integer cartItemQuantity;

    @Column(name = "cart_item_special_instructions", columnDefinition = "TEXT")
    private String cartItemSpecialInstructions;

    @Column(name = "cart_item_unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal cartItemUnitPrice;

    @Column(name = "cart_item_total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal cartItemTotalPrice;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        calculateTotalPrice();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
        calculateTotalPrice();
    }

    /**
     * Calculate total price based on quantity and unit price
     */
    public void calculateTotalPrice() {
        if (cartItemQuantity != null && cartItemUnitPrice != null) {
            this.cartItemTotalPrice = cartItemUnitPrice
                    .multiply(BigDecimal.valueOf(cartItemQuantity));
        }
    }
}
