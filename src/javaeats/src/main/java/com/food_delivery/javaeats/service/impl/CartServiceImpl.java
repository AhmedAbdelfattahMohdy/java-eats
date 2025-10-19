package com.food_delivery.javaeats.service.impl;

import com.food_delivery.javaeats.dto.reponse.CartDTO;
import com.food_delivery.javaeats.dto.reponse.CartItemDTO;
import com.food_delivery.javaeats.dto.reponse.ModifyCartItemResponse;
import com.food_delivery.javaeats.dto.request.ModifyCartItemRequest;
import com.food_delivery.javaeats.entity.Cart;
import com.food_delivery.javaeats.entity.CartItem;
import com.food_delivery.javaeats.entity.MenuItem;
import com.food_delivery.javaeats.enums.CartStatus;
import com.food_delivery.javaeats.exception.custom.CustomerNotFoundException;
import com.food_delivery.javaeats.exception.custom.InvalidCartStatusException;
import com.food_delivery.javaeats.exception.custom.MenuItemNotFoundException;
import com.food_delivery.javaeats.exception.custom.UnauthorizedCartAccessException;
import com.food_delivery.javaeats.repository.CartItemRepository;
import com.food_delivery.javaeats.repository.CartRepository;
import com.food_delivery.javaeats.entity.Customer;
import com.food_delivery.javaeats.repository.CustomerRepository;
import com.food_delivery.javaeats.repository.MenuItemRepository;
import com.food_delivery.javaeats.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public ModifyCartItemResponse modifyCartItem(Long cartItemId, ModifyCartItemRequest request, Long userId) {
        log.info("User {} modifying cart item {} - new quantity: {}",
                userId, cartItemId, request.getNewQuantity());

        // ========================================================================
        // STEP 1: GET CUSTOMER FROM USER_ID
        // ========================================================================
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerNotFoundException(userId));
        log.info("Found customer {} for user {}", customer.getCustomerId(), userId);

        // ========================================================================
        // STEP 2: LOAD CART ITEM WITH ASSOCIATED CART
        // ========================================================================
        CartItem cartItem = cartItemRepository.findByIdWithCart(cartItemId);
        Cart cart = cartItem.getCart();
        log.info("Found cart item {} with cart {}", cartItem.getCartItemId(), cart.getCartId());

        // ========================================================================
        // STEP 3: VERIFY CART OWNERSHIP
        // ========================================================================
        if (!cart.getCustomerId().equals(customer.getCustomerId())) {
            log.warn("User {} (customer {}) attempted to modify cart item {} belonging to customer {}",
                    userId, customer.getCustomerId(), cartItemId, cart.getCustomerId());
            throw new UnauthorizedCartAccessException();
        }

        // ========================================================================
        // STEP 4: VERIFY CART STATUS IS ACTIVE
        // ========================================================================
        if (cart.getStatus() != CartStatus.ACTIVE) {
            log.warn("User {} attempted to modify cart item {} belonging to cart {} with status {}",
                    userId, cartItemId, cart.getCartId(), cart.getStatus());
            throw new InvalidCartStatusException(cart.getStatus());
        }

        // ========================================================================
        // STEP 5: LOAD AND VALIDATE MENU ITEM
        // ========================================================================
        MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                .orElseThrow(() -> new MenuItemNotFoundException(cartItem.getMenuItemId()));
        log.info("Found menu item {} for cart item {}", menuItem.getMenuItemId(), cartItemId);
        // ========================================================================
        // STEP 6: CHECK MENU ITEM IS ACTIVE
        // ========================================================================
        if (!Boolean.TRUE.equals(menuItem.getMenuItemIsActive())) {
            log.warn("User {} attempted to modify cart item {} belonging to menu item {} with status {}",
                    userId, cartItemId, menuItem.getMenuItemId(), menuItem.getMenuItemIsActive());
            throw new MenuItemNotFoundException(menuItem.getMenuItemId());
        }
        // ========================================================================
        // STEP 7: CHECK STOCK AVAILABILITY (IF INCREASING QUANTITY)
        // ========================================================================
        Integer deltaQuantity = request.getNewQuantity() - cartItem.getCartItemQuantity();
        if (deltaQuantity > 0 && menuItem.getMenuItemStockQuantity() != null) {
            if (menuItem.getMenuItemStockQuantity() < deltaQuantity) {
                log.warn("User {} attempted to modify cart item {} belonging to menu item {} with quantity {} which exceeds stock quantity {}",
                        userId, cartItemId, menuItem.getMenuItemId(), request.getNewQuantity(), menuItem.getMenuItemStockQuantity());
                throw new MenuItemNotFoundException(menuItem.getMenuItemId());
            }
        }

        // ========================================================================
        // STEP 8: HANDLE MODIFICATION - REMOVE OR UPDATE
        // ========================================================================
        CartItemDTO cartItemDTO = null;
        if (request.getNewQuantity() == 0) {
            // Remove cart item
            log.info("Removing cart item {} from cart {}", cartItemId, cart.getCartId());
            cartItemRepository.delete(cartItem);
            cart.getItems().remove(cartItem);
        } else {
            // Update item
            log.info("Updating cart item {} quantity to {}", cartItemId, request.getNewQuantity());
            cartItem.setCartItemQuantity(request.getNewQuantity());
            if (cartItem.getCartItemSpecialInstructions() != null) {
                cartItem.setCartItemSpecialInstructions(request.getNewSpecialInstructions());
            }
            cartItem.calculateTotalPrice();
            cartItem.setUpdatedBy(userId);
            cartItemRepository.save(cartItem);
            cartItemDTO = buildCardItemDTO(cartItem, menuItem);
            log.info("Updated cart item {} with quantity {}", cartItemId, cartItemDTO.getQuantity());
        }
            // Update cart
            cart.setUpdatedBy(userId);
            cartRepository.save(cart);

            CartDTO cartDTO = buildCartDTO(cart);


        String message = request.getNewQuantity() == 0
                ? "Cart item removed successfully"
                : "Cart item updated successfully";

        log.info("ModifyCart operation completed for user {} - cart total: {}",
                userId, cartDTO.getTotalAmount());

        return ModifyCartItemResponse.builder()
                .success(true)
                .message(message)
                .cartItem(cartItemDTO)
                .cart(cartDTO)
                .build();
    }

    private CartDTO buildCartDTO(Cart cart) {
        return CartDTO.builder()
                .cartId(cart.getCartId())
                .customerId(cart.getCustomerId())
                .restaurantId(cart.getRestaurantId())
                .status(cart.getStatus())
                .items(cart.getItems().stream().map(item->{
                    MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                            .orElse(null);
                    return buildCardItemDTO(item, menuItem);
                }).collect(Collectors.toList()))
                .totalAmount(cart.calculateTotal())
                .totalItems(cart.getItems().size())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDTO buildCardItemDTO(CartItem item, MenuItem menuItem) {
        return CartItemDTO.builder()
                .cartItemId(item.getCartItemId())
                .menuItemId(menuItem.getMenuItemId())
                .menuItemName(menuItem != null ? menuItem.getMenuItemName() : "Unknown")
                .specialInstructions(item.getCartItemSpecialInstructions())
                .quantity(item.getCartItemQuantity())
                .unitPrice(item.getCartItemUnitPrice())
                .totalPrice(item.getCartItemTotalPrice())
                .build();
    }
}
