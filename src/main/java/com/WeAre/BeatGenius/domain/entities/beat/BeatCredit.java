package com.WeAre.BeatGenius.domain.entities.beat;

import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.entities.generic.BaseEntity;
import com.WeAre.BeatGenius.domain.enums.CreditStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "beat_credits")
@Data
@EqualsAndHashCode(callSuper = true)
public class BeatCredit extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beat_id", nullable = false)
  private Beat beat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producer_id", nullable = false)
  private User producer;

  @Column(nullable = false)
  private String role;

  @Column(name = "profit_share", nullable = false)
  private Double profitShare = 0.0;

  @Column(name = "publishing_share", nullable = false)
  private Double publishingShare = 0.0;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "validation_date")
  private LocalDateTime validationDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CreditStatus status = CreditStatus.PENDING;
}
