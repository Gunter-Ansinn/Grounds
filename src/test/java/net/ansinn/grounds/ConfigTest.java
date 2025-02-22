package net.ansinn.grounds;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void getProperty() {
        var config = new Config();
        try (var resourceIStream = this.getClass().getResourceAsStream("/test-template/config.properties")) {
            Objects.requireNonNull(resourceIStream);
            config.load(resourceIStream);

            System.out.println(config.getProperty("name"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getOrDefault() {
        var config = new Config();
        try (var resourceIStream = this.getClass().getResourceAsStream("/test-template/config.properties")) {
            Objects.requireNonNull(resourceIStream);
            config.load(resourceIStream);

            System.out.println(config.getOrDefault("test_msg","This will never be read, how funny."));
            System.out.println(config.getOrDefault("uhh","This property doesn't exist and you found out on {today}"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerDynamicProvider() {
        var config = new Config();
        try (var resourceIStream = this.getClass().getResourceAsStream("/test-template/config.properties")) {
            // Internet Status checker
            Config.registerDynamicProvider("test", (cfg, args) -> "test");

            Objects.requireNonNull(resourceIStream);
            config.load(resourceIStream);

            System.out.println(config.getOrDefault("test_new","This will never be read, how funny."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}