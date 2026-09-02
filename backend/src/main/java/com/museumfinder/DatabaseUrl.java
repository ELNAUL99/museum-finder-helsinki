package com.museumfinder;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Most hosting platforms - Supabase, Render, Railway, Heroku, Fly - hand the database
 * over as a single {@code DATABASE_URL} in URI form. JDBC cannot read that, so translate
 * it into the three properties Spring actually wants.
 *
 * <p>Nothing happens when the variable is absent, which is the local and test case.
 */
final class DatabaseUrl {

    private DatabaseUrl() {
    }

    /** Reads DATABASE_URL from the environment and applies it as system properties. */
    static void applyIfPresent() {
        Optional.ofNullable(System.getenv("DATABASE_URL"))
                .filter(value -> !value.isBlank())
                .map(DatabaseUrl::parse)
                .ifPresent(properties -> properties.forEach(System::setProperty));
    }

    /**
     * @param databaseUrl e.g. {@code postgresql://user:secret@db.example.com:5432/postgres}
     * @return spring.datasource.* properties
     * @throws IllegalArgumentException if the value is not a usable postgres URI
     */
    static Map<String, String> parse(String databaseUrl) {
        URI uri = URI.create(databaseUrl.trim());
        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equals("postgres") || scheme.equals("postgresql"))) {
            throw new IllegalArgumentException("DATABASE_URL must be a postgres:// or postgresql:// URI");
        }
        if (uri.getHost() == null) {
            throw new IllegalArgumentException("DATABASE_URL has no host");
        }

        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String database = uri.getPath() == null || uri.getPath().length() <= 1 ? "postgres" : uri.getPath().substring(1);

        // Managed Postgres is TLS-only, so default sslmode unless the URL already sets it.
        String query = uri.getQuery();
        String params = query == null || query.isBlank() ? "sslmode=require"
                : query.contains("sslmode=") ? query
                : query + "&sslmode=require";

        Map<String, String> properties = new LinkedHashMap<>();
        properties.put("spring.datasource.url",
                "jdbc:postgresql://" + uri.getHost() + ":" + port + "/" + database + "?" + params);

        String userInfo = uri.getUserInfo();
        if (userInfo != null && !userInfo.isBlank()) {
            int separator = userInfo.indexOf(':');
            String user = separator < 0 ? userInfo : userInfo.substring(0, separator);
            properties.put("spring.datasource.username", decode(user));
            if (separator >= 0) {
                properties.put("spring.datasource.password", decode(userInfo.substring(separator + 1)));
            }
        }
        return properties;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
