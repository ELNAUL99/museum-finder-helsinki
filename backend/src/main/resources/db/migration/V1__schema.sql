-- Helsinki Museum Finder :: core schema

CREATE TABLE museums (
    id                   BIGSERIAL PRIMARY KEY,
    slug                 VARCHAR(120) NOT NULL UNIQUE,
    name                 VARCHAR(200) NOT NULL,
    short_description    VARCHAR(400) NOT NULL,
    description          TEXT NOT NULL,
    address              VARCHAR(200) NOT NULL,
    postal_code          VARCHAR(10)  NOT NULL,
    district             VARCHAR(80)  NOT NULL,
    latitude             DOUBLE PRECISION NOT NULL,
    longitude            DOUBLE PRECISION NOT NULL,
    website              VARCHAR(300),
    phone                VARCHAR(40),
    email                VARCHAR(120),
    image_url            VARCHAR(400),
    adult_price_eur      NUMERIC(6,2) NOT NULL DEFAULT 0,
    free_entry           BOOLEAN      NOT NULL DEFAULT FALSE,
    free_entry_note      VARCHAR(200),
    museum_card          BOOLEAN      NOT NULL DEFAULT FALSE,
    wheelchair_accessible BOOLEAN     NOT NULL DEFAULT FALSE,
    family_friendly      BOOLEAN      NOT NULL DEFAULT FALSE,
    has_cafe             BOOLEAN      NOT NULL DEFAULT FALSE,
    has_shop             BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_museums_district ON museums (district);
CREATE INDEX idx_museums_price    ON museums (adult_price_eur);

-- Free-text search vector over name + descriptions, maintained by trigger.
ALTER TABLE museums ADD COLUMN search_vector tsvector;

CREATE FUNCTION museums_search_vector_update() RETURNS trigger AS $$
BEGIN
    NEW.search_vector :=
        setweight(to_tsvector('simple', coalesce(NEW.name, '')), 'A') ||
        setweight(to_tsvector('simple', coalesce(NEW.short_description, '')), 'B') ||
        setweight(to_tsvector('simple', coalesce(NEW.description, '')), 'C') ||
        setweight(to_tsvector('simple', coalesce(NEW.district, '')), 'B');
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_museums_search_vector
    BEFORE INSERT OR UPDATE ON museums
    FOR EACH ROW EXECUTE FUNCTION museums_search_vector_update();

CREATE INDEX idx_museums_search_vector ON museums USING GIN (search_vector);

CREATE TABLE museum_themes (
    museum_id BIGINT NOT NULL REFERENCES museums (id) ON DELETE CASCADE,
    theme     VARCHAR(40) NOT NULL,
    PRIMARY KEY (museum_id, theme)
);

CREATE INDEX idx_museum_themes_theme ON museum_themes (theme);

-- One row per weekday. closed = true means the museum is shut that day.
CREATE TABLE museum_opening_hours (
    id          BIGSERIAL PRIMARY KEY,
    museum_id   BIGINT   NOT NULL REFERENCES museums (id) ON DELETE CASCADE,
    day_of_week SMALLINT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7), -- ISO: 1 = Monday
    closed      BOOLEAN  NOT NULL DEFAULT FALSE,
    opens_at    TIME,
    closes_at   TIME,
    UNIQUE (museum_id, day_of_week)
);

CREATE TABLE exhibitions (
    id          BIGSERIAL PRIMARY KEY,
    museum_id   BIGINT NOT NULL REFERENCES museums (id) ON DELETE CASCADE,
    title       VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE,
    image_url   VARCHAR(400),
    permanent   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_exhibitions_museum ON exhibitions (museum_id);
CREATE INDEX idx_exhibitions_dates  ON exhibitions (start_date, end_date);

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,
    display_name  VARCHAR(80)  NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER',
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE favorites (
    user_id    BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    museum_id  BIGINT NOT NULL REFERENCES museums (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, museum_id)
);
