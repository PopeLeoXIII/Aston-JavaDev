package org.example.repository;

import org.example.db.ConnectionManagerImpl;
import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Testcontainers
@Tag("DockerRequired")
public class TestcontainerManager {
    private static final String INIT_SQL = "db-migration.sql";

    @Container
    private static PostgreSQLContainer<?> postgres;

    private static PostgreSQLContainer<?> getContainer() {
        if (postgres == null) {
            Properties prop = getProperties();

            postgres = new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("vehicle")
                    .withUsername(prop.getProperty("username"))
                    .withPassword(prop.getProperty("password"))
                    .withInitScript(INIT_SQL);
        }
        return postgres;
    }

    public static void start() {
        getContainer().start();

        final String url = postgres.getJdbcUrl();
        ConnectionManagerImpl.setRemote(url);
    }

    public static void stop() {
        ConnectionManagerImpl.setLocal();
        getContainer().stop();
    }

    private static Properties getProperties() {
        try(InputStream stream = TestcontainerManager.class.getClassLoader().getResourceAsStream("db.properties");) {
            Properties prop = new Properties();
            prop.load(stream);
            return prop;
        } catch (IOException e) {
            throw new RuntimeException("Not found properties file (db.properties). ", e);
        }
    }
}
