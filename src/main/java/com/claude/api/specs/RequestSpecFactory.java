package com.claude.api.specs;

import com.claude.api.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Centralizes construction of Rest Assured {@link RequestSpecification}
 * instances so that base URL, timeouts, headers, and logging behavior are
 * defined in a single place rather than repeated across endpoint classes.
 */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    /**
     * Builds the default JSON request specification for the configured
     * environment's base URL.
     *
     * @return a configured {@link RequestSpecification}
     */
    public static RequestSpecification defaultSpec() {
        final ConfigManager config = ConfigManager.getInstance();
        final LogDetail logDetail = resolveLogDetail(config.get("request_log_level", "headers"));

        return new RequestSpecBuilder()
                .setBaseUri(config.get("base_url"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(logDetail)
                .build();
    }

    private static LogDetail resolveLogDetail(final String level) {
        return switch (level.toLowerCase()) {
            case "all" -> LogDetail.ALL;
            case "body" -> LogDetail.BODY;
            case "status" -> LogDetail.STATUS;
            case "none" -> LogDetail.STATUS;
            default -> LogDetail.HEADERS;
        };
    }
}
