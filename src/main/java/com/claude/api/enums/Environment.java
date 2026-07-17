package com.claude.api.enums;

import com.claude.api.exceptions.FrameworkException;

/**
 * Supported execution environments. Backs the layered configuration
 * resolution performed by {@link com.claude.api.config.ConfigManager}.
 */
public enum Environment {

    QA,
    STAGING,
    PROD;

    /**
     * Resolves an {@link Environment} from a case-insensitive string, as
     * supplied via the {@code env} system property or {@code ENV} variable.
     *
     * @param value the raw environment name
     * @return the matching {@link Environment}
     * @throws FrameworkException if the value does not match a known environment
     */
    public static Environment fromString(final String value) {
        if (value == null || value.isBlank()) {
            return QA;
        }
        try {
            return Environment.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new FrameworkException("Unsupported environment: " + value, ex);
        }
    }

    /**
     * @return the lowercase environment name, matching the
     * {@code config-<env>.yaml} file naming convention.
     */
    public String configSuffix() {
        return name().toLowerCase();
    }
}
