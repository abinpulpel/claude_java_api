package com.claude.api.exceptions;

/**
 * Base unchecked exception for all framework-level failures (configuration
 * resolution, data loading, schema validation setup, etc.), as distinct from
 * test assertion failures raised by TestNG.
 */
public class FrameworkException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FrameworkException(final String message) {
        super(message);
    }

    public FrameworkException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
