// Beat.java
package com.WeAre.BeatGenius.domain.entities;

import com.WeAre.BeatGenius.domain.enums.Genre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "beats")
@Data
@EqualsAndHashCode(callSuper = true)
public class Beat extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private User producer;
}