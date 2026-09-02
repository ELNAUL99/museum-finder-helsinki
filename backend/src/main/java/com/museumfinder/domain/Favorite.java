package com.museumfinder.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "favorites")
@IdClass(Favorite.Key.class)
public class Favorite {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "museum_id")
    private Long museumId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public Favorite(Long userId, Long museumId) {
        this.userId = userId;
        this.museumId = museumId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Key implements Serializable {
        private Long userId;
        private Long museumId;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key key)) {
                return false;
            }
            return Objects.equals(userId, key.userId) && Objects.equals(museumId, key.museumId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, museumId);
        }
    }
}
