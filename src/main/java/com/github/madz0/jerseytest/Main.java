package com.github.madz0.jerseytest;

import com.github.madz0.jerseytest.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class Main {
    private final static String PROPERTY_FILE_NAME = "app.properties";
    private final static String BASE_URI = "http://localhost:9998";
    public final static String APP_URL_KEY = "app.url";
    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    private void start() throws URISyntaxException {
        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        Properties properties = getProperties();
        if(!properties.containsKey(APP_URL_KEY)) {
            properties.put(APP_URL_KEY, BASE_URI);
        }
        String url = properties.get(APP_URL_KEY).toString();
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(new URI(url), new AppConfig(properties), locator);
        try {
            httpServer.start();
            System.out.println(String.format("Jersey app started. %s", url));
        } catch (IOException e) {
            log.error("error starting server: " + e.getLocalizedMessage(), e);
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(this.getClass().getClassLoader().getResource(PROPERTY_FILE_NAME)).openStream());
        } catch (IOException e) {
            log.error("error accessing {} input stream.", PROPERTY_FILE_NAME, e);
        }
        return properties;
    }
}