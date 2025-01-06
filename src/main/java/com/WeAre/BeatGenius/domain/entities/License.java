package com.WeAre.BeatGenius.domain.entities;

import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.entities.generic.BaseEntity;
import com.WeAre.BeatGenius.domain.enums.LicenseType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "licenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class License extends BaseEntity {

  @Builder.Default private String name = null;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private LicenseType type = null;

  @Column(nullable = false)
  @Builder.Default
  private BigDecimal price = null;

  @Column(name = "file_format")
  @Builder.Default
  private String fileFormat = null;

  @Builder.Default private String rights = null;

  @Builder.Default
  @Column(name = "is_tagged")
  private Boolean isTagged = false;

  @Column(length = 1000)
  @Builder.Default
  private String contractTerms = null;

  @Column(name = "distribution_limit")
  @Builder.Default
  private Integer distributionLimit = null;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beat_id")
  @Builder.Default
  private Beat beat = null;
}
