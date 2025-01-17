package com.WeAre.BeatGenius.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private User sender;

  @ManyToOne private User recipient;

  @Column(nullable = false)
  private String content;

  private LocalDateTime sentAt;

  private boolean read;
}
