package com.food_delivery.javaeats.repository;

import com.food_delivery.javaeats.entity.Cart;
import com.food_delivery.javaeats.enums.CartStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.cartId = :cartId")
    Optional<Cart> findByIdWithItems(@Param("cartId") Long cartId);

    Optional<Cart> findByCustomerIdAndStatus(Long customerId, CartStatus status);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.customerId = :customerId AND c.status = :status")
    Optional<Cart> findByCustomerIdAndStatusWithItems(@Param("customerId") Long customerId,
                                                      @Param("status") CartStatus status);
}
