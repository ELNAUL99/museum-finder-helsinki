-- Themes (controlled vocabulary, mirrored by the Theme enum in the backend)

INSERT INTO museum_themes (museum_id, theme)
SELECT m.id, t.theme FROM museums m
JOIN (VALUES
  ('ateneum','art'), ('ateneum','classical_art'), ('ateneum','history'),
  ('kiasma','art'), ('kiasma','modern_art'), ('kiasma','theatre'),
  ('amos-rex','art'), ('amos-rex','modern_art'), ('amos-rex','technology'), ('amos-rex','children'),
  ('ham','art'), ('ham','modern_art'), ('ham','city_history'),
  ('design-museum','design'), ('design-museum','architecture'), ('design-museum','culture'),
  ('architecture-museum','architecture'), ('architecture-museum','design'),
  ('national-museum','history'), ('national-museum','culture'), ('national-museum','children'),
  ('helsinki-city-museum','city_history'), ('helsinki-city-museum','history'), ('helsinki-city-museum','children'), ('helsinki-city-museum','photography'),
  ('natural-history-museum','natural_history'), ('natural-history-museum','science'), ('natural-history-museum','children'),
  ('seurasaari','open_air'), ('seurasaari','history'), ('seurasaari','culture'), ('seurasaari','children'),
  ('suomenlinna-museum','history'), ('suomenlinna-museum','military'), ('suomenlinna-museum','maritime'),
  ('military-museum-maneesi','military'), ('military-museum-maneesi','history'),
  ('sinebrychoff','art'), ('sinebrychoff','classical_art'), ('sinebrychoff','history'),
  ('photography-museum','photography'), ('photography-museum','art'), ('photography-museum','modern_art'),
  ('hotel-restaurant-museum','culture'), ('hotel-restaurant-museum','history'),
  ('theatre-museum','theatre'), ('theatre-museum','culture'), ('theatre-museum','children'),
  ('technology-museum','technology'), ('technology-museum','science'), ('technology-museum','history'), ('technology-museum','children'),
  ('tram-museum','technology'), ('tram-museum','city_history'), ('tram-museum','children'),
  ('villa-hakasalmi','city_history'), ('villa-hakasalmi','history'),
  ('burghers-house','city_history'), ('burghers-house','history'),
  ('taidehalli','art'), ('taidehalli','modern_art'), ('taidehalli','design'),
  ('didrichsen','art'), ('didrichsen','modern_art'), ('didrichsen','architecture'),
  ('villa-gyllenberg','art'), ('villa-gyllenberg','classical_art'),
  ('mannerheim-museum','history'), ('mannerheim-museum','military'),
  ('urho-kekkonen-museum','history'), ('urho-kekkonen-museum','architecture'),
  ('bank-of-finland-museum','history'), ('bank-of-finland-museum','culture'),
  ('helsinki-university-museum','history'), ('helsinki-university-museum','science'),
  ('sports-museum','sports'), ('sports-museum','history'), ('sports-museum','children'),
  ('observatory','science'), ('observatory','astronomy'), ('observatory','history'),
  ('botanic-garden-greenhouses','botany'), ('botanic-garden-greenhouses','science'), ('botanic-garden-greenhouses','children'),
  ('cygnaeus-gallery','art'), ('cygnaeus-gallery','classical_art'),
  ('aalto-house','architecture'), ('aalto-house','design'),
  ('studio-aalto','architecture'), ('studio-aalto','design'),
  ('sederholm-house','city_history'), ('sederholm-house','children'), ('sederholm-house','history')
) AS t(slug, theme) ON t.slug = m.slug;

-- Opening hours. day_of_week follows ISO-8601: 1 = Monday ... 7 = Sunday.

-- A: closed Mon; Tue/Thu/Fri/Sat/Sun 11-18; Wed 11-20
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, false, '11:00'::time, '18:00'::time),
  (3, false, '11:00'::time, '20:00'::time), (4, false, '11:00'::time, '18:00'::time),
  (5, false, '11:00'::time, '18:00'::time), (6, false, '11:00'::time, '18:00'::time),
  (7, false, '11:00'::time, '18:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('kiasma','amos-rex','ham','design-museum','architecture-museum','taidehalli',
                 'photography-museum','theatre-museum','hotel-restaurant-museum');

-- B: closed Mon; Tue/Thu/Fri 10-18; Wed 10-20; Sat/Sun 10-17
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, false, '10:00'::time, '18:00'::time),
  (3, false, '10:00'::time, '20:00'::time), (4, false, '10:00'::time, '18:00'::time),
  (5, false, '10:00'::time, '18:00'::time), (6, false, '10:00'::time, '17:00'::time),
  (7, false, '10:00'::time, '17:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('ateneum','sinebrychoff','national-museum','natural-history-museum');

-- C: Mon-Fri 11-19, Sat-Sun 11-17 (the free city museum keeps long hours)
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, false, '11:00'::time, '19:00'::time), (2, false, '11:00'::time, '19:00'::time),
  (3, false, '11:00'::time, '19:00'::time), (4, false, '11:00'::time, '19:00'::time),
  (5, false, '11:00'::time, '19:00'::time), (6, false, '11:00'::time, '17:00'::time),
  (7, false, '11:00'::time, '17:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('helsinki-city-museum');

-- D: Mon-Tue closed; Wed-Sun 11-17 (small house museums)
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, true, NULL::time, NULL::time),
  (3, false, '11:00'::time, '17:00'::time), (4, false, '11:00'::time, '17:00'::time),
  (5, false, '11:00'::time, '17:00'::time), (6, false, '11:00'::time, '17:00'::time),
  (7, false, '11:00'::time, '17:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('tram-museum','villa-hakasalmi','burghers-house','sederholm-house',
                 'cygnaeus-gallery','villa-gyllenberg','urho-kekkonen-museum');

-- E: closed Mon; Tue-Sun 11-17
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, false, '11:00'::time, '17:00'::time),
  (3, false, '11:00'::time, '17:00'::time), (4, false, '11:00'::time, '17:00'::time),
  (5, false, '11:00'::time, '17:00'::time), (6, false, '11:00'::time, '17:00'::time),
  (7, false, '11:00'::time, '17:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('didrichsen','technology-museum','observatory','botanic-garden-greenhouses');

-- F: open every day 10:30-18:00 (island museums follow the ferry timetable)
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, false, '10:30'::time, '18:00'::time), (2, false, '10:30'::time, '18:00'::time),
  (3, false, '10:30'::time, '18:00'::time), (4, false, '10:30'::time, '18:00'::time),
  (5, false, '10:30'::time, '18:00'::time), (6, false, '10:30'::time, '18:00'::time),
  (7, false, '10:30'::time, '18:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('suomenlinna-museum','military-museum-maneesi','seurasaari','sports-museum');

-- G: Fri-Sun only, guided visits 11-16
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, true, NULL::time, NULL::time),
  (3, true, NULL::time, NULL::time), (4, true, NULL::time, NULL::time),
  (5, false, '11:00'::time, '16:00'::time), (6, false, '11:00'::time, '16:00'::time),
  (7, false, '11:00'::time, '16:00'::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('mannerheim-museum');

-- H: Tue-Sat 11-16, guided tours only (the Aalto properties)
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, true, NULL::time, NULL::time), (2, false, '11:00'::time, '16:00'::time),
  (3, false, '11:00'::time, '16:00'::time), (4, false, '11:00'::time, '16:00'::time),
  (5, false, '11:00'::time, '16:00'::time), (6, false, '11:00'::time, '16:00'::time),
  (7, true, NULL::time, NULL::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('aalto-house','studio-aalto');

-- I: weekdays only, 11-17 (institutional museums)
INSERT INTO museum_opening_hours (museum_id, day_of_week, closed, opens_at, closes_at)
SELECT m.id, s.dow, s.closed, s.opens, s.closes FROM museums m
CROSS JOIN (VALUES
  (1, false, '11:00'::time, '17:00'::time), (2, false, '11:00'::time, '17:00'::time),
  (3, false, '11:00'::time, '17:00'::time), (4, false, '11:00'::time, '17:00'::time),
  (5, false, '11:00'::time, '17:00'::time), (6, true, NULL::time, NULL::time),
  (7, true, NULL::time, NULL::time)
) AS s(dow, closed, opens, closes)
WHERE m.slug IN ('bank-of-finland-museum','helsinki-university-museum');
