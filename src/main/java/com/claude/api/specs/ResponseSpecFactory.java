package com.claude.api.specs;

import com.claude.api.config.ConfigManager;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

/**
 * Centralizes construction of Rest Assured {@link ResponseSpecification}
 * instances, keeping common assertions (status code, response logging) out
 * of individual test and endpoint classes.
 */
public final class ResponseSpecFactory {

    private ResponseSpecFactory() {
    }

    /**
     * Builds a response spec asserting the given status code, with response
     * logging behavior driven by configuration.
     *
     * @param expectedStatusCode the HTTP status code the response must match
     * @return a configured {@link ResponseSpecification}
     */
    public static ResponseSpecification expectStatus(final int expectedStatusCode) {
        final ConfigManager config = ConfigManager.getInstance();
        final LogDetail logDetail = resolveLogDetail(config.get("response_log_level", "status"));

        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(logDetail)
                .build();
    }

    private static LogDetail resolveLogDetail(final String level) {
        return switch (level.toLowerCase()) {
            case "all" -> LogDetail.ALL;
            case "body" -> LogDetail.BODY;
            case "headers" -> LogDetail.HEADERS;
            case "none" -> LogDetail.STATUS;
            default -> LogDetail.STATUS;
        };
    }
}
