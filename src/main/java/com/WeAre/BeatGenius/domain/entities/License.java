package com.WeAre.BeatGenius.domain.entities;

import com.WeAre.BeatGenius.domain.enums.LicenseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "licenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LicenseType type;

    @Column(nullable = false)
    private BigDecimal price;

    private String rights;

    @ManyToOne
    private Beat beat;
}