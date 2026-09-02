package com.museumfinder;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseUrlTest {

    @Test
    void translatesAManagedPostgresUrlIntoSpringProperties() {
        Map<String, String> properties = DatabaseUrl.parse(
                "postgresql://postgres.abc123:s3cr3t@aws-0-eu-north-1.pooler.supabase.com:5432/postgres");

        assertThat(properties)
                .containsEntry("spring.datasource.url",
                        "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres?sslmode=require")
                .containsEntry("spring.datasource.username", "postgres.abc123")
                .containsEntry("spring.datasource.password", "s3cr3t");
    }

    @Test
    void acceptsThePostgresSchemeAndDefaultsPortAndDatabase() {
        assertThat(DatabaseUrl.parse("postgres://user:pw@db.example.com/"))
                .containsEntry("spring.datasource.url", "jdbc:postgresql://db.example.com:5432/postgres?sslmode=require");
    }

    /** Generated passwords routinely contain characters that have to travel percent-encoded. */
    @Test
    void decodesPercentEncodedCredentials() {
        assertThat(DatabaseUrl.parse("postgresql://us%40er:p%40ss%2Fword@db.example.com:6543/app"))
                .containsEntry("spring.datasource.username", "us@er")
                .containsEntry("spring.datasource.password", "p@ss/word");
    }

    @Test
    void keepsAnExplicitSslModeAndOtherQueryParameters() {
        assertThat(DatabaseUrl.parse("postgresql://u:p@h:5432/db?sslmode=disable"))
                .containsEntry("spring.datasource.url", "jdbc:postgresql://h:5432/db?sslmode=disable");
        assertThat(DatabaseUrl.parse("postgresql://u:p@h:5432/db?ApplicationName=mf"))
                .containsEntry("spring.datasource.url",
                        "jdbc:postgresql://h:5432/db?ApplicationName=mf&sslmode=require");
    }

    @Test
    void rejectsSomethingThatIsNotAPostgresUrl() {
        assertThatThrownBy(() -> DatabaseUrl.parse("mysql://u:p@h/db"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> DatabaseUrl.parse("jdbc:postgresql://h/db"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
