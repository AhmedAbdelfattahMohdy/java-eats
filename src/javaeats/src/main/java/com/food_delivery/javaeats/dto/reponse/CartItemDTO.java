package com.food_delivery.javaeats.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private Long menuItemId;
    private String menuItemName;
    private Integer quantity;
    private String specialInstructions;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
