package com.food_delivery.javaeats.repository;

import com.food_delivery.javaeats.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    @Query(value = """
        SELECT ci.*, c.*, cust.*
        FROM cart_item ci
        JOIN cart c ON ci.cart_id = c.cart_id
        JOIN customer cust ON c.customer_id = cust.customer_id
        WHERE ci.cart_item_id = :cartItemId
        """, nativeQuery = true)
    CartItem findByIdWithCart(@Param("cartItemId") Long cartItemId);

    List<CartItem> findByCart_CartId(Long cartId);
    Optional<CartItem> findByCart_CartIdAndMenuItemId(Long cartId, Long menuItemId);

}
