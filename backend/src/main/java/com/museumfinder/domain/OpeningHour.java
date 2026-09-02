package com.museumfinder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "museum_opening_hours")
public class OpeningHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "museum_id", nullable = false)
    private Museum museum;

    /** ISO-8601 day number: 1 = Monday ... 7 = Sunday. */
    @Column(name = "day_of_week", nullable = false)
    private short dayOfWeek;

    @Column(nullable = false)
    private boolean closed;

    @Column(name = "opens_at")
    private LocalTime opensAt;

    @Column(name = "closes_at")
    private LocalTime closesAt;

    @Transient
    public DayOfWeek day() {
        return DayOfWeek.of(dayOfWeek);
    }

    /** True when {@code time} falls inside the opening window for this day. */
    public boolean isOpenAt(LocalTime time) {
        return !closed && opensAt != null && closesAt != null
                && !time.isBefore(opensAt) && time.isBefore(closesAt);
    }
}
