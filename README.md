# Museum Finder Helsinki

Find a museum in Helsinki by describing the afternoon you want — *"free art museums open on
Sunday near Kamppi"* — and let the search work out the filters. Whatever the model decides is
shown back as removable chips, so a wrong guess takes one click to correct.

Spring Boot 3.5 · React 19 + MUI · PostgreSQL 16 · Claude for natural-language search.

---

## Where this came from

This is a rebuild of a school project I worked on in Germany in 2022:
[titichayajojo/museum-finder](https://github.com/titichayajojo/museum-finder), a Frankfurt museum
finder written with three coursemates in Java and Spring Boot, with Thymeleaf pages and MariaDB
behind them. It did the job — search, museum and exhibition pages, accounts, an admin console —
and the parts of it I still like are still here: the domain model of museums, exhibitions and
tags, and the idea that a city's museums are worth a search engine of their own.

What changed is everything around that idea. The city is Helsinki. The server no longer renders
the pages, so the frontend is a real React app instead of server-side templates. And the search
does the thing we wanted in 2022 and could not build: you describe the afternoon you want in a
sentence, and the filters get worked out for you.

| | 2022 · Frankfurt | Now · Helsinki |
|---|---|---|
| Search | One keyword, `LIKE`-matched against name, tags and address | A sentence → structured filters, via Claude |
| Frontend | Thymeleaf templates, server-rendered | React 19 + MUI, typed client, Leaflet map |
| Database | MariaDB, `ddl-auto` generated schema | PostgreSQL 16, Flyway migrations, full-text index |
| Location | Free-text address field | Coordinates, an offline gazetteer, radius search |
| Opening hours | A free-text blob | One row per weekday, so "open on Sunday" is a query |
| Auth | Session-based, with an admin console | Stateless JWT, scoped to what a visitor needs |
| Tests | — | Unit tests over the search layer |

## What it does

- **Natural-language search.** A question goes to the Claude Messages API with a JSON schema
  derived from `SearchFilters`. Claude fills in the schema; the app turns that into a JPA
  Specification. The model can only produce filters the query layer actually implements.
- **Transparent, correctable results.** Every extracted filter renders as a chip. Delete one and
  the search re-runs from the structured filters without consulting the model again.
- **Works with no API key.** A rule-based interpreter handles free/price/day/place/theme/keyword
  parsing. The AI layer is an upgrade, not a dependency — and if a Claude call fails at runtime,
  the request silently falls back rather than erroring.
- **34 real Helsinki museums** with coordinates, per-weekday opening hours, prices, Museum Card
  status, accessibility flags, themes and exhibitions.
- **Map, detail pages, accounts and saved museums** (JWT, BCrypt, stateless).

## Search anatomy

```
"free art museums open on Sunday near Kamppi"
        │
        ▼
ClaudeQueryInterpreter ──(no key / API error)──► HeuristicQueryInterpreter
        │                                                  │
        └──────────────► SearchFilters ◄───────────────────┘
                              │
        ┌─────────────────────┼──────────────────────┐
        ▼                     ▼                      ▼
  MuseumSpecifications   full-text ids        HelsinkiPlaces
  (price, themes,        (search_vector,      (offline gazetteer
   opening hours,         GIN index)           → lat/lon)
   flags, bbox)
        │
        ▼
  exact haversine radius → keyword scoring → sort → results + filter chips
```

Two deliberate choices worth naming:

- **Filters, not embeddings.** Hard constraints — price, a Sunday, step-free access — are exactly
  what a visitor asks about, and a vector search answers those badly. Schema extraction keeps the
  query debuggable and the cost to one small call.
- **Keywords are the softest signal.** If a keyword empties the result set when combined with the
  other filters, it is dropped and the response says so, rather than showing a blank page.

## Running it

**Prerequisites:** Java 21, Node 20.19+ or 22.12+, and PostgreSQL (Docker or local).

```bash
docker compose up -d
```

```bash
cd backend && ./mvnw spring-boot:run
```

```bash
cd frontend && npm install && npm run dev
```

The API is on `http://localhost:8080`, the app on `http://localhost:5173` (Vite proxies `/api`).
Flyway creates the schema and loads all seed data on first start — no manual SQL needed.

### Turning on AI search

```bash
export ANTHROPIC_API_KEY=sk-ant-...
```

Restart the backend. It logs which interpreter is active at startup, and `GET /api/meta` reports
`aiSearchEnabled` so the UI can say so honestly. The model is set by `museumfinder.ai.model`
(default `claude-opus-5`); interpretations are cached for an hour per distinct question.

### Using a local PostgreSQL instead of Docker

```bash
psql -d postgres -c "CREATE ROLE museumfinder LOGIN PASSWORD 'museumfinder'" -c "CREATE DATABASE museumfinder OWNER museumfinder"
```

### Tests

```bash
cd backend && ./mvnw test
```

22 unit tests covering the keyword interpreter, the gazetteer, filter normalisation, geo maths,
`DATABASE_URL` parsing, and — without spending a token — that `SearchFilters` still converts to a
valid JSON schema.

## Deploying

The backend ships as a Docker image (`backend/Dockerfile`, multi-stage: Maven build → Temurin
JRE), so it runs anywhere that takes a container. The frontend is a static bundle.

The deployed setup is a free-tier stack:

| Piece | Where | Notes |
|---|---|---|
| Database | Supabase (`eu-north-1`) | Free tier; pauses after a week of no traffic |
| API | Render web service, Docker, Frankfurt | Free instance sleeps after 15 min idle |
| Frontend | Vercel | Free, CDN-backed |

Any host that supplies a `DATABASE_URL` works without code changes: `DatabaseUrl` translates the
`postgres://user:pass@host:port/db` URI form into the JDBC URL, username and password Spring
wants, defaulting `sslmode=require`. Set these on the API service:

| Variable | Value |
|---|---|
| `DATABASE_URL` | The Postgres connection URI (use the **session** pooler, not transaction — Flyway needs session-mode) |
| `JWT_SECRET` | `openssl rand -base64 48` |
| `CORS_ORIGINS` | The frontend's origin, e.g. `https://your-app.vercel.app` |
| `ANTHROPIC_API_KEY` | Optional; without it the deployed app uses the keyword interpreter |

And on the frontend build: `VITE_API_BASE_URL` = the API's origin.

Flyway runs the migrations on first boot, so a fresh database seeds itself.

## API

| Method | Path | Auth | Purpose |
|---|---|---|---|
| `POST` | `/api/search` | — | `{"q": "..."}` for AI search, or `{"filters": {...}}` for structured |
| `GET` | `/api/museums` | — | All museums |
| `GET` | `/api/museums/{slug}` | — | Detail with hours and exhibitions |
| `GET` | `/api/exhibitions/current` | — | Everything on show today |
| `GET` | `/api/meta` | — | Themes, places, examples, whether AI search is on |
| `POST` | `/api/auth/register` · `/api/auth/login` | — | Returns a JWT |
| `GET` | `/api/auth/me` | JWT | Current user |
| `GET`/`PUT`/`DELETE` | `/api/favorites[/{museumId}]` | JWT | Saved museums |

```bash
curl -s localhost:8080/api/search -H 'Content-Type: application/json' \
  -d '{"q":"design and architecture museums under 15 euros"}'
```

## Layout

```
backend/src/main/java/com/museumfinder/
  domain/    Museum, Exhibition, OpeningHour, Theme, AppUser, Favorite
  repo/      Spring Data repositories (+ the full-text candidate query)
  search/    SearchFilters, the two interpreters, specifications, gazetteer, service
  security/  JWT issue/parse, filter, stateless config
  web/       Controllers and DTOs
backend/src/main/resources/db/migration/   V1 schema, V2-V4 seed data
frontend/src/
  api/       Typed client and DTO mirrors
  components/ SearchBar, FilterChips, FilterDrawer, MuseumCard, MuseumCover, MuseumMap
  pages/     Search, Museum, Favorites, Login
```

## Configuration

| Variable | Default | Notes |
|---|---|---|
| `ANTHROPIC_API_KEY` | — | Unset means keyword-only search |
| `AI_SEARCH_ENABLED` | `true` | Force the fallback interpreter |
| `DB_URL` / `DB_USER` / `DB_PASSWORD` | localhost / museumfinder / museumfinder | |
| `JWT_SECRET` | dev placeholder | **Set this in any real deployment** (32+ chars) |
| `CORS_ORIGINS` | `http://localhost:5173` | Comma-separated |

## About the data

The 34 museums are real, and so are their addresses, districts and coordinates. **Prices and
opening hours are realistic 2025-era approximations, not a live feed** — treat them as demo data
and check the museum's own site before travelling.

There is no photography. Stock images of the wrong buildings are worse than none, so `image_url`
is seeded `NULL` and the frontend draws a generated cover instead: a palette and monogram derived
from the museum's slug, stable across visits. Fill `image_url` with licensed photography and the
cover is replaced automatically, no code change needed.

The natural next step would be pulling live data from the
[MyHelsinki Open API](https://open-api.myhelsinki.fi/) or Helsinki's
[Linked Events](https://api.hel.fi/linkedevents/v1/) into the same schema; the seed migrations are
structured so an importer can write to the same tables.
