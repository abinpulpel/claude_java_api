package com.claude.api.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Logs test lifecycle events (start, pass, fail, skip) so execution history
 * is visible in both the console and the rolling log file, independent of
 * whichever report renderer (Allure, Surefire) is also attached.
 */
public class TestListener implements ITestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(final ITestResult result) {
        LOGGER.info("STARTED: {}", describe(result));
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        LOGGER.info("PASSED: {}", describe(result));
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        LOGGER.error("FAILED: {}", describe(result), result.getThrowable());
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        LOGGER.warn("SKIPPED: {}", describe(result));
    }

    @Override
    public void onStart(final ITestContext context) {
        LOGGER.info("Suite started: {}", context.getName());
    }

    @Override
    public void onFinish(final ITestContext context) {
        LOGGER.info("Suite finished: {} (passed={}, failed={}, skipped={})",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    private String describe(final ITestResult result) {
        return "%s.%s".formatted(
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }
}
