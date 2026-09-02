package com.museumfinder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "museums")
public class Museum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private String website;
    private String phone;
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "adult_price_eur", nullable = false)
    private BigDecimal adultPriceEur = BigDecimal.ZERO;

    @Column(name = "free_entry", nullable = false)
    private boolean freeEntry;

    @Column(name = "free_entry_note")
    private String freeEntryNote;

    /** Accepted by the Finnish Museum Card (Museokortti). */
    @Column(name = "museum_card", nullable = false)
    private boolean museumCard;

    @Column(name = "wheelchair_accessible", nullable = false)
    private boolean wheelchairAccessible;

    @Column(name = "family_friendly", nullable = false)
    private boolean familyFriendly;

    @Column(name = "has_cafe", nullable = false)
    private boolean hasCafe;

    @Column(name = "has_shop", nullable = false)
    private boolean hasShop;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "museum_themes", joinColumns = @JoinColumn(name = "museum_id"))
    @Column(name = "theme", nullable = false)
    private Set<Theme> themes = new LinkedHashSet<>();

    /**
     * A Set, not a List: opening hours are fetched in the same entity graph as themes, and a
     * List bag would be duplicated once per theme row by the join.
     */
    @OneToMany(mappedBy = "museum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfWeek ASC")
    private Set<OpeningHour> openingHours = new LinkedHashSet<>();

    @OneToMany(mappedBy = "museum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("startDate DESC")
    private List<Exhibition> exhibitions = new ArrayList<>();
}
