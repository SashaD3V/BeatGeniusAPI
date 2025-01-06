package com.WeAre.BeatGenius.domain.entities.generic;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequence")
  @SequenceGenerator(
      name = "defaultSequence",
      sequenceName = "default_sequence",
      allocationSize = 1)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  @Builder.Default
  private Long version = 0L;
}
