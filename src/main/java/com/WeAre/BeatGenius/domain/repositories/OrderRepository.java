package com.WeAre.BeatGenius.domain.repositories;

import com.WeAre.BeatGenius.domain.entities.Order;
import com.WeAre.BeatGenius.domain.enums.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByBuyerId(Long buyerId);

  List<Order> findByBeatProducerId(Long producerId);

  List<Order> findByStatus(OrderStatus status);
}
