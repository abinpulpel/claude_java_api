package com.claude.api.listeners;

import com.claude.api.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Retries a failed test method up to the configured {@code retry_count}
 * before letting the failure stand. Applied to every test method via
 * {@link AnnotationTransformer}, so individual test classes never need to
 * declare it explicitly.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryAnalyzer.class);

    private final AtomicInteger attempts = new AtomicInteger(0);

    @Override
    public boolean retry(final ITestResult result) {
        final int maxRetries = ConfigManager.getInstance().getInt("retry_count", 1);
        if (attempts.getAndIncrement() < maxRetries) {
            LOGGER.warn("Retrying {} (attempt {} of {})",
                    result.getMethod().getMethodName(), attempts.get(), maxRetries);
            return true;
        }
        return false;
    }
}
