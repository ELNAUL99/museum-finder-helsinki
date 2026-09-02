package com.museumfinder.search;

import com.museumfinder.domain.Museum;
import com.museumfinder.domain.OpeningHour;
import com.museumfinder.domain.Theme;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Turns {@link SearchFilters} into the SQL-side half of a search. */
public final class MuseumSpecifications {

    private MuseumSpecifications() {
    }

    public static Specification<Museum> from(SearchFilters filters,
                                             DayOfWeek today,
                                             LocalTime now,
                                             Optional<HelsinkiPlaces.Place> place,
                                             Optional<Collection<Long>> textMatchIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.freeOnly()) {
                predicates.add(cb.isTrue(root.get("freeEntry")));
            }
            if (filters.museumCardOnly()) {
                predicates.add(cb.isTrue(root.get("museumCard")));
            }
            if (filters.hasPriceCap()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("adultPriceEur"),
                        BigDecimal.valueOf(filters.maxPriceEur())));
            }
            if (filters.wheelchairAccessible()) {
                predicates.add(cb.isTrue(root.get("wheelchairAccessible")));
            }
            if (filters.familyFriendly()) {
                predicates.add(cb.isTrue(root.get("familyFriendly")));
            }
            if (filters.hasCafe()) {
                predicates.add(cb.isTrue(root.get("hasCafe")));
            }

            // Any-of match on themes, expressed as member-of tests so no join duplicates appear.
            if (!filters.themes().isEmpty()) {
                List<Predicate> themeHits = new ArrayList<>();
                for (Theme theme : filters.themes()) {
                    themeHits.add(cb.isMember(theme, root.<Collection<Theme>>get("themes")));
                }
                predicates.add(cb.or(themeHits.toArray(new Predicate[0])));
            }

            List<Integer> requiredDays = filters.openOn().isoDays(today);
            if (filters.openNow() && requiredDays.isEmpty()) {
                requiredDays = List.of(today.getValue());
            }
            if (!requiredDays.isEmpty()) {
                predicates.add(openOnPredicate(root, query, cb, requiredDays,
                        filters.openNow() ? now : null));
            }

            // Bounding box first; the exact great-circle radius is applied in Java afterwards.
            if (place.isPresent() && filters.hasPlace()) {
                double radius = filters.radiusKm() > 0 ? filters.radiusKm() : SearchService.DEFAULT_RADIUS_KM;
                HelsinkiPlaces.Place p = place.get();
                double dLat = GeoUtil.latDelta(radius);
                double dLon = GeoUtil.lonDelta(radius, p.latitude());
                predicates.add(cb.between(root.get("latitude"), p.latitude() - dLat, p.latitude() + dLat));
                predicates.add(cb.between(root.get("longitude"), p.longitude() - dLon, p.longitude() + dLon));
            }

            textMatchIds.ifPresent(ids -> {
                if (!ids.isEmpty()) {
                    predicates.add(root.get("id").in(ids));
                }
            });

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate openOnPredicate(Root<Museum> root,
                                             jakarta.persistence.criteria.CriteriaQuery<?> query,
                                             jakarta.persistence.criteria.CriteriaBuilder cb,
                                             List<Integer> isoDays,
                                             LocalTime atTime) {
        Subquery<Long> sub = query.subquery(Long.class);
        Root<OpeningHour> hours = sub.from(OpeningHour.class);
        sub.select(hours.get("id"));

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(cb.equal(hours.get("museum"), root));
        conditions.add(cb.isFalse(hours.get("closed")));
        conditions.add(hours.get("dayOfWeek").in(isoDays.stream().map(Integer::shortValue).toList()));
        if (atTime != null) {
            conditions.add(cb.lessThanOrEqualTo(hours.get("opensAt"), atTime));
            conditions.add(cb.greaterThan(hours.get("closesAt"), atTime));
        }
        sub.where(cb.and(conditions.toArray(new Predicate[0])));
        return cb.exists(sub);
    }
}
