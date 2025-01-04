package com.WeAre.BeatGenius.domain.entities;

import com.WeAre.BeatGenius.domain.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
@Data
public class Order {
  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  private Beat beat;

  @ManyToOne(fetch = FetchType.LAZY)
  private License license; // Ajout de la référence à la licence

  @Column(nullable = false)
  private Double totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @CreationTimestamp private LocalDateTime createdAt;
}
