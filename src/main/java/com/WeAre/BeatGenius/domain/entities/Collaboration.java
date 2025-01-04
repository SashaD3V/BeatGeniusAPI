package com.WeAre.BeatGenius.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "collaborations")
@Data
public class Collaboration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beat_id", nullable = false)
  private Beat beat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collaborator_id", nullable = false)
  private User collaborator;

  @Column(nullable = false)
  private String role;

  @Column(name = "profit_share", nullable = false)
  private Double profitShare = 0.0;

  @Column(name = "publishing_share", nullable = false)
  private Double publishingShare = 0.0;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
