package com.claude.api.config;

import com.claude.api.enums.Environment;
import com.claude.api.exceptions.FrameworkException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe, lazily-initialized singleton providing layered configuration
 * resolution.
 *
 * <p>Resolution order for a given key, highest priority first:</p>
 * <ol>
 *   <li>JVM system property of the same name (passed via {@code -D})</li>
 *   <li>Environment variable of the same name, uppercased</li>
 *   <li>{@code config-<env>.yaml} on the classpath</li>
 *   <li>Base {@code config.yaml} on the classpath</li>
 * </ol>
 */
public final class ConfigManager {

    private static volatile ConfigManager instance;

    private final Environment environment;
    private final Map<String, Object> mergedConfig;

    private ConfigManager() {
        this.environment = Environment.fromString(
                System.getProperty("env", System.getenv("ENV")));
        this.mergedConfig = loadMergedConfig(environment);
    }

    /**
     * @return the process-wide {@link ConfigManager} instance, creating it on
     * first access.
     */
    public static ConfigManager getInstance() {
        ConfigManager result = instance;
        if (result == null) {
            synchronized (ConfigManager.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ConfigManager();
                }
            }
        }
        return result;
    }

    /**
     * @return the resolved {@link Environment} for this JVM run.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Resolves a configuration value as a {@link String}.
     *
     * @param key the configuration key
     * @return the resolved value, or {@code null} if not found at any layer
     */
    public String get(final String key) {
        final String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        final String envVar = System.getenv(key.toUpperCase());
        if (envVar != null) {
            return envVar;
        }
        final Object value = mergedConfig.get(key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * Resolves a configuration value with a fallback default.
     *
     * @param key          the configuration key
     * @param defaultValue the value to return if unresolved
     * @return the resolved value, or {@code defaultValue}
     */
    public String get(final String key, final String defaultValue) {
        final String value = get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Resolves a configuration value as an {@code int}.
     *
     * @param key          the configuration key
     * @param defaultValue the value to return if unresolved or unparsable
     * @return the resolved integer value
     */
    public int getInt(final String key, final int defaultValue) {
        final String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Resolves a configuration value as a {@code boolean}.
     *
     * @param key          the configuration key
     * @param defaultValue the value to return if unresolved
     * @return the resolved boolean value
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        final String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadMergedConfig(final Environment env) {
        final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        final Map<String, Object> base = readYaml(yamlMapper, "config/config.yaml", true);
        final Map<String, Object> overlay =
                readYaml(yamlMapper, "config/config-" + env.configSuffix() + ".yaml", false);

        final Map<String, Object> merged = new HashMap<>(base);
        merged.putAll(overlay);
        return merged;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readYaml(final ObjectMapper mapper, final String classpathLocation,
                                          final boolean required) {
        try (InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(classpathLocation)) {
            if (stream == null) {
                if (required) {
                    throw new FrameworkException("Required configuration file not found: " + classpathLocation);
                }
                return new HashMap<>();
            }
            final Map<String, Object> data = mapper.readValue(stream, Map.class);
            return data == null ? new HashMap<>() : data;
        } catch (IOException ex) {
            throw new FrameworkException("Failed to read configuration file: " + classpathLocation, ex);
        }
    }
}
