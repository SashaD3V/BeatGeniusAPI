package com.WeAre.BeatGenius.services.marketplace.impl;

import com.WeAre.BeatGenius.api.requests.marketplace.CreateOrderRequest;
import com.WeAre.BeatGenius.api.requests.marketplace.UpdateOrderRequest;
import com.WeAre.BeatGenius.domain.entities.Beat;
import com.WeAre.BeatGenius.domain.entities.Order;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.mappers.OrderMapper;
import com.WeAre.BeatGenius.domain.repositories.BeatRepository;
import com.WeAre.BeatGenius.domain.repositories.OrderRepository;
import com.WeAre.BeatGenius.services.marketplace.interfaces.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final BeatRepository beatRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(BeatRepository beatRepository,
                            OrderRepository orderRepository,
                            OrderMapper orderMapper) {
        this.beatRepository = beatRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(CreateOrderRequest request, User authenticatedUser) {
        Beat beat = beatRepository.findById(request.getBeatId())
                .orElseThrow(() -> new EntityNotFoundException("Beat not found"));

        Order order = orderMapper.toEntity(request, beat);
        order.setBuyer(authenticatedUser);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public Order updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Mise à jour des champs modifiables
        // Note: normalement dans une commande, on ne met à jour que le statut
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with id: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByBuyerId(userId);
    }
}