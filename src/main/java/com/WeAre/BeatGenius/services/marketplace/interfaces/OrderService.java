package com.WeAre.BeatGenius.services.marketplace.interfaces;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateOrderRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateOrderRequest;
import com.WeAre.BeatGenius.domain.entities.Order;
import com.WeAre.BeatGenius.domain.entities.User;

import java.util.List;

public interface OrderService {
    /**
     * Creates a new order for an authenticated user
     *
     * @param request The order creation request containing beat information
     * @param authenticatedUser The authenticated user making the order
     * @return The created order
     * @throws EntityNotFoundException if the beat is not found
     */
    Order createOrder(CreateOrderRequest request, User authenticatedUser);

    /**
     * Retrieves an order by its ID
     *
     * @param orderId The ID of the order to retrieve
     * @return The found order
     * @throws EntityNotFoundException if the order is not found
     */
    Order getOrder(Long orderId);

    /**
     * Updates an existing order
     *
     * @param orderId The ID of the order to update
     * @param request The update request containing new order details
     * @return The updated order
     * @throws EntityNotFoundException if the order is not found
     */
    Order updateOrder(Long orderId, UpdateOrderRequest request);

    /**
     * Deletes an order by its ID
     *
     * @param orderId The ID of the order to delete
     * @throws EntityNotFoundException if the order is not found
     */
    void deleteOrder(Long orderId);

    /**
     * Lists all orders for a specific user
     *
     * @param userId The ID of the user
     * @return List of orders belonging to the user
     */
    List<Order> getUserOrders(Long userId);
}