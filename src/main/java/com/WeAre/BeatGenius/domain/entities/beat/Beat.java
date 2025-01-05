// Beat.java
package com.WeAre.BeatGenius.domain.entities.beat;

import com.WeAre.BeatGenius.domain.entities.BaseEntity;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.Note;
import com.WeAre.BeatGenius.domain.enums.Scale;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "beats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Beat extends BaseEntity {
  @Column(nullable = false)
  private String title;

  @Column(name = "audio_url", nullable = false)
  private String audioUrl;

  @Column(nullable = false)
  private String description;

  @ElementCollection
  @CollectionTable(name = "beat_tags", joinColumns = @JoinColumn(name = "beat_id"))
  @Column(name = "tag")
  @Builder.Default
  private List<String> tags = new ArrayList<>();

  @Column(name = "bpm")
  private Integer bpm;

  @Column(name = "note")
  @Enumerated(EnumType.STRING)
  private Note note;

  @Column(name = "scale")
  @Enumerated(EnumType.STRING)
  private Scale scale;

  @ElementCollection
  @CollectionTable(name = "beat_moods", joinColumns = @JoinColumn(name = "beat_id"))
  @Column(name = "mood")
  @Builder.Default
  private List<String> moods = new ArrayList<>();

  @ElementCollection
  @CollectionTable(name = "beat_instruments", joinColumns = @JoinColumn(name = "beat_id"))
  @Column(name = "instrument")
  @Builder.Default
  private List<String> instruments = new ArrayList<>();

  @Column(name = "release_date")
  private LocalDateTime releaseDate;

  @Column(name = "include_bulk_discounts")
  @Builder.Default
  private Boolean includeForBulkDiscounts = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Genre genre;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producer_id", nullable = false)
  @ToString.Exclude
  @JsonBackReference
  private User producer;

  @OneToMany(mappedBy = "beat", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<License> licenses = new ArrayList<>();

  @OneToMany(mappedBy = "beat", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<BeatCredit> beatCredits = new ArrayList<>();
}
