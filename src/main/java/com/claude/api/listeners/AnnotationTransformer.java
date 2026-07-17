package com.claude.api.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Attaches {@link RetryAnalyzer} to every {@code @Test} method at runtime, so
 * retry behavior is centrally controlled rather than requiring
 * {@code retryAnalyzer = RetryAnalyzer.class} on each test.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                           final Constructor testConstructor, final Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
