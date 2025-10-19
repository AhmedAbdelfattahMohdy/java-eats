package com.food_delivery.javaeats.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "menu_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "menu_category_id")
    private Long menuCategoryId;

    @Column(name = "menu_item_name", nullable = false)
    private String menuItemName;

    @Column(name = "menu_item_description", columnDefinition = "TEXT")
    private String menuItemDescription;

    @Column(name = "menu_item_image_url")
    private String menuItemImageUrl;

    @Column(name = "menu_item_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal menuItemPrice;

    @Column(name = "menu_item_stock_quantity")
    private Integer menuItemStockQuantity;

    @Column(name = "menu_item_is_active")
    private Boolean menuItemIsActive;

    @Column(name = "menu_item_display_order")
    private Integer menuItemDisplayOrder;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;
}

